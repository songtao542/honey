package com.snt.phoney.ui.signin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.utils.data.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_startup.*
import org.json.JSONObject
import java.lang.Exception
import com.tencent.connect.common.Constants.HTTP_GET
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import com.tencent.connect.UserInfo


class StartupFragment : BaseFragment() {

    private lateinit var mTencent: Tencent

    private val mQQListener = object : IUiListener {
        override fun onComplete(response: Any?) {
            Log.d("TTTT", "Login by qq response==>$response")
            try {
                var json = response as? JSONObject
                json?.let {
                    val openId = it.getString("openid")
                    val accessToken = it.getString("access_token")
                    val expiresIn = it.getString("expires_in")
                    mTencent?.setAccessToken(accessToken, expiresIn)
                    mTencent?.openId = openId
                    getQQUserInfo()
                }

            } catch (e: Exception) {

            }
        }

        override fun onCancel() {
        }

        override fun onError(error: UiError?) {
        }
    }

    lateinit var viewModel: StartupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartupViewModel::class.java)
        signin.setOnClickListener {
            activity?.addFragmentSafely(R.id.containerLayout, SigninFragment.newInstance(), "signin")
        }

        qq.setOnClickListener {
            signupByQQ()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TTTT", "nnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQListener)
    }


    @SuppressLint("ArgumentError")
    private fun signupByQQ() {
        activity?.let { activity ->
            mTencent = Tencent.createInstance(Constants.Tencent.APP_ID, activity.applicationContext)
            if (!mTencent.isSessionValid) {
                mTencent.login(this@StartupFragment!!, "all", mQQListener)
            }
        }
    }

    //Login by qq response==>{"ret":0,"openid":"F91D6B722F2C775D51FC5C1091F24BE7","access_token":"FCDDDC30D2CF2E1772B5EE1AE4EABCCB","pay_token":"264CEDD1A07199EB85E162D3EFAD4D73","expires_in":7776000,"pf":"desktop_m_qq-10000144-android-2002-","pfkey":"a40ec1dc2a21b252e9a214b375fc4c04","msg":"","login_cost":71,"query_authority_cost":382,"authority_cost":0,"expires_time":1549965878475}
    //Login by qq info==>{"ret":0,"msg":"","is_lost":0,"nickname":"涛哥","gender":"男","province":"广东","city":"深圳","year":"1990","constellation":"","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/30","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/50","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/100","figureurl_qq_1":"http:\/\/thirdqq.qlogo.cn\/qqapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/40","figureurl_qq_2":"http:\/\/thirdqq.qlogo.cn\/qqapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/100","is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}


    private fun getQQUserInfo() {
        var userInfo = UserInfo(activity, mTencent.qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(info: Any?) {
                Log.d("TTTT", "Login by qq info==>$info")

                val openId = ""
                val thirdToken = ""
                val plate = ""
                val nickName = ""
                val headPic = ""

                viewModel.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic)
            }

            override fun onCancel() {
            }

            override fun onError(error: UiError?) {
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = StartupFragment()
    }
}
