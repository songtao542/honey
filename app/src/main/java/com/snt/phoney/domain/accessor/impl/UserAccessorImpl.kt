package com.snt.phoney.domain.accessor.impl

import android.text.TextUtils
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.data.Constants
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserAccessorImpl @Inject constructor(private val cache: CacheRepository) : UserAccessor {
    private var mAccessToken: String? = null
    private var mUser: User? = null
    private var mLocked: Boolean = false

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
                //mLocked = !TextUtils.isEmpty(mUser?.privacyPassword)
                mLocked = mUser?.isPrivacyPasswordOpen == true
            }
        }
        return mUser
    }

    override fun setUser(user: User?) {
        if (user != null) {
            user.token?.let { token ->
                mAccessToken = token
            }
            //当原来没有密码，新的user 有密码之后，设置锁定状态
            //if (!TextUtils.isEmpty(user.privacyPassword) && !TextUtils.equals(mUser?.privacyPassword, user.privacyPassword)) {
            if (user.isPrivacyPasswordOpen != mUser?.isPrivacyPasswordOpen) {
                mLocked = true
            }
            mUser = user
        } else {
            mUser = null
        }
        cache.set(Constants.Cache.USER, user)
    }

    override fun tryUnlock(password: String): Boolean {
        if (mLocked) {
            if (TextUtils.equals(getUser()?.privacyPassword, password)) {
                //密码相等，则将锁定状态修改为 false
                mLocked = false
                return true
            }
            return false
        }
        //没有锁定时，不需要解锁，直接返回 true
        return true
    }

    override fun lock() {
        //mLocked = !TextUtils.isEmpty(getUser()?.privacyPassword)
        mLocked = mUser?.isPrivacyPasswordOpen == true
    }

    override fun isLocked(): Boolean {
        return mLocked
    }

}