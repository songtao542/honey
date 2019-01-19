package com.snt.phoney.ui.privacy

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    val closeSuccess = MutableLiveData<String>()

    private fun updateUserPrivacyPassword(password: String?, privatePassword: String?) {
        val user = usecase.getUser()
        user?.let { user ->
            user.password = password
            user.privacyPassword = privatePassword
            usecase.setUser(user)
        }
    }

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
                            @Suppress("CascadeIf")
                            if (it.success) {
                                success.value = context.getString(R.string.set_privacy_password_success)
                                updateUserPrivacyPassword(password, privatePassword)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.set_privacy_password_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.set_privacy_password_failed)
                        }
                ).disposedBy(disposeBag)
    }

    private fun closePrivacyPassword(password: String, privatePassword: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.closePrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.code == 200) {
                                Log.d(TAG, "close privacy password success")
                                updateUserPrivacyPassword(null, null)
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

    fun closePrivacyPassword() {
        val token = usecase.getAccessToken() ?: return
        usecase.closePrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.code == 200) {
                                closeSuccess.value = context.getString(R.string.close_privacy_password_success)
                                Log.d(TAG, "close privacy password success")
                                updateUserPrivacyPassword(null, null)
                            } else if (it.hasMessage) {
                                error.value = it.message
                                Log.d(TAG, "close privacy password failed:${it.message}")
                            } else {
                                error.value = context.getString(R.string.close_privacy_password_failed)
                                Log.d(TAG, "close privacy password failed")
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.close_privacy_password_failed)
                            Log.d(TAG, "close privacy password occur error")
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

    fun hasPrivacyPassword(): Boolean {
        return !TextUtils.isEmpty(usecase.getUser()?.privacyPassword)
    }

}
