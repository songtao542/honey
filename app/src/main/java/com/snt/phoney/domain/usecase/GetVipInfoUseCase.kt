package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.OrderRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class GetVipInfoUseCase @Inject constructor(val repository: UserRepository, val orderRepository: OrderRepository) {

    fun listVipCombo(token: String) = repository.listVipCombo(token)

    fun getMibiAmount(token: String) = repository.getMibiAmount(token)

    fun getMibiWallet(token: String) = repository.getMibiWallet(token)

    fun getVipInfo(token: String) = repository.getVipInfo(token)

    var user: User?
        set(value) {
            repository.user = value
        }
        get() {
            return repository.user
        }
}