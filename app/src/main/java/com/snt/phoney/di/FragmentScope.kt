package com.snt.phoney.di

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

@Scope
@Documented
@Retention(RUNTIME)
annotation class FragmentScope