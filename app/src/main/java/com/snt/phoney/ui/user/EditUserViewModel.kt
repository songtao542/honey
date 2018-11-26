package com.snt.phoney.ui.user

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.City
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.EditUserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditUserViewModel @Inject constructor(private val usecase: EditUserUseCase) : ViewModel() {

    val user: User? = usecase.user

    private var isSetting: Boolean = false
    var success = MutableLiveData<String>()
    var error = MutableLiveData<String>()

    val cities = object : LiveData<List<City>>() {
        override fun onActive() {
            postValue(usecase.getCities())
        }
    }

    fun setFullUserInfo(
            height: Int,
            weight: Double,
            age: Int,
            cup: String,
            cities: String,
            introduce: String,
            career: String,
            program: String,
            wechatAccount: String,
            nickname: String): Disposable? {
        if (isSetting) {
            return null
        }
        isSetting = true
        val token = usecase.user?.token ?: return null
        return usecase.setFullUserInfo(token, height, weight, age, cup, cities, introduce, career, program, wechatAccount, nickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            isSetting = false
                            if (it.code == 200) {
                                success.value = it.data
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {
                            isSetting = false
                            error.value = "修改失败"
                        })
    }

}
