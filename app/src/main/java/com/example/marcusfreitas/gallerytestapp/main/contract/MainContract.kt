package com.example.marcusfreitas.gallerytestapp.main.contract

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage

class MainContract {
    interface MainPresenterContract {
        fun attachFragmentView(fragmentView: MainContract.MainFragmentViewContract)
        fun detachFragmentView()
        fun attachActivityView(activityView: MainContract.MainActivityViewContract)
        fun detachActivityView()
        fun imagePickerResult(requestCode: Int, resultCode: Int, data: Uri)
        fun fabButtonClick()
        fun startDataObserver()
        fun photoTap(uploadedImage: UploadedImage)
        fun photoLongTap(uploadedImage: UploadedImage)
    }

    interface MainActivityViewContract {
        fun showSuccessMessage()
        fun showErrorMessage(throwable: Throwable)
    }

    interface MainFragmentViewContract {
        fun showToastMessage(message: String, length: Int)
        fun setImageUrlList(imageUrlList: List<UploadedImage>)
    }
}