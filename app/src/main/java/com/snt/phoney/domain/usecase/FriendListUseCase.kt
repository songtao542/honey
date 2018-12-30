package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class FriendListUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) : AccessUserUseCase(repository) {


    /**
     * Note: 阻塞当前线程
     */
    fun getCities() = locationRepository.cities

    fun listUser(
            token: String,
            latitude: String,
            longitude: String,
            type: String,
            pageIndex: Int,
            city: String,
            heightStart: String,
            heightEnd: String,
            ageStart: String,
            ageEnd: String,
            cupStart: String,
            cupEnd: String) = repository.listUser(token, latitude, longitude, type, pageIndex, city, heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)

    fun getLocation() = locationRepository.getLocation()

}