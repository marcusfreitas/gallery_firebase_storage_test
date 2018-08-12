package com.example.marcusfreitas.gallerytestapp.main.contract

import android.content.Intent
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage

class MainContract {
    interface MainPresenterContract {
        fun attachFragmentView(fragmentView: MainContract.MainFragmentViewContract)
        fun detachFragmentView()
        fun attachActivityView(activityView: MainContract.MainActivityViewContract)
        fun detachActivityView()
        fun imagePickerResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun fabButtonClick(requestCode: Int)
        fun startDataObserver()
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