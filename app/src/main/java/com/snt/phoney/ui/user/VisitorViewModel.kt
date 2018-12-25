package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetVisitorUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VisitorViewModel @Inject constructor(private val usecase: GetVisitorUseCase) : AppViewModel() {

    val user: User? = usecase.getUser()

    val visitors = MutableLiveData<List<User>>()
    private val mVisitors = ArrayList<User>()

    private var mPageIndex = 1

    fun listVisitor(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading()) {
            return
        }
        if (refresh) {
            mPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listVisitor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading(false)
                            Log.d("TTTT", "list follow==========>$it")
                            if (it.success) {
                                if (refresh) {
                                    visitors.value = mVisitors.empty()
                                }
                                if (it.isNotEmpty) {
                                    visitors.value = mVisitors.addList(it.data)
                                    mPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading(false)
                        }
                ).disposedBy(disposeBag)
    }

}
