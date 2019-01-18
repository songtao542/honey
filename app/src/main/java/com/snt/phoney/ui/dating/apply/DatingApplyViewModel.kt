package com.snt.phoney.ui.dating.apply

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Applicant
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

class DatingApplyViewModel @Inject constructor(private val usecase: DatingUseCase, private val squareUseCase: SquareUseCase) : AppViewModel() {

    private val mApplicants by lazy { ArrayList<Applicant>() }

    val applicants = MutableLiveData<List<Applicant>>()

    val reviewSuccess = MutableLiveData<String>()

    private var mApplicantPageIndex: Int = 1

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
                            loadMore?.isLoadFailed = true
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
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
                            loadMore?.isLoadFailed = true
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
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
                ).disposedBy(disposeBag)
    }

}
