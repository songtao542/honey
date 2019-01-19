package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(val repository: UserRepository) : AccessUserUseCase(repository) {

    fun listMemberCombo(token: String) = repository.listMemberCombo(token)

}