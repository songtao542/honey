package com.snt.phoney.domain.accessor.impl

import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.data.Constants
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserAccessorImpl @Inject constructor(private val cache: CacheRepository) : UserAccessor {

    private var mAccessToken: String? = null
    private var mUser: User? = null

    override fun getAccessToken(): String? {
        if (mAccessToken == null) {
            mAccessToken = getUser()?.token
        }
        return mAccessToken
    }

    override fun getUser(): User? {
        if (mUser == null) {
            runBlocking {
                mUser = cache.get(Constants.Cache.USER)
            }
        }
        return mUser
    }

    override fun setUser(user: User?) {
        if (user != null) {
            user.token?.let { token ->
                mAccessToken = token
            }
            mUser = user
        } else {
            mUser = null
        }
        cache.set(Constants.Cache.USER, user)
    }

}