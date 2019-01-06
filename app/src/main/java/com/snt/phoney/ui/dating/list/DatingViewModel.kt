package com.snt.phoney.ui.dating.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Applicant
import com.snt.phoney.domain.model.ApplyState
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.usecase.DatingUseCase
import com.snt.phoney.domain.usecase.SquareUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DatingViewModel @Inject constructor(private val usecase: DatingUseCase, private val squareUseCase: SquareUseCase) : AppViewModel() {


    private val mOtherDatings by lazy { ArrayList<Dating>() }
    private val mPublishDatings by lazy { ArrayList<Dating>() }
    private val mJoinedDatings by lazy { ArrayList<Dating>() }
    private val mApplicants by lazy { ArrayList<Applicant>() }

    val otherDatings = MutableLiveData<List<Dating>>()
    val publishDatings = MutableLiveData<List<Dating>>()
    val joinedDatings = MutableLiveData<List<Dating>>()
    val applicants = MutableLiveData<List<Applicant>>()

    val reviewSuccess = MutableLiveData<String>()
    val quitSuccess = MutableLiveData<String>()
    val followSuccess = MutableLiveData<Boolean>()

    private var mDatingPageIndex: Int = 1
    private var mJoinDatingPageIndex: Int = 1
    private var mApplicantPageIndex: Int = 1

    fun quitDating(dating: Dating) {
        val token = usecase.getAccessToken() ?: return
        usecase.quitDating(token, dating.safeUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                dating.applyState = ApplyState.CANCELED.value
                                quitSuccess.value = context.getString(R.string.has_canceled)
                            } else {
                                error.value = context.getString(R.string.quit_dating_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.quit_dating_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listJoinedDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("joined_dating")) {
            return
        }
        if (refresh) {
            mJoinDatingPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listJoinedDating(token, mJoinDatingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("joined_dating", false)
                            if (it.success) {
                                if (refresh) {
                                    joinedDatings.value = mJoinedDatings.empty()
                                }
                                if (it.isNotEmpty) {
                                    joinedDatings.value = mJoinedDatings.addList(it.data)
                                    mJoinDatingPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading("joined_dating", false)
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listMyDating(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("my_dating")) {
            return
        }
        if (refresh) {
            mDatingPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listMyDating(token, mDatingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("my_dating", false)
                            if (it.success) {
                                if (refresh) {
                                    publishDatings.value = mPublishDatings.empty()
                                }
                                if (it.isNotEmpty) {
                                    publishDatings.value = mPublishDatings.addList(it.data)
                                    mDatingPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading("my_dating", false)
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }


    fun listDating(refresh: Boolean, userId: String, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("user_dating")) {
            return
        }
        if (refresh) {
            mDatingPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listDatingByUser(token, userId, mDatingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("user_dating", false)
                            if (it.success) {
                                if (refresh) {
                                    otherDatings.value = mOtherDatings.empty()
                                }
                                if (it.isNotEmpty) {
                                    otherDatings.value = mOtherDatings.addList(it.data)
                                    mDatingPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading("user_dating", false)
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listDatingApplicant(refresh: Boolean, uuid: String, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("dating_applicant")) {
            return
        }
        if (refresh) {
            mApplicantPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listDatingApplicant(token, uuid, mApplicantPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("dating_applicant", false)
                            if (it.success) {
                                if (refresh) {
                                    applicants.value = mApplicants.empty()
                                }
                                if (it.isNotEmpty) {
                                    applicants.value = mApplicants.addList(it.data)
                                    mApplicantPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading("dating_applicant", false)
                            error.value = context.getString(R.string.load_failed)
                        }
                )
    }


    fun listApplicant(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("applicant")) {
            return
        }
        if (refresh) {
            mApplicantPageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        usecase.listApplicant(token, mApplicantPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("applicant", false)
                            if (it.code == 200) {
                                if (refresh) {
                                    applicants.value = mApplicants.empty()
                                }
                                if (it.isNotEmpty) {
                                    applicants.value = mApplicants.addList(it.data)
                                    mApplicantPageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            setLoading("applicant", false)
                            error.value = context.getString(R.string.load_failed)
                        }
                )
    }

    fun reviewDating(applicant: Applicant, state: Int) {
        val token = usecase.getAccessToken() ?: return
        usecase.reviewDating(token, applicant.safeUuid, state.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                when (state) {
                                    1 -> {
                                        applicant.state = 1
                                        reviewSuccess.value = context.getString(R.string.has_agree)
                                    }
                                    2 -> {
                                        applicant.state = 2
                                        reviewSuccess.value = context.getString(R.string.has_reject)
                                    }
                                }
                            } else {
                                error.value = context.getString(R.string.review_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.review_failed)
                        }
                )
    }


    fun follow(dating: Dating) {
        val token = usecase.getAccessToken() ?: return
        val userId = dating.user?.safeUuid ?: return
        squareUseCase.follow(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                dating.isCared = it.data!!
                                followSuccess.value = it.data
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
