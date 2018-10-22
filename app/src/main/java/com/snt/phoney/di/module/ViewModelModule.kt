package com.snt.phoney.di.module

import android.arch.lifecycle.ViewModel
import com.snt.phoney.di.ViewModelKey
import com.snt.phoney.ui.dating.create.CreateDatingViewModel
import com.snt.phoney.ui.dating.list.DatingListViewModel
import com.snt.phoney.ui.nearby.NearbyViewModel
import com.snt.phoney.ui.signin.SigninViewModel
import com.snt.phoney.ui.signup.BindPhoneViewModel
import com.snt.phoney.ui.signup.SignupViewModel
import com.snt.phoney.ui.wallet.WalletViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(CreateDatingViewModel::class)
    abstract fun bindCreateDatingViewModel(viewModel: CreateDatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DatingListViewModel::class)
    abstract fun bindDatingListViewModel(viewModel: DatingListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    abstract fun bindWalletViewModel(viewModel: WalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NearbyViewModel::class)
    abstract fun bindNearbyViewModel(viewModel: NearbyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BindPhoneViewModel::class)
    abstract fun bindBindPhoneViewModel(viewModel: BindPhoneViewModel): ViewModel

}