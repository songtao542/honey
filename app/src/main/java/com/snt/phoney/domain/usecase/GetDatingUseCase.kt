package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetDatingUseCase @Inject constructor(private val repository: DatingRepository,
                                           private val userRepository: UserRepository,
                                           private val locationRepository: LocationRepository) : AccessUserUseCase(userRepository) {

    fun getDatingDetail(token: String, uuid: String, latitude: Double, longitude: Double) = repository.getDatingDetail(token, uuid, latitude, longitude)

    fun getLocation() = locationRepository.getLocation()

    fun joinDating(token: String, uuid: String) = repository.joinDating(token, uuid)

}