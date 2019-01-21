package com.snt.phoney.ui.privacy

import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.AccessUserUseCase

open class LockViewModel(private val usecase: AccessUserUseCase) : AppViewModel() {
    fun updateUserPrivacyPassword(password: String?, privatePassword: String?) {
        usecase.getUser()?.let { user ->
            val user = user.updatePrivacyPassword(password, privatePassword)
            usecase.setUser(user)
        }
    }
}