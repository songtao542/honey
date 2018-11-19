package com.snt.phoney.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.snt.phoney.base.BaseOriginalActivity
import com.snt.phoney.ui.signup.WxViewModel
import com.snt.phoney.utils.life.ViewModelProviders
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : BaseOriginalActivity(), IWXAPIEventHandler {

    private lateinit var wxViewModel: WxViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxViewModel = ViewModelProviders.of(this, viewModelFactory, WxViewModel::class.java)

        Log.d("TTTT", "xxxxxxxx22222222222xxxxxxxxxxxwxViewMode>$wxViewModel")

        try {
            if (!wxViewModel.handleIntent(intent)) {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        wxViewModel.error.observe(this, Observer {
            finish()
        })

        wxViewModel.success.observe(this, Observer { user ->
//            startActivity(SignupActivity.newIntent(this).apply {
//                putExtra(Constants.Extra.USER, user)
//            })
            finish()
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxViewModel.handleIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        wxViewModel.handleIntent(data)
    }

    override fun onReq(baseReq: BaseReq) {
        wxViewModel.onReq(baseReq)
    }

    override fun onResp(baseResp: BaseResp) {
        wxViewModel.onResp(baseResp)
    }
}