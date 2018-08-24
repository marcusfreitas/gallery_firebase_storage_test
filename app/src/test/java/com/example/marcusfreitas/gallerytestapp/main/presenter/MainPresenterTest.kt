package com.example.marcusfreitas.gallerytestapp.main.presenter

import android.app.Activity
import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.nhaarman.mockitokotlin2.*
import com.theartofdev.edmodo.cropper.CropImage
import org.junit.Before
import org.junit.Test

class MainPresenterTest {

    private lateinit var navigationControllerMock: MainNavigationControllerInterface
    private lateinit var repositoryMock: RepositoryInterface.RepositoryMethods
    private lateinit var presenter: MainPresenter
    private lateinit var fragmentViewMock: MainContract.MainFragmentViewContract

    @Before
    fun setup() {
        navigationControllerMock = mock()
        repositoryMock = mock()
        fragmentViewMock = mock()

        presenter = MainPresenter(navigationControllerMock, repositoryMock)
        presenter.attachFragmentView(fragmentViewMock)
    }

    @Test
    fun loadData() {

        val uploadedImage = UploadedImage("test", "http://test.jpg")

        whenever(repositoryMock.getUploadedImageList()).thenReturn(arrayListOf(uploadedImage))

        presenter.startDataObserver()

        verify(repositoryMock).startDataListener()
    }

    @Test
    fun imagePickerResult_WhenResultCodeOk() {

        val imageUriMock = mock<Uri>()

        presenter.imagePickerResult(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, Activity.RESULT_OK, imageUriMock)

        verify(repositoryMock).uploadImage(eq(imageUriMock), any())
    }

    @Test
    fun imagePickerResult_WhenResultCodeCanceled() {

        val imageUriMock = mock<Uri>()

        presenter.imagePickerResult(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, Activity.RESULT_CANCELED, imageUriMock)

        verify(repositoryMock, times(0)).uploadImage(eq(imageUriMock), any())
    }

    @Test
    fun fabButtonClick() {
        presenter.fabButtonClick()

        verify(navigationControllerMock).openImagePicker()
    }

    @Test
    fun photoTap() {
        val uploadedImage = UploadedImage()
        presenter.photoTap(uploadedImage)

        verify(navigationControllerMock).openDetailView(uploadedImage)
    }

}