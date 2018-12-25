package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.JMessageRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class LoginJMessageUseCase @Inject constructor(private val jMessageRepository: JMessageRepository, private val userRepository: UserRepository) : AccessUserUseCase(userRepository) {

    fun login(username: String,
              password: String,
              callback: ((responseCode: Int, responseMessage: String) -> Unit)? = null) = jMessageRepository.login(username, password, callback)

}