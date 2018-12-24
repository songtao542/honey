package com.snt.phoney.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.GetVisitorUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VisitorViewModel @Inject constructor(private val usecase: GetVisitorUseCase) : AppViewModel() {

    val user: User? = usecase.getUser()

    val visitors = MutableLiveData<List<User>>()
    private val mVisitors = ArrayList<User>()

    private var mPageIndex = 1

    fun listVisitor(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("visitor")) {
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
                            setLoading("visitor", false)
                            Log.d("TTTT", "list follow==========>$it")
                            if (it.success) {
                                if (refresh) {
                                    mVisitors.clear()
                                    visitors.value = mVisitors
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
                            setLoading("visitor", false)
                        }
                ).disposedBy(disposeBag)
    }

}
