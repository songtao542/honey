package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.OrderRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class WalletUseCase @Inject constructor(private val repository: UserRepository, private val orderRepository: OrderRepository) : AccessUserUseCase(repository) {

    fun getMibiAmount(token: String) = repository.getMibiAmount(token)

    fun getMibiWallet(token: String) = repository.getMibiWallet(token)

    fun listOrder(token: String, type: String, page: String, startTime: String, endTime: String) = orderRepository.listOrder(token, type, page, startTime, endTime)

    fun preWithdraw(token: String) = orderRepository.preWithdraw(token)

    fun bindAlipay(token: String) = orderRepository.bindAlipay(token)

    fun uploadAuthCode(token: String, authCode: String) = orderRepository.uploadAuthCode(token, authCode)


    fun withdraw(token: String, money: Double) = orderRepository.withdraw(token, money)

    fun getWithdrawInfo(token: String, uuid: String) = orderRepository.getWithdrawInfo(token, uuid)

}