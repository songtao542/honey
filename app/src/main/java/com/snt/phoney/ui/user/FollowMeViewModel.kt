package com.snt.phoney.ui.user

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

class FollowMeViewModel @Inject constructor(private val usecase: FollowUseCase) : AppViewModel() {

    private val mFollower = ArrayList<User>()
    val follower = MutableLiveData<List<User>>()
    private var mPageIndex = 1

    fun listFollowMe(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("follow")) {
            return
        }
        if (refresh) {
            mPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listFollowMe(token, mPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("follow", false)
                            if (it.success) {
                                if (refresh) {
                                    mFollower.clear()
                                }
                                if (it.isNotEmpty) {
                                    follower.value = mFollower.addList(it.data)
                                    mPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading("follow", false)
                            loadMore?.isLoadFailed = true
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

}
