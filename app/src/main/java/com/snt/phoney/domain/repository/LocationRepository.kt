package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import io.reactivex.Single

interface LocationRepository {
    fun getCities():Single<Response<List<Province>>>
}