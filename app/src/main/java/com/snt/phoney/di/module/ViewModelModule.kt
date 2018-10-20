package com.snt.phoney.di.module

import android.arch.lifecycle.ViewModel
import com.snt.phoney.di.ViewModelKey
import com.snt.phoney.ui.signin.SigninViewModel
import com.snt.phoney.ui.signup.SignupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SigninViewModel::class)
    abstract fun bindSigninViewModel(viewModel: SigninViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSignupViewModel(viewModel: SignupViewModel): ViewModel

}