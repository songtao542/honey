package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class CreateDatingUseCase @Inject constructor(private val repository: DatingRepository, private val userRepository: UserRepository) {

    fun listDatingProgram(token: String, uuid: String) = repository.listDatingProgram(token, uuid)

    val user: User?
        get() {
            return userRepository.user
        }
}