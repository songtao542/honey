package com.snt.phoney.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_startup.*


class StartupFragment : BaseFragment() {

    lateinit var viewModel: StartupViewModel
    lateinit var qqViewModel: QQViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartupViewModel::class.java)
        qqViewModel = ViewModelProviders.of(this).get(QQViewModel::class.java)

        signin.setOnClickListener {
            activity?.addFragmentSafely(R.id.containerLayout, SigninFragment.newInstance(), "signin")
        }

        qq.setOnClickListener {
            qqViewModel.login(this@StartupFragment)
        }

        qqViewModel.user.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.openId, it.thirdToken, it.plate, it.nickName, it.headPic)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TTTT", "nnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
        Tencent.onActivityResultData(requestCode, resultCode, data, qqViewModel)
    }


    //Login by qq response==>{"ret":0,"openid":"F91D6B722F2C775D51FC5C1091F24BE7","access_token":"FCDDDC30D2CF2E1772B5EE1AE4EABCCB","pay_token":"264CEDD1A07199EB85E162D3EFAD4D73","expires_in":7776000,"pf":"desktop_m_qq-10000144-android-2002-","pfkey":"a40ec1dc2a21b252e9a214b375fc4c04","msg":"","login_cost":71,"query_authority_cost":382,"authority_cost":0,"expires_time":1549965878475}
    //Login by qq info==>{"ret":0,"msg":"","is_lost":0,"nickname":"涛哥","gender":"男","province":"广东","city":"深圳","year":"1990","constellation":"","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/30","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/50","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/100","figureurl_qq_1":"http:\/\/thirdqq.qlogo.cn\/qqapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/40","figureurl_qq_2":"http:\/\/thirdqq.qlogo.cn\/qqapp\/1107903594\/F91D6B722F2C775D51FC5C1091F24BE7\/100","is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}


    companion object {
        @JvmStatic
        fun newInstance() = StartupFragment()
    }
}
