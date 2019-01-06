package com.snt.phoney.ui.nearby

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetRecommendUserUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NearbyViewModel @Inject constructor(private val usecase: GetRecommendUserUseCase) : AppViewModel() {

    private val mPageOneUsers = ArrayList<User>()
    val users = MutableLiveData<List<User>>()
    private var mPageIndex: Int = 1

    fun listRecommendUser() {
        val token = usecase.getAccessToken() ?: return
        var observable: Observable<Response<List<User>>> =
                usecase.getLocation()
                        .flatMap {
                            usecase.listRecommendUser(token, it.latitude.toString(), it.longitude.toString(), mPageIndex)
                                    .toObservable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        if (it.isEmpty) {
                            mPageIndex = 1
                            users.value = mPageOneUsers
                        } else {
                            if (mPageIndex == 1) {
                                mPageOneUsers.addList(it.data)
                            }
                            users.value = it.data
                        }
                        mPageIndex++
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }.disposedBy(disposeBag)
    }


}
