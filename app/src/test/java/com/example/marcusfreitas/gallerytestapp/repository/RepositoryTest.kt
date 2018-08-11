package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.user.UserAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RepositoryTest {

    /*
        I'll let these tests as it is because I was spending too much time creating those
        and it's really hard test Firebase functions, as I don't want to delay more to
        delivery this test submission, I'll let this class test unfinished and focus more on the presenter's
        that is the class that will use this repository functions
    */

    private lateinit var storageReferenceMock: StorageReference
    private lateinit var databaserReferenceMock: DatabaseReference
    private lateinit var repository: Repository

    @Before
    fun setup() {
        storageReferenceMock = mock()
        databaserReferenceMock = mock()
        repository = Repository(storageReferenceMock, databaserReferenceMock, UserAuth())
    }

    @Test
    fun uploadImage_isCorrect() {

        val imageUriMock = mock<Uri> {
            on { lastPathSegment }.thenReturn("test.jpg")
        }

        val taskMock = mock<Task<Uri>> {
            on { result }.thenReturn(imageUriMock)
        }

        val uploadedReference = mock<StorageReference> {
            on { downloadUrl }.thenReturn(taskMock)
        }

        val metadataMock = mock<StorageMetadata> {
            on { reference }.thenReturn(uploadedReference)
        }

        val snapShotMock = mock<UploadTask.TaskSnapshot> {
            on { metadata }.thenReturn(metadataMock)
        }

        val storageTaskMock = mock<StorageTask<UploadTask.TaskSnapshot>> {
            on { snapshot }.thenReturn(snapShotMock)
        }

        val uploadTaskMock = mock<UploadTask> { _ ->
            on { addOnSuccessListener(any()) }.thenReturn(storageTaskMock)
            on { addOnFailureListener(any()) }.thenReturn(storageTaskMock)
        }

        val childStorageReferenceMock = mock<StorageReference> {
            on { putFile(imageUriMock) }.thenReturn(uploadTaskMock)
        }

        whenever(storageReferenceMock.child("test.jpg")).thenReturn(childStorageReferenceMock)

        val uploadedDatabaseReference = mock<DatabaseReference> {
            on { key }.thenReturn("123456")
        }

        whenever(databaserReferenceMock.push()).thenReturn(uploadedDatabaseReference)

        val uploadImageListenerMock = mock<RepositoryInterface.OnUploadImage>()

        repository.uploadImage(imageUriMock, uploadImageListenerMock)

        verify(childStorageReferenceMock, times(1)).putFile(imageUriMock)

    }

    @Test
    fun uploadImage_isInProgress() {

        val imageUriMock = mock<Uri> {
            on { lastPathSegment }.thenReturn("test.jpg")
        }

        val storageTaskMock = mock<StorageTask<UploadTask.TaskSnapshot>> {
            on { isInProgress }.thenReturn(true)
        }

        val uploadTaskMock = mock<UploadTask> { _ ->
            on { addOnSuccessListener(any()) }.thenReturn(storageTaskMock)
            on { addOnFailureListener(any()) }.thenReturn(storageTaskMock)
        }

        val childStorageReferenceMock = mock<StorageReference> {
            on { putFile(imageUriMock) }.thenReturn(uploadTaskMock)
        }

        whenever(storageReferenceMock.child("test.jpg")).thenReturn(childStorageReferenceMock)

        val uploadImageListenerMock = mock<RepositoryInterface.OnUploadImage>()

        repository.mUploadTask = storageTaskMock

        repository.uploadImage(imageUriMock, uploadImageListenerMock)

        verify(childStorageReferenceMock, times(0)).putFile(any())
    }

    @Test
    fun startDataListener() {

        val onDataChangedMock = mock<RepositoryInterface.OnDataChanged>()

        val uploadedImage = UploadedImage("test", "http://test.jpg")

        val dataSnapshotMock = mock<DataSnapshot> {
            on { getValue(UploadedImage::class.java) }.thenReturn(uploadedImage)
        }

        val snapshotList = listOf(dataSnapshotMock)

        val dataSnapshotListMock = mock<DataSnapshot> {
            on { children }.thenReturn(snapshotList)
        }

//        doAnswer {
//            val listener: ValueEventListener = it.getArgument<ValueEventListener>(0)
//            listener.onDataChange(dataSnapshotListMock)
//        }.whenever(databaserReferenceMock).addValueEventListener(any())

        repository.startDataListener()
        repository.mOnDataChangedListener = onDataChangedMock

        verify(databaserReferenceMock, times(1)).addValueEventListener(any())
//        verify(onDataChangedMock, times(1)).updatedData(eq(listOf(uploadedImage)))
    }
}