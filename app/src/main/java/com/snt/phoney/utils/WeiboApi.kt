package com.snt.phoney.utils

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.BitmapFactory
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.share.WbShareHandler
import com.sina.weibo.sdk.utils.Utility
import com.snt.phoney.R
import com.snt.phoney.utils.data.Constants
import android.content.pm.PackageManager
import android.content.Context
import android.widget.Toast


class WeiboApi constructor(private val application: Application) {

    private var ssoHandler: SsoHandler? = null
    private var shareHandler: WbShareHandler? = null

    init {
        WbSdk.install(application, AuthInfo(application, Constants.Weibo.APP_KEY, Constants.Weibo.REDIRECT_URL, Constants.Weibo.SCOPE))
    }

    fun authorize(activity: Activity, listener: WbAuthListener) {
        if (ssoHandler == null) {
            ssoHandler = SsoHandler(activity)
        }
        ssoHandler?.authorize(listener)
    }


    fun share(activity: Activity, title: String, description: String, url: String, text: String) {
        if (!isWeiboInstalled(activity)) {
            Toast.makeText(activity, R.string.weibo_not_install, Toast.LENGTH_SHORT).show()
            return
        }
        shareHandler = WbShareHandler(activity).apply {
            registerApp()
            setProgressColor(0xffffff)
        }

        val bitmap = BitmapFactory.decodeResource(activity.resources, com.snt.phoney.R.drawable.ic_launcher_for_share)
        val weiboMessage = WeiboMultiMessage()
        weiboMessage.mediaObject = WebpageObject().apply {
            this.identify = Utility.generateGUID()
            this.title = title
            this.description = description
            this.setThumbImage(bitmap)
            this.actionUrl = url
            this.defaultText = text
        }
        weiboMessage.textObject = TextObject().apply {
            this.text = text
            this.title = title
            this.actionUrl = url
        }
        weiboMessage.imageObject = ImageObject().apply {
            this.setImageObject(bitmap)
        }
        shareHandler?.shareMessage(weiboMessage, true)
    }


    private fun isWeiboInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        val installedList = packageManager.getInstalledPackages(0)
        installedList?.let { installedList ->
            for (app in installedList) {
                val pname = app.packageName
                if (pname == "com.sina.weibo") {
                    return true
                }
            }
        }
        return false
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //SSO 授权回调 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        ssoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }

}