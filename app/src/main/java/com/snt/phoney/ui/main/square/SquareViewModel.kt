package com.snt.phoney.ui.main.square

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.usecase.SquareUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
    fun listRecommendDating(refresh: Boolean, dateType: Int, distanceType: Int, program: String, loadMore: LoadMoreAdapter.LoadMore? = null): Disposable? {
        if (refresh) {
            mRecommendPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return null
        return usecase.location
                .flatMap {
                    usecase.listRecommendDating(token, mRecommendPageIndex, dateType, distanceType, program, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .singleOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                if (refresh) {
                                    mRecommendDating.clear()
                                }
                                if (!it.isEmpty) {
                                    recommendDating.value = mRecommendDating.addList(it.data)
                                    mRecommendPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {

                        }
                )
    }

    /**
     * 热门约会
     */
    fun listPopularDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
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
                                if (refresh) {
                                    mPopularDating.clear()
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
