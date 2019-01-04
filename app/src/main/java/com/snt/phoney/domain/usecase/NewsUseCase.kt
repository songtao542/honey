package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.ToolRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject


class NewsUseCase @Inject constructor(private val repository: ToolRepository) {

    fun listNews(page: Int) = repository.listNews(page)

}