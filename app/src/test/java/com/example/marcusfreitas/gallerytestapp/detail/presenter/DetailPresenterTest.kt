package com.example.marcusfreitas.gallerytestapp.detail.presenter

import com.example.marcusfreitas.gallerytestapp.detail.contract.DetailContract
import com.example.marcusfreitas.gallerytestapp.repository.RepositoryInterface
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class DetailPresenterTest {

    private lateinit var repositoryMock: RepositoryInterface.RepositoryMethods
    private lateinit var presenter: DetailPresenter
    private lateinit var fragmentViewMock: DetailContract.DetailViewContract

    @Before
    fun setup() {
        repositoryMock = mock()
        fragmentViewMock = mock()

        presenter = DetailPresenter(repositoryMock)
        presenter.attachView(fragmentViewMock)

    }

    @Test
    fun loadPhoto() {

        val photoUrl = "http://test.jpg"

        whenever(fragmentViewMock.uploadedImage).thenReturn(UploadedImage(url = photoUrl))

        presenter.loadPhoto()

        verify(fragmentViewMock).showPhoto(photoUrl)
    }

    @Test
    fun loadPhotoWithError() {

        whenever(fragmentViewMock.uploadedImage).thenReturn(null)

        presenter.loadPhoto()

        verify(fragmentViewMock).showNoPhotos()
    }

    @Test
    fun deletePhoto() {

        val uploadedImageMock = mock<UploadedImageInterface>()
        whenever(uploadedImageMock.databaseReference).thenReturn("test")

        whenever(fragmentViewMock.uploadedImage).thenReturn(uploadedImageMock)

        presenter.deletePhoto()

        verify(repositoryMock).deleteImage(any())
    }

}