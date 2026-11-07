package com.resonance.music.ui.screens.lyrics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LyricsScreen(
    onBackClick: () -> Unit,
    viewModel: LyricsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // 智能居中平滑滚动：动态计算偏移使当前高亮句始终停留在视口黄金居中位置
    LaunchedEffect(uiState.currentLineIndex) {
        if (uiState.currentLineIndex >= 0 && uiState.isSynced) {
            val visibleHeight = listState.layoutInfo.viewportSize.height
            val centerOffset = (visibleHeight / 3)
            listState.animateScrollToItem(
                index = uiState.currentLineIndex,
                scrollOffset = -centerOffset
            )
        }
    }

    // 毫秒级播放进度轮询
    LaunchedEffect(uiState.isSynced) {
        if (uiState.isSynced) {
            while (true) {
                viewModel.updateCurrentPosition()
                delay(120)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. 沉浸式毛玻璃弥散背景 (Apple Music 风格动态氛围底色)
        if (uiState.coverArtUrl != null) {
            AsyncImage(
                model = uiState.coverArtUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.45f }
                    .blur(radius = 70.dp),
                contentScale = ContentScale.Crop
            )
        }
        // 半透明暗色遮罩保底，确保任何主题下歌词清晰易读
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Column {
                            Text(
                                text = uiState.title.ifBlank { "实时歌词" },
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (uiState.artist.isNotBlank()) {
                                Text(
                                    text = uiState.artist,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "关闭歌词")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.lines.isEmpty() && uiState.plainLyrics.isNullOrBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.MusicNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(56.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "暂无歌词",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    uiState.isSynced -> {
                        // 现代全景居中动效歌词系统
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(22.dp)
                        ) {
                            itemsIndexed(uiState.lines, key = { index, _ -> index }) { index, line ->
                                val isActive = index == uiState.currentLineIndex
                                val isPassed = index < uiState.currentLineIndex

                                // 动效平滑过渡：缩放比例与透明度动画
                                val scale by animateFloatAsState(
                                    targetValue = if (isActive) 1.05f else 0.96f,
                                    animationSpec = tween(durationMillis = 350),
                                    label = "scale"
                                )
                                val alpha by animateFloatAsState(
                                    targetValue = when {
                                        isActive -> 1.0f
                                        isPassed -> 0.40f
                                        else -> 0.60f
                                    },
                                    animationSpec = tween(durationMillis = 350),
                                    label = "alpha"
                                )
                                val textColor by animateColorAsState(
                                    targetValue = if (isActive) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    animationSpec = tween(durationMillis = 300),
                                    label = "textColor"
                                )

                                Text(
                                    text = line.text,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = if (isActive) 26.sp else 21.sp,
                                        lineHeight = if (isActive) 36.sp else 30.sp,
                                        letterSpacing = (-0.3).sp
                                    ),
                                    color = textColor,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                            this.alpha = alpha
                                        }
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.seekTo(line.startMs) }
                                        .padding(vertical = 4.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }

                    else -> {
                        // 纯文本歌词：带呼吸间距与卡片内边距
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 40.dp)
                        ) {
                            item {
                                Text(
                                    text = uiState.plainLyrics ?: "",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 19.sp,
                                        lineHeight = 32.sp,
                                        letterSpacing = 0.2.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
