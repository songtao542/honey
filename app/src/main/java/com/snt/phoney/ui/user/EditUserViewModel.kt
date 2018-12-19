package com.snt.phoney.ui.user

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.CityPickerConverter
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.EditUserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditUserViewModel @Inject constructor(private val usecase: EditUserUseCase) : AppViewModel() {

    val user: User? = usecase.user

    private var isSetting: Boolean = false
    var success = MutableLiveData<String>()
    var error = MutableLiveData<String>()

    val cities = object : LiveData<List<com.zaaach.citypicker.model.City>>() {
        override fun onActive() {
            GlobalScope.launch(Dispatchers.Default) {
                postValue(CityPickerConverter.convert(usecase.getCities()))
            }
        }
    }

    fun setFullUserInfo(userArg: User): Disposable? {
        if (isSetting) {
            return null
        }
        isSetting = true
        val token = usecase.user?.token ?: return null
        return usecase.setFullUserInfo(token, userArg.height, userArg.weight, userArg.age, userArg.safeCup, userArg.cityCodesString, userArg.safeIntroduce, userArg.safeCareer, "", userArg.safeWechatAccount, userArg.safeNickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            isSetting = false
                            if (it.code == 200) {
                                val user = user?.copy(height = userArg.height, weight = userArg.weight, age = userArg.age,
                                        cup = userArg.cup, cities = userArg.cities, introduce = userArg.introduce, career = userArg.career,
                                        wechatAccount = userArg.wechatAccount, nickname = userArg.nickname)
                                if (userArg != null) {
                                    usecase.user = user
                                }
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
