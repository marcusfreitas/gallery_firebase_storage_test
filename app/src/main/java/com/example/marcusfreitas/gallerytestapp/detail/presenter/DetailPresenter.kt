package com.example.marcusfreitas.gallerytestapp.detail.presenter

import com.example.marcusfreitas.gallerytestapp.detail.contract.DetailContract
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface

class DetailPresenter(val repository: RepositoryInterface.RepositoryMethods) : DetailContract.DetailPresenterContract {

    private var fragmentView: DetailContract.DetailViewContract? = null

    override fun loadPhoto() {
        val uploadedImage = fragmentView?.uploadedImage

        if (uploadedImage != null) {
            fragmentView?.showPhoto(uploadedImage.url)
        } else {
            fragmentView?.showNoPhotos()
        }
    }

    override fun attachView(view: DetailContract.DetailViewContract) {
        fragmentView = view
    }

    override fun detachView() {
        fragmentView = null
    }

    override fun deletePhoto() {
        val uploadedImage: UploadedImageInterface? = fragmentView?.uploadedImage
        if (uploadedImage != null) {
            repository.deleteImage(uploadedImage)
                    ?.addOnSuccessListener {
                        fragmentView?.closeDetail()
                    }
                    ?.addOnFailureListener {
                        fragmentView?.deleteErrorMessage()
                    }
        } else {
            fragmentView?.deleteErrorMessage()
        }

    }

}