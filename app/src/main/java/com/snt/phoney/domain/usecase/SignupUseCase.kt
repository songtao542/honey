package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.repository.LocationRepository
import io.reactivex.Single
import javax.inject.Inject

class SignupUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    fun getCities(): Single<Response<List<Province>>> = locationRepository.getCities()


}