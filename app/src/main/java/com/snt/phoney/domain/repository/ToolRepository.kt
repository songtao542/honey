package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.Career
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.model.ReportReason
import com.snt.phoney.domain.model.Response
import io.reactivex.Single

interface ToolRepository {
    fun listCareer(token: String): Single<Response<List<Career>>>
    fun listPurpose(token: String): Single<Response<List<Purpose>>>
    fun listReportReasons(token: String): Single<Response<List<ReportReason>>>
}