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
                    return@flatMap if (it.code == Response.SUCCESS && !TextUtils.isEmpty(it.data)) {
                        usecase.payInMibi(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        Single.create<Response<String>> { Response<String>(code = 999, message = context.getString(R.string.create_order_failed)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == Response.SUCCESS && it.data != null) {
                                success.value = context.getString(R.string.buy_vip_success)
                            } else if (!TextUtils.isEmpty(it.message)) {
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
                    return@flatMap if (it.code == Response.SUCCESS && !TextUtils.isEmpty(it.data)) {
                        usecase.alipay(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        Single.create<Response<String>> { Response<String>(code = 999, message = context.getString(R.string.create_order_failed)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == Response.SUCCESS && it.data != null) {
                                payByAli(it.data)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.buy_vip_failed)
                        }
                ).disposedBy(disposeBag)
    }

    private fun payByAli(orderInfo: String) {
        activityRef?.get()?.let { activity ->
            AlipayApi.pay(activity, orderInfo) {
                if (it == 9000) {
                    success.value = context.getString(R.string.buy_success)
                } else {
                    error.value = context.getString(R.string.buy_vip_failed)
                }
            }
        }
    }

}
