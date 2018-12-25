package com.snt.phoney.ui.main.square

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.usecase.SquareUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SquareViewModel @Inject constructor(private val usecase: SquareUseCase) : AppViewModel() {

    private var mRecommendPageIndex: Int = 1
    private var mPopularPageIndex: Int = 1

    private val mRecommendDating = ArrayList<Dating>()
    private val mPopularDating = ArrayList<Dating>()

    val recommendDating = MutableLiveData<List<Dating>>()
    val popularDating = MutableLiveData<List<Dating>>()

    /**
     * 推荐约会
     */
    fun listRecommendDating(refresh: Boolean, dateType: String, distanceType: String, program: String, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("recommend")) {
            return
        }
        if (refresh) {
            mRecommendPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.location
                .flatMap {
                    usecase.listRecommendDating(token, mRecommendPageIndex, dateType, distanceType, program, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (it.success) {
                                setLoading("recommend", false)
                                if (refresh) {
                                    recommendDating.value = mRecommendDating.empty()
                                }
                                if (it.isNotEmpty) {
                                    recommendDating.value = mRecommendDating.addList(it.data)
                                    mRecommendPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading("recommend", false)
                        },
                        onComplete = {
                            setLoading("recommend", false)
                        }
                ).disposedBy(disposeBag)
    }

    /**
     * 热门约会
     */
    fun listPopularDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("popular")) {
            return
        }
        if (refresh) {
            mPopularPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listPopularDating(token, mPopularPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                setLoading("popular", false)
                                if (refresh) {
                                    popularDating.value = mPopularDating.empty()
                                }
                                if (!it.isEmpty) {
                                    popularDating.value = mPopularDating.addList(it.data)
                                    mPopularPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading("popular", false)
                        }
                ).disposedBy(disposeBag)
    }


    fun joinDating(uuid: String): Single<Response<String>>? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.joinDating(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun follow(uuid: String): Single<Response<Boolean>>? {
        val token = usecase.getAccessToken() ?: return null
        return usecase.follow(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}
