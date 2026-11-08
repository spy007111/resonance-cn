package com.resonance.music.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.resonance.music.data.repository.MusicRepository
import com.resonance.music.data.repository.SettingsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Lock-screen / notification lyrics.
 *
 * Android 13+ draws the lock-screen media control from the MediaSession's current
 * MediaMetadata, so the only supported way to put a line of lyrics there is to keep
 * the playing MediaItem's metadata in sync with the song position. [Player.replaceMediaItem]
 * is the documented mechanism for that (the media id/URI stay identical, so the
 * player treats it as the same media). We only touch it when the synced lyric line
 * actually changes — never once per position tick.
 */
class LockScreenLyrics(
    private val player: Player,
    private val musicRepository: MusicRepository,
    private val settings: SettingsStore,
    private val scope: CoroutineScope
) {
    private var tickerJob: Job? = null
    private var loadJob: Job? = null

    /** The untouched (artist-carrying) media item for the current song. */
    private var baseItem: MediaItem? = null
    private var songId: String? = null
    private var lines: List<Pair<Long, String>> = emptyList()
    private var shownLine: String? = null
    private var enabled = true

    init {
        scope.launch {
            settings.lockScreenLyrics.collect { value ->
                enabled = value
                if (!value) {
                    stopTicker()
                    restoreArtist()
                } else {
                    startTicker()
                }
            }
        }
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // replaceMediaItem() re-emits a transition for the same id; ignore it.
                if (mediaItem?.mediaId != songId) start(mediaItem)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) startTicker() else stopTicker()
            }
        })
    }

    private fun start(item: MediaItem?) {
        loadJob?.cancel()
        stopTicker()
        songId = item?.mediaId
        baseItem = item
        lines = emptyList()
        shownLine = null
        if (item == null || !MediaItems.hasSong(item)) return

        val song = MediaItems.song(item)
        loadJob = scope.launch {
            val structured = musicRepository.getStructuredLyrics(song.id)
            val synced = structured.firstOrNull { it.synced && !it.line.isNullOrEmpty() }
                ?: return@launch
            val parsed = synced.line!!.mapNotNull { line ->
                line.start?.let { it to line.value.trim() }
            }.sortedBy { it.first }
            if (parsed.isEmpty()) return@launch
            lines = parsed
            startTicker()
        }
    }

    private fun startTicker() {
        if (!enabled || lines.isEmpty()) return
        if (tickerJob?.isActive == true) return
        tickerJob = scope.launch {
            while (isActive) {
                tickOnce()
                delay(TICK_MS)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun tickOnce() {
        if (!enabled || lines.isEmpty() || !player.isPlaying) return
        val index = lines.indexOfLast { it.first <= player.currentPosition }
        if (index < 0) return
        val text = lines[index].second.ifBlank { "♪" }
        if (text == shownLine) return
        apply(text)
    }

    private fun apply(text: String) {
        val base = baseItem ?: return
        val current = player.currentMediaItem ?: return
        if (current.mediaId != base.mediaId) return
        val metadata = base.mediaMetadata.buildUpon()
            .setArtist(text)
            .setSubtitle(text)
            .build()
        shownLine = text
        player.replaceMediaItem(player.currentMediaItemIndex, base.buildUpon().setMediaMetadata(metadata).build())
    }

    /** Put the artist back once lyrics are off (or gone). */
    private fun restoreArtist() {
        val base = baseItem ?: return
        val current = player.currentMediaItem ?: return
        if (shownLine == null || current.mediaId != base.mediaId) return
        shownLine = null
        player.replaceMediaItem(player.currentMediaItemIndex, base)
    }

    private companion object {
        const val TICK_MS = 400L
    }
}
