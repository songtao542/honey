package com.snt.phoney.repository

import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.Career
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.repository.ToolRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRepositoryImpl @Inject constructor(val api: Api) : ToolRepository {
    override fun listPurpose(token: String): Single<Response<List<Purpose>>> {
        return api.listPurpose(token)
    }

    override fun listCareer(token: String): Single<Response<List<Career>>> {
        return api.listCareer(token)
    }

}