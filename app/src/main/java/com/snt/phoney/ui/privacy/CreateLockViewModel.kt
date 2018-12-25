package com.snt.phoney.ui.privacy

import android.util.Log
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.PrivacyLockUseCase
import com.snt.phoney.extensions.TAG
import com.snt.phoney.extensions.disposedBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateLockViewModel @Inject constructor(private val usecase: PrivacyLockUseCase) : AppViewModel() {

    /**
     * @param password  MD5后的数字密码（32位）
     * @param privatePassword  倒叙密码后的MD5的数字密码（32位）
     */
    fun setPrivacyPassword(password: String, privatePassword: String) {
        checkPrivacyPassword(password, privatePassword)
    }

    private fun trySetPrivacyPassword(password: String, privatePassword: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.setPrivacyPassword(token, password, privatePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                success.value = context.getString(R.string.set_privacy_password_success)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.set_privacy_password_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.set_privacy_password_failed)
                        }
                )
    }

    private fun closePrivacyPassword(password: String, privatePassword: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.closePrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                Log.d(TAG, "close privacy password success")
                            } else if (it.hasMessage) {
                                Log.d(TAG, "close privacy password failed:${it.message}")
                            } else {
                                Log.d(TAG, "close privacy password failed")
                            }
                            trySetPrivacyPassword(password, privatePassword)
                        },
                        onError = {
                            Log.d(TAG, "close privacy password occur error")
                            trySetPrivacyPassword(password, privatePassword)
                        }
                ).disposedBy(disposeBag)
    }

    private fun checkPrivacyPassword(password: String, privatePassword: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.hasPrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                closePrivacyPassword(password, privatePassword)
                            } else {
                                trySetPrivacyPassword(password, privatePassword)
                            }
                        },
                        onError = {
                            trySetPrivacyPassword(password, privatePassword)
                        }
                ).disposedBy(disposeBag)
    }


}
