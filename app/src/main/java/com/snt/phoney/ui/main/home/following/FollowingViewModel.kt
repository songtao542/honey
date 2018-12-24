package com.snt.phoney.ui.main.home.following

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.FollowUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FollowingViewModel @Inject constructor(private val usecase: FollowUseCase) : AppViewModel() {

    private val mUsers = ArrayList<User>()
    val users = MutableLiveData<List<User>>()
    private var mPageIndex: Int = 1

    fun listMyFollow(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("my_follow")) {
            return
        }
        if (refresh) {
            mPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listMyFollow(token, mPageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("my_follow", false)
                            Log.d("TTTT", "listMyFollow==>$it")
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
                            setLoading("my_follow", false)
                        }
                ).disposedBy(disposeBag)
    }

    fun follow(user: User) {
        val token = usecase.getAccessToken() ?: return
        usecase.follow(token, user.safeUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Log.d("TTTT", "listMyFollow==>$it")
                            if (it.success) {
                                success.value = context.getString(R.string.has_follow)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.follow_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.follow_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
