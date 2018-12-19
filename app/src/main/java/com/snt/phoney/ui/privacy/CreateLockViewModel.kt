package com.snt.phoney.ui.privacy

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.usecase.CreatePrivacyLockUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CreateLockViewModel @Inject constructor(private val usecase: CreatePrivacyLockUseCase) : AppViewModel() {

    val success = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    val closeSuccess = MutableLiveData<String>()

    val hasPrivacyPassword = MutableLiveData<Boolean>()

    /**
     * @param password  MD5后的数字密码（32位）
     * @param privatePassword  倒叙密码后的MD5的数字密码（32位）
     */
    fun setPrivacyPassword(password: String, privatePassword: String): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.setPrivacyPassword(token, password, privatePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                success.value = ""
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = "出错了"
                        }
                )
    }

    fun closePrivacyPassword(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.closePrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                closeSuccess.value = ""
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            error.value = "出错了"
                        }
                )
    }

    fun hasPrivacyPassword(): Disposable? {
        val token = usecase.user?.token ?: return null
        return usecase.hasPrivacyPassword(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        hasPrivacyPassword.value = it.data
                    }
                }
    }


}
