package com.snt.phoney.ui.dating.list

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.*
import com.snt.phoney.domain.usecase.GetDatingUseCase
import com.snt.phoney.domain.usecase.SquareUseCase
import com.snt.phoney.extensions.disposedBy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DatingViewModel @Inject constructor(private val usecase: GetDatingUseCase, private val squareUseCase: SquareUseCase) : AppViewModel() {


    val otherDatings = MutableLiveData<List<Dating>>()
    val publishDatings = MutableLiveData<List<Dating>>()
    val joinedDatings = MutableLiveData<List<Dating>>()
    val applicants = MutableLiveData<List<Applicant>>()

    val reviewSuccess = MutableLiveData<String>()
    val quitSuccess = MutableLiveData<String>()
    val followSuccess = MutableLiveData<Boolean>()

    val error = MutableLiveData<String>()

    private var datingPageIndex: Int = 1
    private var joinDatingPageIndex: Int = 1
    private var applicantPageIndex: Int = 1

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

    fun listJoinedDating() {
        val token = usecase.getAccessToken() ?: return
        usecase.listJoinedDating(token, joinDatingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                joinedDatings.value = it.data
                                joinDatingPageIndex++
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listMyDating() {
        val token = usecase.getAccessToken() ?: return
        usecase.listMyDating(token, datingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                publishDatings.value = it.data
                                datingPageIndex++
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }


    fun listDating(userId: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.listDatingByUser(token, userId, datingPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                otherDatings.value = it.data
                                datingPageIndex++
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listDatingApplicant(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.listDatingApplicant(token, uuid, applicantPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                applicants.value = it.data
                                applicantPageIndex++
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                )
    }


    fun listApplicant() {
        val token = usecase.getAccessToken() ?: return
        usecase.listApplicant(token, applicantPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                applicants.value = it.data
                                applicantPageIndex++
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
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


    fun follow(user: User) {
        val token = usecase.getAccessToken() ?: return
        squareUseCase.follow(token, user.safeUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                user.care = it.data!!
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
