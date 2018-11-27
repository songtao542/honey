package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject


class GetVisitorUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun listVisitor(token: String) = userRepository.listVisitor(token)

    var user: User? = userRepository.user
}