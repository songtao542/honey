package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(private val repository: ToolRepository, private val userRepository: UserRepository) : AccessUserUseCase(userRepository) {

    fun listOfficialMessage(token: String) = repository.listOfficialMessage(token)

}