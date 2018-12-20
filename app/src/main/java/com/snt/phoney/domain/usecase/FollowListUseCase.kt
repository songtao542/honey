package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class FollowListUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository): AccessUserUseCase(repository)  {


    fun listFollow(token: String) = repository.listFollow(token)

    fun follow(token: String, uuid: String) = repository.follow(token, uuid)



}