package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.OrderRepository
import com.snt.phoney.domain.repository.UserRepository
import javax.inject.Inject

class PayOrderUseCase @Inject constructor(val repository: UserRepository, val orderRepository: OrderRepository) {

    fun createOrder(token: String,
                    type: String,
                    target: String,
                    uid: String) = orderRepository.createOrder(token, type, target, uid)

    fun alipay(token: String, orderId: String) = orderRepository.alipay(token, orderId)

    fun wechatPay(token: String, orderId: String) = orderRepository.wechatPay(token, orderId)

    var user: User?
        set(value) {
            repository.user = value
        }
        get() {
            return repository.user
        }
}