package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.LocationRepository
import javax.inject.Inject


class GetLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    fun getLocation() = locationRepository.getLocation()
}