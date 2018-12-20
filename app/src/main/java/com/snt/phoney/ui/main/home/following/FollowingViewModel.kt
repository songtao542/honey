package com.snt.phoney.ui.main.home.following

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.FollowListUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FollowingViewModel @Inject constructor(private val usecase: FollowListUseCase) : AppViewModel() {

    val users = MutableLiveData<List<User>>()
    val error = MutableLiveData<String>()
    var pageIndex: Int = 0

    fun listFollow( ): Disposable? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.listFollow(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "listFollow==>$it")
                    if (it.code == 200) {
                        users.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


    fun follow(uuid: String): Single<Response<Boolean>>? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.follow(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}
