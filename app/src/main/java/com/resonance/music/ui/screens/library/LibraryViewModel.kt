package com.resonance.music.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resonance.music.data.api.models.*
import com.resonance.music.data.repository.MusicRepository
import com.resonance.music.playback.PlaybackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AlbumSort(val type: String, val label: String) {
    AZ("alphabeticalByName", "A–Z"),
    NEWEST("newest", "Recently added"),
    RECENT("recent", "Recently played"),
    FREQUENT("frequent", "Most played"),
    RANDOM("random", "Random")
}

data class LibraryUiState(
    val isLoading: Boolean = false,
    val artists: List<ArtistItem> = emptyList(),
    val albums: List<AlbumItem> = emptyList(),
    val albumSort: AlbumSort = AlbumSort.AZ,
    val songs: List<SongItem> = emptyList(),
    val starredArtists: List<ArtistItem> = emptyList(),
    val starredAlbums: List<AlbumItem> = emptyList(),
    val starredSongs: List<SongItem> = emptyList(),
    val error: String? = null,
    val coverArtUrlBuilder: ((String) -> String?)? = null
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private var artistsLoaded = false
    private var albumsLoaded = false
    private var songsLoaded = false
    private var favoritesLoaded = false

    // One stable instance so UiState copies compare equal (a fresh lambda each
    // copy would break equality and force recomposition). 128px suits 48dp rows.
    private val coverArtBuilder: (String) -> String? = { musicRepository.getCoverArtUrl(it, 128) }

    fun loadArtists() {
        if (artistsLoaded) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val artists = musicRepository.getArtists()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    artists = artists,
                    coverArtUrlBuilder = coverArtBuilder
                )
                artistsLoaded = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun loadAlbums() {
        if (albumsLoaded) return
        albumsLoaded = true
        loadAlbumsWith(_uiState.value.albumSort)
    }

    fun loadSongs() {
        if (songsLoaded) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val songs = musicRepository.getAllSongs(limit = 500)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    songs = songs,
                    coverArtUrlBuilder = coverArtBuilder
                )
                songsLoaded = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun playSong(song: SongItem) {
        val songs = _uiState.value.songs
        val index = songs.indexOf(song).coerceAtLeast(0)
        playbackManager.playSongs(songs, index)
    }

    fun playAllSongs(shuffle: Boolean = false) {
        val songs = _uiState.value.songs
        if (songs.isNotEmpty()) {
            val list = if (shuffle) songs.shuffled() else songs
            playbackManager.playSongs(list, 0)
        }
    }

    fun setAlbumSort(sort: AlbumSort) {
        if (sort == _uiState.value.albumSort) return
        _uiState.value = _uiState.value.copy(albumSort = sort)
        loadAlbumsWith(sort)
    }

    private fun loadAlbumsWith(sort: AlbumSort) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val albums = musicRepository.getAlbumList(sort.type, size = 200)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    albums = albums,
                    coverArtUrlBuilder = coverArtBuilder
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun loadFavorites() {
        if (favoritesLoaded) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val starred = musicRepository.getStarred()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    starredArtists = starred.artist ?: emptyList(),
                    starredAlbums = starred.album ?: emptyList(),
                    starredSongs = starred.song ?: emptyList(),
                    coverArtUrlBuilder = coverArtBuilder
                )
                favoritesLoaded = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun playStarredSong(song: SongItem) {
        val songs = _uiState.value.starredSongs
        val index = songs.indexOf(song).coerceAtLeast(0)
        playbackManager.playSongs(songs, index)
    }

    fun playNext(song: SongItem) = playbackManager.playNext(listOf(song))
    fun addToQueue(song: SongItem) = playbackManager.addToQueue(listOf(song))

    /**
     * Re-fetch the visible tab for pull-to-refresh. Suspends until done so the
     * caller can stop the refresh indicator, and never flips isLoading — the
     * current list stays on screen with the pull spinner over it.
     */
    suspend fun refresh(tab: LibraryTab) {
        try {
            when (tab) {
                LibraryTab.Artists -> {
                    val artists = musicRepository.getArtists()
                    _uiState.value = _uiState.value.copy(
                        artists = artists, coverArtUrlBuilder = coverArtBuilder, error = null
                    )
                    artistsLoaded = true
                }
                LibraryTab.Albums -> {
                    val albums = musicRepository.getAlbumList(_uiState.value.albumSort.type, size = 200)
                    _uiState.value = _uiState.value.copy(
                        albums = albums, coverArtUrlBuilder = coverArtBuilder, error = null
                    )
                    albumsLoaded = true
                }
                LibraryTab.Songs -> {
                    val songs = musicRepository.getAllSongs(limit = 500)
                    _uiState.value = _uiState.value.copy(
                        songs = songs, coverArtUrlBuilder = coverArtBuilder, error = null
                    )
                    songsLoaded = true
                }
                LibraryTab.Favorites -> {
                    val starred = musicRepository.getStarred()
                    _uiState.value = _uiState.value.copy(
                        starredArtists = starred.artist ?: emptyList(),
                        starredAlbums = starred.album ?: emptyList(),
                        starredSongs = starred.song ?: emptyList(),
                        coverArtUrlBuilder = coverArtBuilder,
                        error = null
                    )
                    favoritesLoaded = true
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.localizedMessage)
        }
    }
}
