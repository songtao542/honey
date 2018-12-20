package com.snt.phoney.domain.repository.impl

import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.PreWithdraw
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.WithdrawInfo
import com.snt.phoney.domain.model.WxPrePayResult
import com.snt.phoney.domain.repository.OrderRepository
import io.reactivex.Single
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(private val api: Api) : OrderRepository {
    override fun createOrder(token: String, type: String, target: String, uid: String): Single<Response<String>> {
        return api.createOrder(token, type, target, uid)
    }

    override fun payInMibi(token: String, orderId: String): Single<Response<String>> {
        return api.payInMibi(token, orderId)
    }

    override fun wechatPay(token: String, orderId: String): Single<Response<WxPrePayResult>> {
        return api.wechatPay(token, orderId)
    }

    override fun alipay(token: String, orderId: String): Single<Response<String>> {
        return api.alipay(token, orderId)
    }

    override fun preWithdraw(token: String): Single<Response<PreWithdraw>> {
        return api.preWithdraw(token)
    }

    override fun withdraw(token: String, money: Double): Single<Response<PreWithdraw>> {
        return api.withdraw(token, money)
    }

    override fun getWithdrawInfo(token: String, uuid: String): Single<Response<WithdrawInfo>> {
        return api.getWithdrawInfo(token, uuid)
    }

    override fun listOrder(token: String, type: String, page: String, startTime: String, endTime: String): Single<Response<String>> {
        return api.listOrder(token, type, page, startTime, endTime)
    }

}