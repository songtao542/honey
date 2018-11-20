package com.snt.phoney.ui.signup

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.usecase.WxSigninUseCase
import com.snt.phoney.utils.data.Constants.Wechat
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//@SignupScope
class WxViewModel @Inject constructor(application: Application, private val usecase: WxSigninUseCase) : AndroidViewModel(application), IWXAPIEventHandler {

    private val wxApi: IWXAPI = WXAPIFactory.createWXAPI(application, Wechat.APP_ID, false)
    val error = MutableLiveData<String>()
    val success = MutableLiveData<String>()
    val user = MutableLiveData<WxUser>()

    private var wxState: Long = 0

    private var accessToken: WxAccessToken? = null

    init {
        accessToken = usecase.accessToken
        user.value = usecase.user
    }

    fun login() {
        if (accessToken == null || accessToken?.isSessionValid() == false) {
            authorize()
        } else {
            getUserInfo()
        }
    }

    fun resume() {
        user.value = usecase.user
    }

    fun clear() {
        usecase.user = null
    }

    private fun authorize() {
        wxApi.registerApp(Wechat.APP_ID)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        wxState = System.currentTimeMillis()
        req.state = "$wxState"
        wxApi.sendReq(req)
    }

    override fun onResp(resp: BaseResp?) {
        val openId = resp?.openId

        resp?.let {
            val gson = Gson()
            if (it.errCode == BaseResp.ErrCode.ERR_OK) {
                val code = (it as SendAuth.Resp).code
                getAccessToken(code)
            } else {
                error.value = "出错了"
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
                            usecase.accessToken = accessToken
                            getUserInfo()
                        },
                        onError = {
                            error.value = "出错了"
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
                            usecase.user = wxuser
                            user.value = wxuser
                            success.value = "success"
                        },
                        onError = {
                            error.value = "出错了"
                        }
                )
    }

    override fun onReq(req: BaseReq?) {
    }

    fun handleIntent(intent: Intent): Boolean {
        return wxApi.handleIntent(intent, this)
    }
}