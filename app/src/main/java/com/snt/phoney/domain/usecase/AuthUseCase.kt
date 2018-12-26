package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: UserRepository, private val toolRepository: ToolRepository) : AccessUserUseCase(repository) {

    fun getAuthRandomMessage(token: String, type: Int) = toolRepository.getAuthRandomMessage(token, type)
    fun auth(token: String, type: Int, file: File) = toolRepository.auth(token, type, file)
    fun getAuthState(token: String) = toolRepository.getAuthState(token)

}