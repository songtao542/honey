package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserLocationUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) : AccessUserUseCase(repository) {

    fun updateUserLocation(token: String,
                           latitude: Double,
                           longitude: Double,
                           address: String,
                           city: String) = repository.updateUserLocation(token, latitude, longitude, address, city)

    fun getLocation() = locationRepository.getLocation()

}