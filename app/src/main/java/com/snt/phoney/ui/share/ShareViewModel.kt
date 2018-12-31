package com.snt.phoney.ui.share

import android.app.Activity
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.utils.QQApi
import com.snt.phoney.utils.SHARE_TO_TIMELINE
import com.snt.phoney.utils.WechatApi
import com.snt.phoney.utils.WeiboApi
import com.tencent.tauth.IUiListener
import javax.inject.Inject

class ShareViewModel @Inject constructor() : AppViewModel() {

    private val mWechatApi by lazy { WechatApi(application) }
    private val mQQtApi by lazy { QQApi(application) }
    private val mWeiboApi by lazy { WeiboApi(application) }

    fun shareByWechat() {
        mWechatApi.shareUrl("纯蜜", "纯蜜生活更精彩", "http://www.baidu.com", SHARE_TO_TIMELINE)
    }

    fun shareByQQ(activity: Activity, listener: IUiListener? = null) {
        mQQtApi.shareUrl(activity, "纯蜜", "纯蜜生活更精彩", "http://www.baidu.com", listener)
    }

    fun shareByQzone(activity: Activity, listener: IUiListener? = null) {
        mQQtApi.shareQzoneUrl(activity, "纯蜜", "纯蜜生活更精彩", "http://www.baidu.com", listener)
    }

    fun shareByWeibo(activity: Activity, listener: IUiListener? = null) {
        mWeiboApi.share(activity, "纯蜜", "纯蜜生活更精彩", "http://www.baidu.com", "纯蜜生活更精彩")
    }

}