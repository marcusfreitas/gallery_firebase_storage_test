package com.example.marcusfreitas.gallerytestapp.repository.user

interface UserAuthInterface {
    fun getUserId(isTestUser: Boolean) : String
    fun getRandomId() : String
}