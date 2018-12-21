package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(val repository: UserRepository) : AccessUserUseCase(repository) {

    fun getMibiAmount(token: String) = repository.getMibiAmount(token)

    fun getMibiWallet(token: String) = repository.getMibiWallet(token)

    fun getVipInfo(token: String) = repository.getVipInfo(token)

}