package com.snt.phoney.ui.signin

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.usecase.WeiboSigninUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class WeiboViewModel(application: Application, private val weiboUseCase: WeiboSigninUseCase) : AndroidViewModel(application) {

    private var ssoHandler: SsoHandler? = null
    private var accessToken: Oauth2AccessToken? = null

    val weiboUser = MutableLiveData<WeiboUser>()
    val error = MutableLiveData<String>()

    fun login(activity: Activity) {
        if (accessToken == null) {
            accessToken = weiboUseCase.accessToken
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
                    weiboUseCase.accessToken = token
                    getWeiboUserInfo()
                }
            }

            override fun onFailure(errorMessage: WbConnectErrorMessage?) {
                error.postValue("授权失败")
            }

            override fun cancel() {
                error.postValue("取消授权")
            }
        })
    }

    private fun refreshTokenAndGetUser(activity: Activity) {
        weiboUseCase.refreshToken()
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
        return weiboUseCase.getUserInfo(accessToken!!.token, accessToken!!.uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            it.token = accessToken!!.token
                            it.uid = accessToken!!.uid
                            weiboUser.value = it
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
}