package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseError

class RepositoryInterface {

    interface RepositoryMethods {
        fun uploadImage(imageUri: Uri, listener: RepositoryInterface.OnUploadImage)
        fun getUploadedImageList() : ArrayList<UploadedImage>
        fun startDataListener()
        fun setOnDataChangedListener(listener: OnDataChanged)
        fun deleteImage(uploadedImage: UploadedImageInterface): Task<Void>?
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
