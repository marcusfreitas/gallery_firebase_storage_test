package com.example.marcusfreitas.gallerytestapp.main.navigation

import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage

interface MainNavigationControllerInterface {
    fun openImagePicker()
    fun openDetailView(uploadedImage: UploadedImage)
}