package com.snt.phoney.ui.main.square

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.domain.usecase.SquareUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import com.snt.phoney.utils.life.SingleLiveData
import cust.widget.loadmore.LoadMoreAdapter
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

    val popularSuccess = SingleLiveData<String>()
    val popularError = SingleLiveData<String>()

    fun isRecommendListEmpty(): Boolean {
        return mRecommendDating.isEmpty()
    }

    val programs = object : LiveData<List<DatingProgram>>() {
        override fun onActive() {
            usecase.getAccessToken()?.let { token ->
                usecase.listDatingProgram(token, usecase.getUser()!!.uuid ?: "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onSuccess = {
                                    value = it.data
                                },
                                onError = {

                                }
                        ).disposedBy(disposeBag)
            }
        }
    }

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
                            loadMore?.isLoadFailed = true
                            error.value = context.getString(R.string.load_failed)
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
                            loadMore?.isLoadFailed = true
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }


    fun joinDating(dating: Dating, official: Boolean) {
        val token = usecase.getAccessToken() ?: return
        usecase.joinDating(token, dating.safeUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                dating.isAttend = true
                                if (official) {
                                    success.value = context.getString(R.string.join_dating_success)
                                } else {
                                    popularSuccess.value = context.getString(R.string.join_dating_success)
                                }
                            } else if (it.hasMessage) {
                                if (official) {
                                    error.value = it.message
                                } else {
                                    popularError.value = it.message
                                }
                            } else {
                                if (official) {
                                    error.value = context.getString(R.string.join_dating_failed)
                                } else {
                                    popularError.value = context.getString(R.string.join_dating_failed)
                                }
                            }
                        },
                        onError = {
                            if (official) {
                                error.value = context.getString(R.string.join_dating_failed)
                            } else {
                                popularError.value = context.getString(R.string.join_dating_failed)
                            }
                        }
                ).disposedBy(disposeBag)
    }

    fun follow(dating: Dating, official: Boolean) {
        val token = usecase.getAccessToken() ?: return
        usecase.follow(token, dating.user?.safeUuid ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                dating.isCared = it.data ?: false
                                if (official) {
                                    if (it.data == true) {
                                        success.value = context.getString(R.string.has_follow)
                                    } else {
                                        success.value = context.getString(R.string.has_canceld_follow)
                                    }
                                } else {
                                    if (it.data == true) {
                                        popularSuccess.value = context.getString(R.string.has_follow)
                                    } else {
                                        popularSuccess.value = context.getString(R.string.has_canceld_follow)
                                    }
                                }
                            } else if (it.hasMessage) {
                                if (official) {
                                    error.value = it.message
                                } else {
                                    popularError.value = it.message
                                }
                            } else {
                                if (official) {
                                    error.value = context.getString(R.string.follow_failed)
                                } else {
                                    popularError.value = context.getString(R.string.follow_failed)
                                }
                            }
                        },
                        onError = {
                            if (official) {
                                error.value = context.getString(R.string.follow_failed)
                            } else {
                                popularError.value = context.getString(R.string.follow_failed)
                            }
                        }
                ).disposedBy(disposeBag)
    }

}
