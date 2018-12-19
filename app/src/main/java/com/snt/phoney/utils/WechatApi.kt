package com.snt.phoney.utils

import android.app.Application
import android.content.Intent
import com.snt.phoney.domain.model.WxPrePayResult
import com.snt.phoney.utils.data.Constants
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class WechatApi(private val application: Application) {

    private var api: IWXAPI = WXAPIFactory.createWXAPI(application, Constants.Wechat.APP_ID, false)
    var state: Long = 0

    fun pay(info: WxPrePayResult) {
        val req = PayReq()
        req.appId = info.appid
        req.partnerId = info.partnerid
        req.prepayId = info.prepayid
        req.nonceStr = info.noncestr
        req.timeStamp = info.timestamp
        req.packageValue = info.pack
        req.sign = info.sign
        req.extData = "app data" // optional
        api.sendReq(req)
    }

    fun authorize() {
        api.registerApp(Constants.Wechat.APP_ID)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        state = System.currentTimeMillis()
        req.state = "s-$state"
        api.sendReq(req)
    }

    fun handleIntent(intent: Intent, eventHandler: IWXAPIEventHandler): Boolean {
        return api.handleIntent(intent, eventHandler)
    }

}