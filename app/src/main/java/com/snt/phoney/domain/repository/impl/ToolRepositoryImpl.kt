package com.snt.phoney.domain.repository.impl

import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.*
import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.utils.media.MultipartUtil
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Part
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToolRepositoryImpl @Inject constructor(val api: Api) : ToolRepository {
    override fun listOfficialMessage(token: String): Single<Response<List<OfficialMessage>>> {
        return api.listOfficialMessage(token)
    }

    override fun listReportReasons(): Single<Response<List<ReportReason>>> {
        return api.listReportReasons()
    }

    override fun listPurpose(token: String): Single<Response<List<Purpose>>> {
        return api.listPurpose(token)
    }

    override fun listCareer(token: String): Single<Response<List<Career>>> {
        return api.listCareer(token)
    }

    override fun report(token: String,
                        reasonType: String,
                        targetUid: String,
                        content: String,
                        type: String,
                        file: File): Single<Response<String>> {
        val cover = MultipartUtil.getMultipart("cover", file)
        return api.report(token, reasonType, targetUid, content, type, cover)
    }

}