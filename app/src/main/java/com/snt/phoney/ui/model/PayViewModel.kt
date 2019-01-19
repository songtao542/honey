package com.snt.phoney.ui.model

import android.app.Activity
import android.text.TextUtils
import com.snt.phoney.R
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.utils.AlipayApi
import com.snt.phoney.wxapi.WXPayViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

open class PayViewModel constructor(private val usecase: PayOrderUseCase) : WXPayViewModel(usecase) {

    private var activityRef: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun buyWithMibi(type: OrderType, target: String, uid: String? = null) {
        val token = usecase.getAccessToken() ?: return
        usecase.createOrder(token, type.value.toString(), target, uid ?: "")
                .flatMap {
                    return@flatMap if (it.success && !TextUtils.isEmpty(it.data)) {
                        usecase.payInMibi(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        var errorMessage = context.getString(R.string.create_order_failed)
                        if (it.hasMessage) {
                            errorMessage = it.message
                        }
                        Single.create<Response<String>> { emitter -> emitter.onSuccess(Response(code = it.code, message = errorMessage)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                buySuccess.value = true
                                success.value = context.getString(R.string.buy_member_success)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.buy_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.buy_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun buyWithWechat(type: OrderType, target: String, uid: String? = null) {
        buy(type, target, uid)
    }

    fun buyWithAlipay(type: OrderType, target: String, uid: String? = null) {
        val token = usecase.getAccessToken() ?: return
        usecase.createOrder(token, type.value.toString(), target, uid ?: "")
                .flatMap {
                    return@flatMap if (it.success && !TextUtils.isEmpty(it.data)) {
                        usecase.alipay(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        var errorMessage = context.getString(R.string.create_order_failed)
                        if (it.hasMessage) {
                            errorMessage = it.message
                        }
                        Single.create<Response<String>> { emitter -> emitter.onSuccess(Response(code = it.code, message = errorMessage)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success && !TextUtils.isEmpty(it.data)) {
                                payByAlipay(it.data!!)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.buy_member_failed)
                        }
                ).disposedBy(disposeBag)
    }

    private fun payByAlipay(orderInfo: String) {
        activityRef?.get()?.let { activity ->
            AlipayApi.pay(activity, orderInfo) {
                if (it == 9000) {
                    buySuccess.value = true
                    success.value = context.getString(R.string.buy_success)
                } else {
                    error.value = context.getString(R.string.buy_member_failed)
                }
            }
        }
    }

}
