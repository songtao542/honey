//package com.snt.phoney.di.module
//
//import com.snt.phoney.di.SignupScope
//import com.snt.phoney.di.ViewModelKey
//import com.snt.phoney.ui.signin.*
//import com.snt.phoney.wxapi.WXEntryActivity
//import dagger.Binds
//import dagger.Module
//import dagger.android.ContributesAndroidInjector
//import dagger.multibindings.IntoMap
//import androidx.lifecycle.ViewModel
//
//@Suppress("unused")
//@Module
//abstract class SignupModule {
//
//    //@SignupScope
//    @ContributesAndroidInjector(modules = [SignupFragmentModule::class])
//    internal abstract fun contributeSignupActivity(): SignupActivity
//
//    //@SignupScope
//    @ContributesAndroidInjector(modules = [SignupFragmentModule::class])
//    internal abstract fun contributeWXEntryActivity(): WXEntryActivity
//
//}
//
//
//@Suppress("unused")
//@Module
//abstract class SignupFragmentModule {
//
//    @SignupScope
//    @ContributesAndroidInjector(modules = [SignupViewModelModule::class])
//    abstract fun contributeStartupFragment(): StartupFragment
//
//    @SignupScope
//    @ContributesAndroidInjector(modules = [SignupViewModelModule::class])
//    abstract fun contributeSigninFragment(): SignupFragment
//
//}
//
//
//@Suppress("unused")
////@Module
//abstract class SignupViewModelModule {
//
//    //@Binds
//    //@IntoMap
//    //@SignupScope
//    //@ViewModelKey(StartupViewModel::class)
//    //abstract fun bindStartupViewModel(viewModel: StartupViewModel): ViewModel
//
//    //@Binds
//    //@IntoMap
//    //@SignupScope
//    //@ViewModelKey(WxViewModel::class)
//    //abstract fun bindWxViewModel(viewModel: WxViewModel): ViewModel
//
//    //@Binds
//    //@IntoMap
//    //@SignupScope
//    //@ViewModelKey(WeiboViewModel::class)
//    //abstract fun bindWeiboViewModel(viewModel: WeiboViewModel): ViewModel
//
//    //@Binds
//    //@IntoMap
//    //@SignupScope
//    //@ViewModelKey(QQViewModel::class)
//    //abstract fun bindQQViewModel(viewModel: QQViewModel): ViewModel
//}