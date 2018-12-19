package com.snt.phoney.ui.main.home.friend

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.FriendListUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FriendViewModel @Inject constructor(private val usecase: FriendListUseCase) : AppViewModel() {

    val users = MutableLiveData<List<User>>()
    val error = MutableLiveData<String>()
    var pageIndex: Int = 0

    fun listUser(
            type: FilterType,
            city: String,
            heightStart: String,
            heightEnd: String,
            ageStart: String,
            ageEnd: String,
            cupStart: String,
            cupEnd: String): Disposable? {
        val token = usecase.user?.token ?: return null
        var latitude = ""
        var longitude = ""
        var observable: Observable<Response<List<User>>> =
                if (type == FilterType.NEAREST) {
                    usecase.getLocation()
                            .flatMap {
                                usecase.listUser(token, it.latitude.toString(), it.longitude.toString(),
                                        type.ordinal.toString(), pageIndex.toString(), city,
                                        heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
                                        .toObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                            }
                } else {
                    usecase.listUser(token, latitude, longitude,
                            type.ordinal.toString(), pageIndex.toString(), city,
                            heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "getCities==>$it")
                    if (it.code == 200) {
                        users.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


}
