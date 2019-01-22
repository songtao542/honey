package com.snt.phoney.ui.signup

import com.snt.phoney.R
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.usecase.WxSigninUseCase
import com.snt.phoney.utils.life.SingleLiveData
import com.snt.phoney.wxapi.WXAuthViewModel
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import javax.inject.Inject

class WechatViewModel @Inject constructor(private val usecase: WxSigninUseCase) : WXAuthViewModel(usecase), IWXAPIEventHandler {

    val user = SingleLiveData<WxUser>()

    init {
        user.value = usecase.getWxUser()
    }

    fun login() {
        authorize()
    }

    fun resume() {
        val wxUser = usecase.getWxUser()
        if (wxUser != null) {
            user.value = wxUser
        } else {
            error.postValue(context.getString(R.string.auth_failed))
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearWxUser()
    }

    fun clearWxUser() {
        usecase.setWxUser(null)
        usecase.setWxAccessToken(null)
    }

}