package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class FriendListUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) {


    fun listUser(
            token: String,
            latitude: String,
            longitude: String,
            type: String,
            page: String,
            city: String,
            heightStart: String,
            heightEnd: String,
            ageStart: String,
            ageEnd: String,
            cupStart: String,
            cupEnd: String) = repository.listUser(token, latitude, longitude, type, page, city, heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)

    fun getLocation() = locationRepository.getLocation()

    var user: User?
        set(value) {
            repository.user = value
        }
        get() {
            return repository.user
        }

}