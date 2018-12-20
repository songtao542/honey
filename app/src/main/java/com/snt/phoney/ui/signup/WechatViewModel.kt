package com.snt.phoney.ui.signup

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.usecase.WxSigninUseCase
import com.snt.phoney.wxapi.WXAuthViewModel
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import javax.inject.Inject

class WechatViewModel @Inject constructor(private val usecase: WxSigninUseCase) : WXAuthViewModel(usecase), IWXAPIEventHandler {

    val user = MutableLiveData<WxUser>()

    init {
        user.value = usecase.getWxUser()
    }

    fun login() {
        authorize()
    }

    fun resume() {
        user.value = usecase.getWxUser()
    }

}