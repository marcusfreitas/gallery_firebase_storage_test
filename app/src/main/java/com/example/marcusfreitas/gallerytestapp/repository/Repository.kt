package com.example.marcusfreitas.gallerytestapp.repository

import android.net.Uri
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.user.UserAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class Repository(storageReference: StorageReference, databaseReference: DatabaseReference, userAuth: UserAuth) :
        RepositoryInterface.RepositoryMethods {

    private val mStorageReference: StorageReference = storageReference
    private val mDatabaseReference: DatabaseReference = databaseReference
    private val mUserAuth: UserAuth = userAuth

    private val mUploadedImageList = arrayListOf<UploadedImage>()

    var mUploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
    var mOnDataChangedListener: RepositoryInterface.OnDataChanged? = null

    override fun uploadImage(imageUri: Uri, listener: RepositoryInterface.OnUploadImage) {
        val fileReference = mStorageReference.child(imageUri.lastPathSegment)

        if (mUploadTask == null || mUploadTask?.isInProgress == false) {
            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener {
                        val uploadId = mDatabaseReference.push().key ?: mUserAuth.getUserId(true)
                        val reference = it.metadata?.reference
                        if (reference != null) {
                            mDatabaseReference.child(uploadId).setValue(
                                    UploadedImage(name = imageUri.lastPathSegment,
                                            url = reference.downloadUrl.toString()))
                            listener.onSuccess(reference.downloadUrl.toString())
                        }
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

}