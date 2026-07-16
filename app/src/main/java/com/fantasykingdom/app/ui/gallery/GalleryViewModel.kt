package com.fantasykingdom.app.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.GalleryImage
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryUiState(
    val isLoading: Boolean = true,
    val images: List<GalleryImage> = emptyList(),
    val selectedImage: GalleryImage? = null
)

/** Feature #11 — Image Gallery, backed by images stored in Firebase Storage. */
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val images = parkRepository.getGalleryImages()
            _uiState.value = GalleryUiState(isLoading = false, images = images)
        }
    }

    fun selectImage(image: GalleryImage?) {
        _uiState.value = _uiState.value.copy(selectedImage = image)
    }
}
