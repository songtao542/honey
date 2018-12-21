package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetVipInfoUseCase @Inject constructor(val repository: UserRepository) : AccessUserUseCase(repository) {

    fun listVipCombo(token: String) = repository.listVipCombo(token)

}