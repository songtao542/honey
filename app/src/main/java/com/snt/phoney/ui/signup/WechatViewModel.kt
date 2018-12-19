package com.snt.phoney.ui.signup

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.usecase.WxSigninUseCase
import com.snt.phoney.utils.WechatApi
import com.snt.phoney.wxapi.WXAuthViewModel
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WechatViewModel @Inject constructor(private val usecase: WxSigninUseCase) : WXAuthViewModel(usecase), IWXAPIEventHandler {

    val user = MutableLiveData<WxUser>()

    init {
        user.value = usecase.user
    }

    fun login() {
        authorize()
    }

    fun resume() {
        user.value = usecase.user
    }

}