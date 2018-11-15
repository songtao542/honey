package com.snt.phoney.ui.setup

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.usecase.BindPhoneUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BindPhoneViewModel @Inject constructor(private val usecase: BindPhoneUseCase) : ViewModel() {

    var verificationCodeId = MutableLiveData<String>()

    val error = MutableLiveData<String>()
    val success = MutableLiveData<String>()

    fun bindPhone(phone: String, code: String): Disposable? {
        val uuid = usecase.user?.uuid
        val token = usecase.user?.token
        if (uuid == null || token == null || verificationCodeId.value == null) {
            return null
        }
        return usecase.bindPhone(verificationCodeId.value!!, code, phone, uuid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "bindPhone==>$it")
                    if (it.code == 200) {
                        success.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }

    fun requestVerificationCode(phone: String): Disposable {
        return usecase.requestVerificationCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "requestVerificationCode success->$it")
                    if (it.code == 200) {
                        verificationCodeId.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }
}
