package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
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