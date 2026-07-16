package com.fantasykingdom.app.ui.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/** Feature #11 — Image Gallery: a photo grid with a full-screen lightbox viewer. */
@Composable
fun ImageGalleryScreen(onBack: () -> Unit) {
    val viewModel: GalleryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = { FKTopBar("Gallery", onBack) }) { padding ->
            if (uiState.isLoading) {
                FKLoadingIndicator(modifier = Modifier.padding(padding))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(uiState.images) { image ->
                        AsyncImage(
                            model = image.imageUrl,
                            contentDescription = image.caption,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(2.dp)
                                .aspectRatio(1f)
                                .clickable { viewModel.selectImage(image) }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.selectedImage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.zIndex(10f)
        ) {
            uiState.selectedImage?.let { image ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.92f))
                        .clickable { viewModel.selectImage(null) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = coil.compose.rememberAsyncImagePainter(image.imageUrl),
                        contentDescription = image.caption,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    )
                    IconButton(
                        onClick = { viewModel.selectImage(null) },
                        modifier = Modifier.align(Alignment.TopEnd).padding(20.dp)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                    }
                    if (image.caption.isNotBlank()) {
                        Text(
                            image.caption,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}
