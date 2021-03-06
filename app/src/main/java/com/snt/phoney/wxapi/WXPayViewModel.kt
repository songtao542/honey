package com.snt.phoney.wxapi

import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.WxPrePayResult
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.sendBroadcast
import com.snt.phoney.utils.WechatApi
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class WXPayViewModel @Inject constructor(private val usecase: PayOrderUseCase) : AppViewModel(), IWXAPIEventHandler {

    private val wechatApi: WechatApi by lazy { WechatApi(application) }
    private var eventHandler: IWXAPIEventHandler? = null

    val buySuccess = MutableLiveData<Boolean>()

    fun buy(type: OrderType, target: String, uid: String? = null) {
        val token = usecase.getAccessToken() ?: return
        usecase.createOrder(token, type.value.toString(), target, uid ?: "")
                .flatMap {
                    return@flatMap if (it.success && !TextUtils.isEmpty(it.data)) {
                        usecase.wechatPay(token, it.data!!)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        var errorMessage = context.getString(R.string.create_order_failed)
                        if (it.hasMessage) {
                            errorMessage = it.message
                        }
                        Single.create<Response<WxPrePayResult>> { emitter -> emitter.onSuccess(Response(code = it.code, message = errorMessage)) }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success && it.data != null) {
                                payByWechat(it.data)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.buy_member_failed)
                        }
                ).disposedBy(disposeBag)
    }

    private fun payByWechat(prePayInfo: WxPrePayResult) {
        wechatApi.pay(prePayInfo)
    }

    override fun onReq(req: BaseReq) {
        req?.let { req ->
            eventHandler?.onReq(req)
        }
    }

    override fun onResp(resp: BaseResp) {
        resp?.let { resp ->
            eventHandler?.onResp(resp)
            if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode == 0) {
                buySuccess.value = true
                context.sendBroadcast("buy_success")
            }
        }
    }

    fun handleIntent(intent: Intent, eventHandler: IWXAPIEventHandler? = null): Boolean {
        this.eventHandler = eventHandler
        return wechatApi.handleIntent(intent, this)
    }
}
