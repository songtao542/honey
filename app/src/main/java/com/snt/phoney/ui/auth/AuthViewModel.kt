package com.snt.phoney.ui.auth

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.AuthUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val usecase: AuthUseCase) : AppViewModel() {

    val randomMessage = MutableLiveData<String>()

    /**
     * 0、随机数 1、随机一段话
     */
    fun getAuthRandomMessage(type: Int) {
        val token = usecase.getAccessToken() ?: return
        usecase.getAuthRandomMessage(token, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                randomMessage.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.get_random_message_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.get_random_message_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun auth(type: Int, file: File) {
        val token = usecase.getAccessToken() ?: return
        usecase.auth(token, type, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                if (!TextUtils.isEmpty(it.data)) {
                                    success.value = it.data
                                } else {
                                    success.value = context.getString(R.string.auth_wait_review)
                                }
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.auth_upload_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.auth_upload_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
