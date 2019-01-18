package com.snt.phoney.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.TAG
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


class WXPayEntryActivity : BaseActivity(), IWXAPIEventHandler {

    private lateinit var viewModel: WXPayViewModel

    override fun onApplyTheme(themeId: Int): Int = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WXPayViewModel::class.java)

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

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
            Log.d(TAG, "pay complete, errCode=${resp.errCode}")
        }
        finish()
    }
}