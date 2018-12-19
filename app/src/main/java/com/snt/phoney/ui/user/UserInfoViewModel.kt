package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetUserInfoUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserInfoViewModel @Inject constructor(private val usecase: GetUserInfoUseCase) : AppViewModel() {

    val userInfo = MutableLiveData<User>()
    val error = MutableLiveData<String>()

    fun getUserInfo(uuid: String): Disposable? {
        Log.d("TTTT", "getUser ---------getUser-------- getUser->$uuid")
        val token = usecase.user?.token ?: return null
        return usecase.getLocation()
                .flatMap {
                    usecase.getUserInfo(token, uuid, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.d("TTTT", "success ---------success-------- success->$it")
                            if (it.code == 200) {
                                userInfo.value = it.data
                            }
                        },
                        onError = {
                            Log.d("TTTT", "error ---------error-------- error->$it")
                        })
    }

}
