package com.snt.phoney.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.base.BaseActivity
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

class WXEntryActivity : BaseActivity(), IWXAPIEventHandler {

    private lateinit var viewModel: WXAuthViewModel

    override fun onConfigureTheme(): Int? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val contentView = FrameLayout(this)
        //contentView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //contentView.setBackgroundColor(0x00ffffff)
        //setContentView(contentView)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WXAuthViewModel::class.java)

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

        }
    }
}