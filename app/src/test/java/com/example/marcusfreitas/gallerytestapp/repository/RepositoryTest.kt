package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.user.UserAuthInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var storageReferenceMock: StorageReference
    private lateinit var databaseReferenceMock: DatabaseReference
    private lateinit var userAuthMock: UserAuthInterface
    private lateinit var repository: Repository

    @Before
    fun setup() {
        storageReferenceMock = mock()
        databaseReferenceMock = mock()
        userAuthMock = mock()
        repository = Repository(storageReferenceMock, databaseReferenceMock, userAuthMock)
    }

    @Test
    fun uploadImage_isCorrect() {

        val imageUriMock = mock<Uri>()

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

        val fileStorageReferenceMock = mock<StorageReference> {
            on { putFile(imageUriMock) }.thenReturn(uploadTaskMock)
        }

        val uploadId = "132456"
        val userId = "test"

        whenever(userAuthMock.getRandomId()).thenReturn(uploadId)
        whenever(userAuthMock.getUserId(true)).thenReturn(userId)

        val userStorageReference = mock<StorageReference> {
            on { child(uploadId) }.thenReturn(fileStorageReferenceMock)
        }

        whenever(storageReferenceMock.child(userId)).thenReturn(userStorageReference)

        val uploadedDatabaseReference = mock<DatabaseReference> {
            on { key }.thenReturn("ABCDEF")
        }

        whenever(databaseReferenceMock.push()).thenReturn(uploadedDatabaseReference)


        val uploadImageListenerMock = mock<RepositoryInterface.OnUploadImage>()

        repository.uploadImage(imageUriMock, uploadImageListenerMock)

        verify(fileStorageReferenceMock, times(1)).putFile(imageUriMock)

    }

    @Test
    fun uploadImage_isInProgress() {

        val imageUriMock = mock<Uri>()

        val storageTaskMock = mock<StorageTask<UploadTask.TaskSnapshot>> {
            on { isInProgress }.thenReturn(true)
        }

        val uploadTaskMock = mock<UploadTask> { _ ->
            on { addOnSuccessListener(any()) }.thenReturn(storageTaskMock)
            on { addOnFailureListener(any()) }.thenReturn(storageTaskMock)
        }

        val uploadId = "132456"
        val userId = "test"

        whenever(userAuthMock.getRandomId()).thenReturn(uploadId)
        whenever(userAuthMock.getUserId(true)).thenReturn(userId)

        val fileStorageReferenceMock = mock<StorageReference> {
            on { putFile(imageUriMock) }.thenReturn(uploadTaskMock)
        }

        val userStorageReference = mock<StorageReference> {
            on { child(uploadId) }.thenReturn(fileStorageReferenceMock)
        }

        whenever(storageReferenceMock.child(userId)).thenReturn(userStorageReference)

        val uploadImageListenerMock = mock<RepositoryInterface.OnUploadImage>()

        repository.mUploadTask = storageTaskMock

        repository.uploadImage(imageUriMock, uploadImageListenerMock)

        verify(fileStorageReferenceMock, times(0)).putFile(any())
    }

    @Test
    fun startDataObserver() {

        val onDataChangedMock = mock<RepositoryInterface.OnDataChanged>()

        repository.startDataListener()
        repository.mOnDataChangedListener = onDataChangedMock

        verify(databaseReferenceMock, times(1)).addValueEventListener(any())
    }
}