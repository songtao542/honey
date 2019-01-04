package com.snt.phoney.domain.accessor

import com.snt.phoney.domain.model.User

interface UserAccessor {
    fun getAccessToken(): String?
    fun getUser(): User?
    fun setUser(user: User?)

    fun tryUnlock(password: String): Boolean
    fun lock()
    fun isLocked(): Boolean
}