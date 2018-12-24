package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetRecommendUserUseCase @Inject constructor(private val repository: UserRepository, private val locationRepository: LocationRepository) : AccessUserUseCase(repository) {


    fun listRecommendUser(token: String,
                          latitude: String,
                          longitude: String,
                          pageIndex: Int) = repository.listRecommendUser(token, latitude, longitude, pageIndex)

    fun getLocation() = locationRepository.getLocation()

}