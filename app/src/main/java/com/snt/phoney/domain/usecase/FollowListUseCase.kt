package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class FollowListUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) {


    fun listFollow(token: String) = repository.listFollow(token)

    fun follow(token: String, uuid: String) = repository.follow(token, uuid)

    var user: User?
        set(value) {
            repository.user = value
        }
        get() {
            return repository.user
        }

}