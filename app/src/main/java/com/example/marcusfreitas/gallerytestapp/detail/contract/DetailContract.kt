package com.example.marcusfreitas.gallerytestapp.detail.contract

import android.os.Bundle
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface

class DetailContract {

    interface DetailPresenterContract {
        fun loadPhoto()
        fun attachView(view: DetailContract.DetailViewContract)
        fun detachView()
        fun deletePhoto()
    }

    interface DetailViewContract {
        var uploadedImage: UploadedImageInterface?
        fun showPhoto(url: String)
        fun getBundle() : Bundle?
        fun showNoPhotos()
        fun closeDetail()
        fun deleteErrorMessage()
    }

}