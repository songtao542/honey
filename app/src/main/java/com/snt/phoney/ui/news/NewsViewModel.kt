package com.snt.phoney.ui.news

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.News
import com.snt.phoney.domain.usecase.NewsUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val usecase: NewsUseCase) : AppViewModel() {

    private val mNewsList = ArrayList<News>()

    val newsList = MutableLiveData<List<News>>()

    private var mPageIndex = 1

    fun listNews(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading()) {
            return
        }
        if (refresh) {
            mPageIndex = 1
        }
        usecase.listNews(mPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading(false)
                            if (it.success) {
                                if (refresh) {
                                    newsList.value = mNewsList.empty()
                                }
                                if (it.isNotEmpty) {
                                    newsList.value = mNewsList.addList(it.data)
                                    mPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading(false)
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }
}