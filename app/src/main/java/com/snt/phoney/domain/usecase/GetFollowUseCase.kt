package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject


class GetFollowUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun listFollow(token: String) = userRepository.listFollow(token)

    var user: User? = userRepository.user
}