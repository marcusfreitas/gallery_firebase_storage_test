package com.example.marcusfreitas.gallerytestapp.repository.user

import java.util.*

class UserAuth {

    private val mTestUserId = "test"

    fun getUserId(isTestUser: Boolean) : String {
        return if (isTestUser) {
            mTestUserId
        } else {
            UUID.randomUUID().toString()
        }
    }

}