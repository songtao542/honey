package com.snt.phoney.domain.repository

import android.location.Location
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import io.reactivex.Observable
import io.reactivex.Single

interface LocationRepository {
    fun getCities(): Single<Response<List<Province>>>

    fun getLocation(): Observable<Location>

    val cities: List<City>


}