package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.ReportReason
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.repository.DatingRepository
import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class ReportUseCase @Inject constructor(private val repository: ToolRepository, private val userRepository: UserRepository) : AccessUserUseCase(userRepository) {


    fun listReportReasons() = repository.listReportReasons()

    fun report(token: String,
               reasonType: String,
               targetUid: String,
               content: String,
               type: String,
               file: File) = repository.report(token, reasonType, targetUid, content, type, file)

}