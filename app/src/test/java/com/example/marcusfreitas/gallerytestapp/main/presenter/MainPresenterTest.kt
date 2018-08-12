package com.example.marcusfreitas.gallerytestapp.main.presenter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import org.junit.Before

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

        verify(repositoryMock, times(1)).startDataListener()
        verify(fragmentViewMock, times(1)).setImageUrlList(eq(arrayListOf(uploadedImage)))
    }

    @Test
    fun imagePickerResult_WhenResultCodeOk() {

        val imageUriMock = mock<Uri>()

        val intentMock = mock<Intent> {
            on { data }.thenReturn(imageUriMock)
        }

        presenter.imagePickerResult(1, Activity.RESULT_OK, intentMock)

        verify(repositoryMock, times(1)).uploadImage(eq(imageUriMock), any())
    }

    @Test
    fun imagePickerResult_WhenResultCodeCanceled() {

        val imageUriMock = mock<Uri>()

        val intentMock = mock<Intent> {
            on { data }.thenReturn(imageUriMock)
        }

        presenter.imagePickerResult(1, Activity.RESULT_CANCELED, intentMock)

        verify(repositoryMock, times(0)).uploadImage(eq(imageUriMock), any())
    }

    @Test
    fun imagePickerResult_WhenIntentDataNull() {

        val imageUriMock = mock<Uri>()

        val intentMock = mock<Intent> {
            on { data }.thenReturn(null)
        }

        presenter.imagePickerResult(1, Activity.RESULT_OK, intentMock)

        verify(repositoryMock, times(0)).uploadImage(eq(imageUriMock), any())
    }

    @Test
    fun fabButtonClick() {
        presenter.fabButtonClick(1)

        verify(navigationControllerMock, times(1)).openImagePicker(eq(1))
    }





}