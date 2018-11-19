package com.snt.phoney.di.module

import androidx.lifecycle.ViewModel
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
import com.snt.phoney.ui.signup.*
import com.snt.phoney.ui.user.EditUserViewModel
import com.snt.phoney.ui.user.UserInfoViewModel
import com.snt.phoney.ui.vip.VipViewModel
import com.snt.phoney.ui.wallet.WalletViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    abstract fun bindSigninViewModel(viewModel: SignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupWizardViewModel::class)
    abstract fun bindSetupWizardViewModel(viewModel: SetupWizardViewModel): ViewModel

    @Binds
    @IntoMap
//    @SignupScope
    @ViewModelKey(StartupViewModel::class)
    abstract fun bindStartupViewModel(viewModel: StartupViewModel): ViewModel

    @Binds
    @IntoMap
//    @SignupScope
    @ViewModelKey(WxViewModel::class)
    abstract fun bindWxViewModel(viewModel: WxViewModel): ViewModel

    @Binds
    @IntoMap
//    @SignupScope
    @ViewModelKey(WeiboViewModel::class)
    abstract fun bindWeiboViewModel(viewModel: WeiboViewModel): ViewModel

    @Binds
    @IntoMap
//    @SignupScope
    @ViewModelKey(QQViewModel::class)
    abstract fun bindQQViewModel(viewModel: QQViewModel): ViewModel

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
    @ViewModelKey(DatingDetailViewModel::class)
    abstract fun bindDatingDetailViewModel(viewModel: DatingDetailViewModel): ViewModel

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

    @Binds
    @IntoMap
    @ViewModelKey(VipViewModel::class)
    abstract fun bindVipViewModel(viewModel: VipViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserInfoViewModel::class)
    abstract fun bindUserInfoViewModel(viewModel: UserInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgetPasswordViewModel::class)
    abstract fun bindForgetPasswordViewModel(viewModel: ForgetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditUserViewModel::class)
    abstract fun bindEditUserViewModel(viewModel: EditUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    abstract fun bindReportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateLockViewModel::class)
    abstract fun bindCreateLockViewModel(viewModel: CreateLockViewModel): ViewModel


}