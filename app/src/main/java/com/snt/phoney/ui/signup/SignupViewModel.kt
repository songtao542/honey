package com.snt.phoney.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.usecase.SignupUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignupViewModel @Inject constructor(val signupUseCase: SignupUseCase) : ViewModel() {

    fun getCities(): Disposable {
        return signupUseCase.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "getCities==>$it")
                }
    }

}
