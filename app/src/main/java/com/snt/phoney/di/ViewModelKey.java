package com.snt.phoney.di;


import android.app.Activity;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.internal.Beta;

import static java.lang.annotation.ElementType.METHOD;

/**
 * {@link MapKey} annotation to key bindings by a type of an {@link Activity}.
 */
@Beta
@MapKey
@Documented
@Target(METHOD)
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}