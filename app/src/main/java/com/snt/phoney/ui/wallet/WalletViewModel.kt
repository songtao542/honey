package com.snt.phoney.ui.wallet

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.domain.model.MibiWallet
import com.snt.phoney.domain.model.OrderRecord
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.VipInfo
import com.snt.phoney.domain.usecase.PayOrderUseCase
import com.snt.phoney.domain.usecase.WalletUseCase
import com.snt.phoney.extensions.addList
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.empty
import com.snt.phoney.ui.model.PayViewModel
import cust.widget.loadmore.LoadMoreAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val usecase: WalletUseCase, private val payUsecase: PayOrderUseCase) : PayViewModel(payUsecase) {

    val vipInfo = MutableLiveData<VipInfo>()
    val mibiAmount = MutableLiveData<Int>()
    val mibiWallet = MutableLiveData<MibiWallet>()

    val consumeOrders by lazy { MutableLiveData<List<OrderRecord>>() }
    private val mConsumeOrders by lazy { ArrayList<OrderRecord>() }

    val rechargeOrders by lazy { MutableLiveData<List<OrderRecord>>() }
    private val mRechargeOrders by lazy { ArrayList<OrderRecord>() }

    private var mRechargePageIndex = 1
    private var mConsumePageIndex = 1

    fun getMibiAmount() {
        val token = usecase.getAccessToken() ?: return
        usecase.getMibiAmount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        mibiAmount.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun getMibiWallet() {
        val token = usecase.getAccessToken() ?: return
        usecase.getMibiWallet(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        mibiWallet.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun getVipInfo() {
        val token = usecase.getAccessToken() ?: return
        usecase.getVipInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.success) {
                        vipInfo.value = it.data
                    }
                }.disposedBy(disposeBag)
    }


    fun buyMibiWithWechat(target: String, uid: String? = null) {
        buyWithWechat(OrderType.BUY_MIBI, target, uid)
    }

    fun buyMibiWithAlipay(target: String, uid: String? = null) {
        buyWithAlipay(OrderType.BUY_MIBI, target, uid)
    }

    fun withdraw() {
        val token = usecase.getAccessToken() ?: return
//        payUsecase.createOrder(token, OrderType.WITHDRAW_MIBI.value.toString(), "", "")
//                .flatMap {
//                    return@flatMap if (it.success && !TextUtils.isEmpty(it.data)) {
//                        usecase.alipay(token, it.data!!)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                    } else {
//                        var errorMessage = context.getString(R.string.create_order_failed)
//                        if (it.hasMessage) {
//                            errorMessage = it.message
//                        }
//                        Single.create<Response<String>> { emitter -> emitter.onSuccess(Response(code = it.code, message = errorMessage)) }
//                    }
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeBy(
//                        onSuccess = {
//                            if (it.success && !TextUtils.isEmpty(it.data)) {
//                                payByAli(it.data!!)
//                            } else if (it.hasMessage) {
//                                error.value = it.message
//                            }
//                        },
//                        onError = {
//                            error.value = context.getString(R.string.buy_vip_failed)
//                        }
//                ).disposedBy(disposeBag)
    }


    fun listRechargeOrder(refresh: Boolean, startTime: Long? = null, endTime: Long? = null, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("recharge")) {
            return
        }
        if (refresh) {
            mRechargePageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        val start = startTime?.toString() ?: ""
        val end = endTime?.toString() ?: ""
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
                        }
                ).disposedBy(disposeBag)
    }


    fun listConsumeOrder(refresh: Boolean, startTime: Long? = null, endTime: Long? = null, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (isLoading("consume")) {
            return
        }
        if (refresh) {
            mConsumePageIndex = 1
        }
        val token = usecase.getAccessToken() ?: return
        val start = startTime?.toString() ?: ""
        val end = endTime?.toString() ?: ""
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
                        }
                ).disposedBy(disposeBag)
    }
}
