package com.snt.phoney.wxapi

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.usecase.WxSigninUseCase
import com.snt.phoney.utils.WechatApi
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class WXAuthViewModel @Inject constructor(private val usecase: WxSigninUseCase) : AppViewModel(), IWXAPIEventHandler {

    val success = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    private val wxApi: WechatApi by lazy { WechatApi(application) }
    private var accessToken: WxAccessToken? = null
    private var eventHandler: IWXAPIEventHandler? = null

    init {
        accessToken = usecase.getWxAccessToken()
    }

    fun authorize() {
        if (accessToken == null || accessToken?.isSessionValid() == false) {
            wxApi.authorize()
        } else {
            getUserInfo()
        }
    }

    override fun onReq(req: BaseReq?) {
        req?.let { req ->
            eventHandler?.onReq(req)
        }
    }

    override fun onResp(resp: BaseResp?) {
        resp?.let { resp ->
            eventHandler?.onResp(resp)
            //val openId = resp?.openId
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                val code = (resp as SendAuth.Resp).code
                getAccessToken(code)
            } else {
                error.value = context.getString(R.string.wechat_auth_failed)
            }
        }
    }

    private fun getAccessToken(code: String) {
        val grantType = "authorization_code"
        usecase.getAccessToken(code, grantType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            accessToken = it
                            usecase.setWxAccessToken(accessToken)
                            getUserInfo()
                        },
                        onError = {
                            error.value = context.getString(R.string.wechat_auth_failed)
                        }
                )
    }

    private fun getUserInfo() {
        usecase.getUserInfo(accessToken?.accessToken ?: "", accessToken?.openid ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { wxuser ->
                            //{"openid":"o4PN51DasTTHB1Ieds6NIC5AM5OA","nickname":"songtao","sex":1,"language":"en","city":"","province":"","country":"","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/7KaRIUuOoUrZDcmvibZNia8kDCsfPiaO2bt5NwgCZaic17hiafVDOneG5SVSE1cniaf5a1Om5ia0x4oD21LNFBMbdd28Q/132","privilege":[],"unionid":"ocGSv5_JyViQROMku0z65_AAg1x8"}
                            wxuser.accessToken = accessToken?.accessToken
                            wxuser.refreshToken = accessToken?.refreshToken
                            usecase.setWxUser(wxuser)
                            success.value = "success"
                        },
                        onError = {
                            error.value = context.getString(R.string.wechat_auth_failed)
                        }
                )
    }

    fun handleIntent(intent: Intent, eventHandler: IWXAPIEventHandler? = null): Boolean {
        this.eventHandler = eventHandler
        return wxApi.handleIntent(intent, this)
    }
}