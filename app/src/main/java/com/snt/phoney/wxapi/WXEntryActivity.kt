package com.snt.phoney.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.snt.phoney.base.BaseOriginalActivity
import com.snt.phoney.ui.signup.WechatViewModel
import com.snt.phoney.utils.life.ViewModelProviders
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : BaseOriginalActivity(), IWXAPIEventHandler {

    private lateinit var viewModel: WechatViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory, WechatViewModel::class.java)

        try {
            if (!viewModel.handleIntent(intent, this)) {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        viewModel.error.observe(this, Observer {
            finish()
        })

        viewModel.success.observe(this, Observer {
            finish()
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (!viewModel.handleIntent(intent, this)) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!viewModel.handleIntent(intent, this)) {
            finish()
        }
    }

    override fun onReq(req: BaseReq?) {
        req?.let {

        }
    }

    override fun onResp(resp: BaseResp?) {
        resp?.let {

        }
    }
}