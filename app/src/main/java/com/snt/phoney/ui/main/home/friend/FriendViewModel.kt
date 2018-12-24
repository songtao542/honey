package com.snt.phoney.ui.main.home.friend

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.FriendListUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class FriendViewModel @Inject constructor(private val usecase: FriendListUseCase) : AppViewModel() {

    private val mUsers = ArrayList<User>()
    val users = MutableLiveData<List<User>>()
    private var mPageIndex: Int = 1

    fun listUser(refresh: Boolean,
                 type: FilterType,
                 city: String,
                 heightStart: String,
                 heightEnd: String,
                 ageStart: String,
                 ageEnd: String,
                 cupStart: String,
                 cupEnd: String,
                 loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("user")) {
            return
        }
        if (refresh) {
            mPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        var latitude = ""
        var longitude = ""
        var observable: Observable<Response<List<User>>> =
                if (type == FilterType.NEAREST) {
                    usecase.getLocation()
                            .flatMap {
                                usecase.listUser(token, it.latitude.toString(), it.longitude.toString(),
                                        type.ordinal.toString(), mPageIndex, city,
                                        heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
                                        .toObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                            }
                } else {
                    usecase.listUser(token, latitude, longitude,
                            type.ordinal.toString(), mPageIndex, city,
                            heightStart, heightEnd, ageStart, ageEnd, cupStart, cupEnd)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.d("TTTT", "getCities= eeeeeeeeeeeeeeeeeeeee =>success=${it.success}  empty=${it.isEmpty}")
                            setLoading("user", false)
                            if (it.success) {
                                if (refresh) {
                                    mUsers.clear()
                                }
                                if (it.isNotEmpty) {
                                    users.value = mUsers.addList(it.data)
                                    mPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }

                        },
                        onError = {
                            setLoading("user", false)
                        }
                ).disposedBy(disposeBag)
    }


}
