package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface
import com.example.marcusfreitas.gallerytestapp.repository.user.UserAuthInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class Repository(storageReference: StorageReference, databaseReference: DatabaseReference, userAuth: UserAuthInterface) :
        RepositoryInterface.RepositoryMethods {


    private val mStorageReference: StorageReference = storageReference
    private val mDatabaseReference: DatabaseReference = databaseReference
    private val mUserAuth: UserAuthInterface = userAuth

    private val mUploadedImageList = arrayListOf<UploadedImage>()

    var mUploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
    var mOnDataChangedListener: RepositoryInterface.OnDataChanged? = null

    override fun uploadImage(imageUri: Uri, listener: RepositoryInterface.OnUploadImage) {
        val uploadId = mUserAuth.getRandomId()
        val userStorageReference = mStorageReference.child(mUserAuth.getUserId(true))
        val fileReference = userStorageReference.child(uploadId)

        if (mUploadTask == null || mUploadTask?.isInProgress == false) {
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener { snapshot ->
                        addToDatabase(snapshot, uploadId, listener)
                    }
                    .addOnFailureListener {
                        listener.onError(it)
                    }
        }
    }

    private fun addToDatabase(snapshot: UploadTask.TaskSnapshot, uploadId: String,
                              listener: RepositoryInterface.OnUploadImage) {
        val databaseReference = mDatabaseReference.push().key ?: mUserAuth.getUserId(true)
        val reference = snapshot.metadata?.reference
        reference?.downloadUrl?.addOnSuccessListener { downloadUri ->
            mDatabaseReference.child(databaseReference).setValue(
                    UploadedImage(name = uploadId, url = downloadUri.toString(),
                            databaseReference = databaseReference))
                    .addOnSuccessListener {
                        listener.onSuccess(downloadUri.toString())
                    }
                    .addOnFailureListener {
                        listener.onError(it)
                    }
        }
    }

    override fun getUploadedImageList() = mUploadedImageList

    override fun startDataListener() {
        mDatabaseReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                mOnDataChangedListener?.cancelled(error)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mUploadedImageList.clear()

                snapshot.children.mapNotNullTo(mUploadedImageList) {
                    it.getValue<UploadedImage>(UploadedImage::class.java)
                }

                mOnDataChangedListener?.updatedData(mUploadedImageList)
            }

        })
    }

    override fun setOnDataChangedListener(listener: RepositoryInterface.OnDataChanged) {
        mOnDataChangedListener = listener
    }

    override fun deleteImage(uploadedImage: UploadedImageInterface): Task<Void>? {
        val userStorageReference = mStorageReference.child(mUserAuth.getUserId(true))
        val fileReference = userStorageReference.child(uploadedImage.name)

        mDatabaseReference.child(uploadedImage.databaseReference).removeValue()

        return fileReference.delete()
    }

}