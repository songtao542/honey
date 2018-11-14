package com.snt.phoney.ui.signin

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.utils.data.Constants
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject
import java.lang.Exception

class QQViewModel(application: Application) : AndroidViewModel(application), IUiListener {

    private var mTencent: Tencent = Tencent.createInstance(Constants.Tencent.APP_ID, getApplication())

    val token = MutableLiveData<String>()
    val user = MutableLiveData<QQUser>()

    fun login(fragment: Fragment) {
        mTencent.login(fragment, "all", this)
    }

    override fun onComplete(response: Any?) {
        try {
            var json = response as? JSONObject
            json?.let {
                val openId = it.getString("openid")
                val accessToken = it.getString("access_token")
                val expiresIn = it.getString("expires_in")
                mTencent?.setAccessToken(accessToken, expiresIn)
                mTencent?.openId = openId
                token.value = accessToken
                getUserInfo()
            }
        } catch (e: Exception) {
        }
    }

    override fun onCancel() {
    }

    override fun onError(error: UiError?) {
    }

    private fun getUserInfo() {
        val userInfo = UserInfo(getApplication(), mTencent.qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(info: Any?) {
                Log.d("TTTT", "Login by qq info==>$info")
                val openId = ""
                val thirdToken = ""
                val plate = ""
                val nickName = ""
                val headPic = ""
                user.value = QQUser(openId, thirdToken, plate, nickName, headPic)
            }

            override fun onCancel() {
            }

            override fun onError(error: UiError?) {
            }
        })
    }
}

data class QQUser(val openId: String,
                  val thirdToken: String,
                  val plate: String,
                  val nickName: String,
                  val headPic: String)