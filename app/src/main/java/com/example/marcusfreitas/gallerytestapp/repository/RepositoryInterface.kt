package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class RepositoryInterface {

    interface RepositoryMethods {
        fun uploadImage(imageUri: Uri, listener: RepositoryInterface.OnUploadImage)
        fun getUploadedImageList() : ArrayList<UploadedImage>
        fun startDataListener()
        fun setOnDataChangedListener(listener: OnDataChanged)
    }

    interface OnUploadImage {
        fun onSuccess(imageUrl: String)
        fun onError(throwable: Throwable)
    }

    interface OnDataChanged {
        fun updatedData(data: List<UploadedImage>)
        fun cancelled(error: DatabaseError)
    }
}
