package com.snt.phoney.utils

import android.app.Application
import android.content.Intent
import android.graphics.BitmapFactory
import com.snt.phoney.R
import com.snt.phoney.domain.model.WxPrePayResult
import com.snt.phoney.utils.data.Constants
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
 * 分享到会话
 */
const val SHARE_TO_SESSION = SendMessageToWX.Req.WXSceneSession
/**
 * 分享到朋友圈
 */
const val SHARE_TO_TIMELINE = SendMessageToWX.Req.WXSceneTimeline

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


    /**
     *@param flag 0 发到微信对话，1 发到朋友圈
     */
    fun shareUrl(title: String, description: String, url: String, scene: Int = SHARE_TO_TIMELINE) {
        //flag:0代表分享到微信好友，1代表分享到朋友圈
        //初始化一个WXWebpageObject填写url
        val webPageObject = WXWebpageObject()
        webPageObject.webpageUrl = url
        //用WXWebpageObject对象初始化一个WXMediaMessage，天下标题，描述
        val msg = WXMediaMessage(webPageObject)
        msg.title = title
        msg.description = description
        //这块需要注意，图片的像素千万不要太大，不然的话会调不起来微信分享，或者直接参考下文的sharePicture
        val thumb = BitmapFactory.decodeResource(application.resources, R.mipmap.ic_launcher_for_share)
        msg.setThumbImage(thumb)
        val req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg
        req.scene = scene
        api.sendReq(req)
    }

}