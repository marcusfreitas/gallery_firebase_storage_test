package com.example.marcusfreitas.gallerytestapp.repository.provider

import com.example.marcusfreitas.gallerytestapp.repository.Repository
import com.example.marcusfreitas.gallerytestapp.repository.user.UserAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RepositoryProvider {

    companion object {
        private val mUploadKeyName = "uploads"

        private fun provideFirebaseStorageReference() =
                FirebaseStorage.getInstance().getReference(mUploadKeyName)

        private fun provideFirebaseDatabaseReference() =
                FirebaseDatabase.getInstance().getReference(mUploadKeyName)

        private fun provideUserAuth() = UserAuth()

        fun provideRepository() = Repository(provideFirebaseStorageReference(),
                provideFirebaseDatabaseReference(), provideUserAuth())
    }

}