package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.*
import io.reactivex.Single
import java.io.File

interface ToolRepository {
    fun listCareer(token: String): Single<Response<List<Career>>>
    fun listPurpose(token: String): Single<Response<List<Purpose>>>

    fun listOfficialMessage(token: String): Single<Response<List<OfficialMessage>>>

    fun listReportReasons(): Single<Response<List<ReportReason>>>

    fun report(token: String,
               reasonType: String,
               targetUid: String,
               content: String,
               type: String,
               file: File): Single<Response<String>>

    fun getAuthRandomMessage(token: String, type: Int): Single<Response<String>>

    fun auth(token: String, type: Int, file: File): Single<Response<String>>

    fun getAuthState(token: String): Single<Response<AuthState>>

    fun listNews(page: Int): Single<Response<List<News>>>
}