package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject


class GetUserInfoUseCase @Inject constructor(private val userRepository: UserRepository, private val locationRepository: LocationRepository) {

    fun getUserAmountInfo(token: String) = userRepository.getUserAmountInfo(token)

    fun getUserInfo(token: String, uid: String, latitude: Double, longitude: Double) = userRepository.getUserInfo(token, uid, latitude, longitude)

    fun getLocation() = locationRepository.getLocation()

    var user: User? = userRepository.user
}