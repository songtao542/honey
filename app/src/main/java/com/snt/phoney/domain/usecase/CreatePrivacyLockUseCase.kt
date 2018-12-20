package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class CreatePrivacyLockUseCase @Inject constructor(val repository: UserRepository) : AccessUserUseCase(repository)  {

    fun setPrivacyPassword(token: String, password: String, privatePassword: String) = repository.setPrivacyPassword(token, password, privatePassword)

    fun closePrivacyPassword(token: String) = repository.closePrivacyPassword(token)

    fun hasPrivacyPassword(token: String) = repository.hasPrivacyPassword(token)

}