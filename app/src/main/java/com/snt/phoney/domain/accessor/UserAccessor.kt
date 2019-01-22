package com.snt.phoney.domain.accessor

import com.snt.phoney.domain.model.MemberInfo
import com.snt.phoney.domain.model.User

interface UserAccessor {
    fun getAccessToken(): String?
    fun getUser(): User?
    fun setUser(user: User?, callback: ((old: User?) -> Unit)? = null)

    fun tryUnlock(password: String): Boolean
    fun lock()
    fun isLocked(): Boolean

    fun updatePrivacyPassword(password: String?, privacyPassword: String?)
    fun updateMemberInfo(memberInfo: MemberInfo?)
}