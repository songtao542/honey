package com.snt.phoney.ui.main.square

import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.usecase.SquareUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SquareViewModel @Inject constructor(private val usecase: SquareUseCase) : AppViewModel() {

    private var recommendPageIndex: Int = 0
    private var popularPageIndex: Int = 0

    val recommendDating = MutableLiveData<List<Dating>>()
    val popularDating = MutableLiveData<List<Dating>>()

    /**
     * 推荐约会
     */
    fun listRecommendDating(refresh: Boolean, dateType: Int, distanceType: Int, program: String): Disposable? {
        if (refresh) {
            recommendPageIndex = 0
        }
        val token = usecase.getAccessToken() ?: return null
        return usecase.location
                .flatMap {
                    usecase.listRecommendDating(token, recommendPageIndex, dateType, distanceType, program, it.latitude, it.longitude)
                            .toObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .singleOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                recommendPageIndex++
                                recommendDating.value = it.data
                            }
                        },
                        onError = {

                        }
                )
    }

    /**
     * 热门约会
     */
    fun listPopularDating(refresh: Boolean): Disposable? {
        if (refresh) {
            popularPageIndex = 0
        }
        val token = usecase.getAccessToken() ?: return null
        return usecase.listPopularDating(token, popularPageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                popularPageIndex++
                                popularDating.value = it.data
                            }
                        },
                        onError = {

                        }
                )
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
