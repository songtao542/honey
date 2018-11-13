package com.snt.phoney.repository

import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.repository.LocationRepository
import io.reactivex.Single
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val api: Api) : LocationRepository {

    override fun getCities(): Single<Response<String>> {
        return api.listCities()
    }

}