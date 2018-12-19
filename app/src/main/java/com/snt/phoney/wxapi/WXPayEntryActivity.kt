package com.snt.phoney.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.snt.phoney.base.BaseOriginalActivity
import com.snt.phoney.utils.life.ViewModelProviders
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


class WXPayEntryActivity : BaseOriginalActivity(), IWXAPIEventHandler {

    private lateinit var viewModel: WXPayViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory, WXPayViewModel::class.java)

        Log.d("TTTT", "WXPayEntryActivity---->onCreate")

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
        Log.d("TTTT", "WXPayEntryActivity---->onNewIntent")
        if (!viewModel.handleIntent(intent, this)) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TTTT", "WXPayEntryActivity---->onActivityResult")
        if (!viewModel.handleIntent(intent, this)) {
            finish()
        }
    }

    override fun onReq(req: BaseReq?) {
        Log.d("TTTT", "WXPayEntryActivity---->onReq")
        req?.let {

        }
    }

    override fun onResp(resp: BaseResp?) {
        Log.d("TTTT", "WXPayEntryActivity---->onResp")
        resp?.let {
            Log.d("TTTT", "onPayFinish, errCode = " + resp.errCode)
        }
        finish()
    }
}