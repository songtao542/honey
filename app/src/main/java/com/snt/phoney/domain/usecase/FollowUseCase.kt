package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject


class FollowUseCase @Inject constructor(private val userRepository: UserRepository) : AccessUserUseCase(userRepository) {

    fun listMyFollow(token: String, pageIndex: Int) = userRepository.listMyFollow(token, pageIndex)

    fun listFollowMe(token: String, pageIndex: Int) = userRepository.listFollowMe(token, pageIndex)

    fun follow(token: String, uuid: String) = userRepository.follow(token, uuid)

}