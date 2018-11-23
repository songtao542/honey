package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class BindPhoneUseCase @Inject constructor(val repository: UserRepository) {

    fun bindPhone(msgId: String, code: String, phone: String, uuid: String, token: String) = repository.bindPhone(msgId, code, phone, uuid, token)

    fun requestVerificationCode(phone: String) = repository.requestVerificationCode(phone)

    val user: User?
        get() = repository.user

}

