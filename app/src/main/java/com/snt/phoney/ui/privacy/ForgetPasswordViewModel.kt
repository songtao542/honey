package com.snt.phoney.ui.privacy

import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.ResetPrivacyLockUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class ForgetPasswordViewModel @Inject constructor(private val usecase: ResetPrivacyLockUseCase) : LockViewModel(usecase)  {

    val state = SingleLiveData<Int>()

    fun getResetPasswordState() {
        val token = usecase.getAccessToken() ?: return
        usecase.getResetPasswordState(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                state.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.get_reset_state_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.get_reset_state_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun uploadResetPasswordFile(file: File) {
        val token = usecase.getAccessToken() ?: return
        usecase.uploadResetPasswordFile(token, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                success.value = context.getString(R.string.upload_success)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.upload_reset_file_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.upload_reset_file_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun cancelResetPassword() {
        val token = usecase.getAccessToken() ?: return
        usecase.cancelResetPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                        },
                        onError = {
                            error.value = context.getString(R.string.cancel_reset_password_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun resetPassword(password: String, privatePassword: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.resetPassword(token, password, privatePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                success.value = context.getString(R.string.reset_password_success)
                                updateUserPrivacyPassword(password, privatePassword)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.reset_password_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.reset_password_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
