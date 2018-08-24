package com.example.marcusfreitas.gallerytestapp.main.presenter

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.google.firebase.database.DatabaseError
import com.theartofdev.edmodo.cropper.CropImage

class MainPresenter(navigationController: MainNavigationControllerInterface,
                    repository: RepositoryInterface.RepositoryMethods) : MainContract.MainPresenterContract, RepositoryInterface.OnDataChanged {

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
    }

    override fun imagePickerResult(requestCode: Int, resultCode: Int, data: Uri) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                mRepository.uploadImage(data, object : RepositoryInterface.OnUploadImage {
                    override fun onSuccess(imageUrl: String) {
                        mActivityView?.showSuccessMessage()
                    }

                    override fun onError(throwable: Throwable) {
                        mActivityView?.showErrorMessage(throwable)
                    }

                })
            }
        }
    }

    override fun fabButtonClick() {
        mNavigationController.openImagePicker()
    }

    // OnDataChanged

    override fun updatedData(data: List<UploadedImage>) {
        mFragmentView?.setImageUrlList(data)
    }

    override fun cancelled(error: DatabaseError) {
        mFragmentView?.showToastMessage(error.message, Toast.LENGTH_SHORT)
    }

    override fun photoTap(uploadedImage: UploadedImage) {
        mNavigationController.openDetailView(uploadedImage)
    }

    override fun photoLongTap(uploadedImage: UploadedImage) {
//        uploadedImage.storageReference?.delete()

    }

}