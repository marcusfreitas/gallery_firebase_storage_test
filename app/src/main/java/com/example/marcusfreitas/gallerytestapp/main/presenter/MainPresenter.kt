package com.example.marcusfreitas.gallerytestapp.main.presenter

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.google.firebase.database.DatabaseError

class MainPresenter(navigationController: MainNavigationControllerInterface,
                    repository: RepositoryInterface.RepositoryMethods) : MainContract.MainPresenterContract, RepositoryInterface.OnDataChanged {

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1
    }

    private val mNavigationController = navigationController
    private val mRepository = repository
    private var mFragmentView: MainContract.MainFragmentViewContract? = null
    private var mActivityView: MainContract.MainActivityViewContract? = null

    init {
        mRepository.setOnDataChangedListener(this)
    }

    override fun attachFragmentView(fragmentView: MainContract.MainFragmentViewContract) {
        mFragmentView = fragmentView
    }

    override fun detachFragmentView() {
        mFragmentView = null
    }

    override fun attachActivityView(activityView: MainContract.MainActivityViewContract) {
        mActivityView = activityView
    }

    override fun detachActivityView() {
        mActivityView = null
    }

    override fun startDataObserver() {
        mRepository.startDataListener()
//        mFragmentView?.setImageUrlList(mRepository.getUploadedImageList())
    }

    override fun imagePickerResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK
        && data != null && data.data != null) {
            val imageUri = data.data

            mRepository.uploadImage(imageUri, object : RepositoryInterface.OnUploadImage {
                override fun onSuccess(imageUrl: String) {
                    mActivityView?.showSuccessMessage()
                }

                override fun onError(throwable: Throwable) {
                    mActivityView?.showErrorMessage(throwable)
                }

            })
        }

    }

    override fun fabButtonClick(requestCode: Int) {
        mNavigationController.openImagePicker(requestCode)
    }

    // OnDataChanged

    override fun updatedData(data: List<UploadedImage>) {
        mFragmentView?.setImageUrlList(data)
    }

    override fun cancelled(error: DatabaseError) {
        mFragmentView?.showToastMessage(error.message, Toast.LENGTH_SHORT)
    }

}