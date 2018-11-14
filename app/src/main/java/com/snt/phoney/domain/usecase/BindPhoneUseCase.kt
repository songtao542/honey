package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.UserCredentialRepository
import io.reactivex.Single
import javax.inject.Inject

class BindPhoneUseCase @Inject constructor(val repository: UserCredentialRepository, val cache: CacheRepository) {

    fun bindPhone(msgId: String, code: String, phone: String, uuid: String, token: String) = repository.bindPhone(msgId, code, phone, uuid, token)

    fun requestVerificationCode(phone: String): Single<Response<String>> = repository.requestVerificationCode(phone)

    val user: User?
        get() = cache.user


}

