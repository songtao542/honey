package com.snt.phoney.ui.wallet

import com.snt.phoney.R
import com.snt.phoney.domain.model.*
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.domain.usecase.WalletUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import com.snt.phoney.ui.model.PayViewModel
import com.snt.phoney.utils.life.SingleLiveData
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val usecase: WalletUseCase, payUsecase: PayOrderUseCase) : PayViewModel(payUsecase) {

    val mibiWallet = SingleLiveData<MibiWallet>()
    val preWithdraw = SingleLiveData<PreWithdraw>()

    val alipaySign = SingleLiveData<String>()

    val consumeOrders by lazy { SingleLiveData<List<OrderRecord>>() }
    private val mConsumeOrders by lazy { ArrayList<OrderRecord>() }

    val rechargeOrders by lazy { SingleLiveData<List<OrderRecord>>() }
    private val mRechargeOrders by lazy { ArrayList<OrderRecord>() }

    val withdrawInfo by lazy { SingleLiveData<WithdrawInfo>() }

    private var mRechargePageIndex = 1
    private var mConsumePageIndex = 1

    fun getAccessToken(): String {
        return usecase.getAccessToken() ?: ""
    }

    fun getUserHeadIcon(): String {
        return usecase.getUser()?.avatar ?: ""
    }

    fun getMibiWallet() {
        val token = usecase.getAccessToken() ?: return
        usecase.getMibiWallet(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.success) {
                                mibiWallet.value = it.data
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    fun buyMibiWithWechat(target: String, uid: String? = null) {
        buyWithWechat(OrderType.BUY_MIBI, target, uid)
    }

    fun buyMibiWithAlipay(target: String, uid: String? = null) {
        buyWithAlipay(OrderType.BUY_MIBI, target, uid)
    }

    fun getWithdrawInfo(uuid: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.getWithdrawInfo(token, uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                withdrawInfo.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.load_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.load_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun withdraw(money: Double) {
        val token = usecase.getAccessToken() ?: return
        usecase.withdraw(token, money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                success.value = context.getString(R.string.withdraw_success)
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.withdraw_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.withdraw_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun preWithdraw() {
        val token = usecase.getAccessToken() ?: return
        usecase.preWithdraw(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                preWithdraw.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.can_not_withdraw)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.can_not_withdraw)
                        }
                ).disposedBy(disposeBag)
    }

    fun bindAlipay() {
        val token = usecase.getAccessToken() ?: return
        usecase.bindAlipay(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            @Suppress("CascadeIf")
                            if (it.success) {
                                alipaySign.value = it.data
                            } else if (it.hasMessage) {
                                error.value = it.message
                            } else {
                                error.value = context.getString(R.string.bind_alipay_failed)
                            }
                        },
                        onError = {
                            error.value = context.getString(R.string.bind_alipay_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun uploadAuthCode(authCode: String) {
        val token = usecase.getAccessToken() ?: return
        usecase.uploadAuthCode(token, authCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            //if (it.success) {
                            //    alipaySign.value = it.data
                            //} else if (it.hasMessage) {
                            //    error.value = it.message
                            //} else {
                            //    error.value = context.getString(R.string.bind_alipay_failed)
                            //}
                        },
                        onError = {
                            error.value = context.getString(R.string.bind_alipay_failed)
                        }
                ).disposedBy(disposeBag)
    }

    fun listRechargeOrder(refresh: Boolean, startTime: String? = null, endTime: String? = null, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("recharge")) {
            return
        }
        if (refresh) {
            mRechargePageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        val start = startTime ?: ""
        val end = endTime ?: ""
        usecase.listOrder(token, "0", mRechargePageIndex.toString(), start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("recharge", false)
                            if (it.success) {
                                if (refresh) {
                                    rechargeOrders.value = mRechargeOrders.empty()
                                }
                                if (it.isNotEmpty) {
                                    rechargeOrders.value = mRechargeOrders.addList(it.data)
                                    mRechargePageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading("recharge", false)
                            loadMore?.isLoadFailed = true
                        }
                ).disposedBy(disposeBag)
    }


    fun listConsumeOrder(refresh: Boolean, startTime: String? = null, endTime: String? = null, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("consume")) {
            return
        }
        if (refresh) {
            mConsumePageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        val start = startTime ?: ""
        val end = endTime ?: ""
        usecase.listOrder(token, "1", mConsumePageIndex.toString(), start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            setLoading("consume", false)
                            if (it.success) {
                                if (refresh) {
                                    consumeOrders.value = mConsumeOrders.empty()
                                }
                                if (it.isNotEmpty) {
                                    consumeOrders.value = mConsumeOrders.addList(it.data)
                                    mConsumePageIndex++
                                } else {
                                    loadMore?.isEnable = false
                                }
                            }
                        },
                        onError = {
                            setLoading("consume", false)
                            loadMore?.isLoadFailed = true
                        }
                ).disposedBy(disposeBag)
    }
}
