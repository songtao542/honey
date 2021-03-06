package com.snt.phoney.ui.signup

import android.app.Activity
import android.content.Intent
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.QQUser
import com.snt.phoney.utils.QQApi
import com.snt.phoney.utils.life.SingleLiveData
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import org.json.JSONObject
import javax.inject.Inject

class QQViewModel @Inject constructor() : AppViewModel(), IUiListener {

    private val mQQApi by lazy { QQApi(application) }

    var token: String? = null
    var openId: String = ""
    val user = SingleLiveData<QQUser>()

    fun login(activity: Activity) {
        mQQApi.login(activity, this)
    }

    override fun onComplete(response: Any?) {
        try {
            var json = response as? JSONObject
            json?.let {
                openId = it.getString("openid")
                val accessToken = it.getString("access_token")
                val expiresIn = it.getString("expires_in")
                mQQApi.setAccessToken(accessToken, expiresIn)
                mQQApi.openId = openId
                token = accessToken
                getUserInfo()
            }
        } catch (e: Exception) {
            error.postValue(context.getString(R.string.auth_failed))
        }
    }

    override fun onCancel() {
    }

    override fun onError(error: UiError?) {
    }

    private fun getUserInfo() {
        val userInfo = UserInfo(application, mQQApi.qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(info: Any?) {
                var json = info as? JSONObject
                json?.let {
                    val thirdToken = token ?: ""
                    val plate = "0" //0 qq 1 wx 3 wb
                    val nickName = it.getString("nickname")
                    val headPic = it.getString("figureurl_qq_2") ?: it.getString("figureurl_qq_1")
                    val gender = it.getString("gender")
                    val sex = when (gender) {
                        "男" -> 1
                        "女" -> 0
                        else -> -1
                    }
                    val province = it.getString("province")
                    val city = it.getString("city")
                    val year = it.getString("year").toInt()
                    user.value = QQUser(openId, thirdToken, plate, nickName, headPic, sex, province, city, year)
                }
            }

            override fun onCancel() {
                error.value = context.getString(R.string.cancel_auth)
            }

            override fun onError(e: UiError?) {
                error.postValue(context.getString(R.string.auth_failed))
            }
        })
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mQQApi.onActivityResult(requestCode, resultCode, data, this)
    }

}

