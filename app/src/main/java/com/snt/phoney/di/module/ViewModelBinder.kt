package com.snt.phoney.di.module

import androidx.lifecycle.ViewModel
import com.snt.phoney.di.ActivityScope
import com.snt.phoney.di.ViewModelKey
import com.snt.phoney.ui.dating.create.CreateDatingViewModel
import com.snt.phoney.ui.dating.detail.DatingDetailViewModel
import com.snt.phoney.ui.dating.list.DatingListViewModel
import com.snt.phoney.ui.nearby.NearbyViewModel
import com.snt.phoney.ui.password.ForgetPasswordViewModel
import com.snt.phoney.ui.privacy.CreateLockViewModel
import com.snt.phoney.ui.report.ReportViewModel
import com.snt.phoney.ui.setup.BindPhoneViewModel
import com.snt.phoney.ui.setup.SetupWizardViewModel
import com.snt.phoney.ui.signup.QQViewModel
import com.snt.phoney.ui.signup.StartupViewModel
import com.snt.phoney.ui.signup.WeiboViewModel
import com.snt.phoney.ui.signup.WxViewModel
import com.snt.phoney.ui.user.EditUserViewModel
import com.snt.phoney.ui.user.UserInfoViewModel
import com.snt.phoney.ui.vip.VipViewModel
import com.snt.phoney.ui.wallet.WalletViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SetupWizardActivityViewModelModule {
    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SetupWizardViewModel::class)
    abstract fun bindSetupWizardViewModel(viewModel: SetupWizardViewModel): ViewModel
}

@Module
abstract class SignupActivityViewModelModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(StartupViewModel::class)
    abstract fun bindStartupViewModel(viewModel: StartupViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WxViewModel::class)
    abstract fun bindWxViewModel(viewModel: WxViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WeiboViewModel::class)
    abstract fun bindWeiboViewModel(viewModel: WeiboViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(QQViewModel::class)
    abstract fun bindQQViewModel(viewModel: QQViewModel): ViewModel

}


@Module
abstract class WXEntryActivityViewModelModule {


    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WxViewModel::class)
    abstract fun bindWxViewModel(viewModel: WxViewModel): ViewModel
}

@Module
abstract class MainActivityViewModelModule {


}

@Module
abstract class CommonActivityViewModelModule {
    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CreateDatingViewModel::class)
    abstract fun bindCreateDatingViewModel(viewModel: CreateDatingViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DatingListViewModel::class)
    abstract fun bindDatingListViewModel(viewModel: DatingListViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DatingDetailViewModel::class)
    abstract fun bindDatingDetailViewModel(viewModel: DatingDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WalletViewModel::class)
    abstract fun bindWalletViewModel(viewModel: WalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(NearbyViewModel::class)
    abstract fun bindNearbyViewModel(viewModel: NearbyViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(BindPhoneViewModel::class)
    abstract fun bindBindPhoneViewModel(viewModel: BindPhoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(VipViewModel::class)
    abstract fun bindVipViewModel(viewModel: VipViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(UserInfoViewModel::class)
    abstract fun bindUserInfoViewModel(viewModel: UserInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel::class)
    abstract fun bindForgetPasswordViewModel(viewModel: ForgetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(EditUserViewModel::class)
    abstract fun bindEditUserViewModel(viewModel: EditUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ReportViewModel::class)
    abstract fun bindReportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CreateLockViewModel::class)
    abstract fun bindCreateLockViewModel(viewModel: CreateLockViewModel): ViewModel
}