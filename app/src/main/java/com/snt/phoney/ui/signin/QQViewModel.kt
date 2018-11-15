package com.snt.phoney.ui.signin

import android.app.Activity
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
    var openId: String = ""
    val user = MutableLiveData<QQUser>()

    fun login(activity: Activity) {
        mTencent.login(activity, "all", this)
    }

    override fun onComplete(response: Any?) {
        try {
            var json = response as? JSONObject
            json?.let {
                openId = it.getString("openid")
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
                var json = info as? JSONObject
                json?.let {
                    Log.d("TTTT", "Login by qq info==>$info")
                    val thirdToken = token.value ?: ""
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
                  val headPic: String,
                  val sex: Int,
                  val province: String,
                  val city: String,
                  val year: Int)