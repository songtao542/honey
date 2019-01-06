package com.snt.phoney.ui.setup

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.BindPhoneUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BindPhoneViewModel @Inject constructor(private val usecase: BindPhoneUseCase) : AppViewModel() {

    var verificationCodeId = SingleLiveData<String>()

    fun bindPhone(phone: String, code: String) {
        val uuid = usecase.getUser()?.uuid
        val token = usecase.getAccessToken()
        if (uuid == null || token == null || verificationCodeId.value == null) {
            return
        }
        usecase.bindPhone(verificationCodeId.value!!, code, phone, uuid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        success.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }.disposedBy(disposeBag)
    }

    fun requestVerificationCode(phone: String) {
        usecase.requestVerificationCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        verificationCodeId.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }.disposedBy(disposeBag)
    }
}
