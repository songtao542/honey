package com.snt.phoney.ui.signup

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.usecase.WeiboSigninUseCase
import com.snt.phoney.extensions.TAG
import com.snt.phoney.utils.data.Constants.Weibo
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeiboViewModel @Inject constructor(private val usecase: WeiboSigninUseCase) : AppViewModel() {

    private var ssoHandler: SsoHandler? = null
    private var accessToken: Oauth2AccessToken? = null

    val user = SingleLiveData<WeiboUser>()

    override fun initialize() {
        WbSdk.install(application, AuthInfo(application, Weibo.APP_KEY, Weibo.REDIRECT_URL, Weibo.SCOPE))
    }

    fun login(activity: Activity) {
        if (accessToken == null) {
            accessToken = usecase.getWeiboAccessToken()
        }
        if (accessToken == null) { //accessToken == null 说明没有授权过
            authorize(activity)
        } else if (!accessToken!!.isSessionValid) { //授权过但是过期了，刷新token
            refreshTokenAndGetUser(activity)
        } else { //授权未过期
            getWeiboUserInfo()
        }
    }

    private fun authorize(activity: Activity) {
        if (ssoHandler == null) {
            ssoHandler = SsoHandler(activity)
        }
        ssoHandler!!.authorize(object : WbAuthListener {
            override fun onSuccess(token: Oauth2AccessToken?) {
                if (token?.isSessionValid == true) {
                    accessToken = token
                    usecase.setWeiboAccessToken(token)
                    getWeiboUserInfo()
                }
            }

            override fun onFailure(errorMessage: WbConnectErrorMessage?) {
                Log.d(TAG, "授权失败: ${errorMessage?.errorCode}  ${errorMessage?.errorMessage}")
                error.postValue("授权失败")
            }

            override fun cancel() {
                error.postValue("取消授权")
            }
        })
    }

    private fun refreshTokenAndGetUser(activity: Activity) {
        usecase.refreshToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            accessToken = it
                            getWeiboUserInfo()
                        },
                        onError = {
                            //刷新Token失败，重新授权
                            authorize(activity)
                        }
                )
    }

    private fun getWeiboUserInfo(): Disposable? {
        if (accessToken == null) {
            return null
        }
        return usecase.getUserInfo(accessToken!!.token, accessToken!!.uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            it.token = accessToken!!.token
                            it.uid = accessToken!!.uid
                            user.value = it
                        },
                        onError = {
                            error.postValue("授权失败")
                        }
                )
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //SSO 授权回调 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        ssoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun onCleared() {
        super.onCleared()
        clearWeiboUser()
    }

    fun clearWeiboUser() {
        usecase.setWeiboUser(null)
        usecase.setWeiboAccessToken(null)
    }
}