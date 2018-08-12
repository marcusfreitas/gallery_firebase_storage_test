package com.example.marcusfreitas.gallerytestapp.repository.user

import java.util.*

class UserAuth : UserAuthInterface {

    private val mTestUserId = "test"

    override fun getUserId(isTestUser: Boolean) : String {
        return if (isTestUser) {
            mTestUserId
        } else {
            UUID.randomUUID().toString()
        }
    }

    override fun getRandomId() : String = UUID.randomUUID().toString()
}