package com.snt.phoney.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

@SuppressLint("Registered")
public class ComponentActivity extends Activity implements LifecycleOwner, ViewModelStoreOwner {

    static final class NonConfigurationInstances {
        ViewModelStore viewModelStore;
    }

    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private ViewModelStore mViewModelStore;

    @SuppressLint("ObsoleteSdkInt")
    public ComponentActivity() {
        Lifecycle lifecycle = getLifecycle();
        if (null == lifecycle) {
            throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's "
                    + "constructor. Please make sure you are lazily constructing your Lifecycle "
                    + "in the first call to getLifecycle() rather than relying on field "
                    + "initialization.");
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getLifecycle().addObserver((GenericLifecycleObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_STOP) {
                    Window window = getWindow();
                    final View decor = window != null ? window.peekDecorView() : null;
                    if (decor != null) {
                        decor.cancelPendingInputEvents();
                    }
                }
            });
        }
        getLifecycle().addObserver((GenericLifecycleObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (!isChangingConfigurations()) {
                    getViewModelStore().clear();
                }
            }
        });
    }

    @Override
    @SuppressWarnings("RestrictedApi")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportFragment.injectIfNeededIn(this);
    }

    @SuppressLint("RestrictedApi")
    @CallSuper
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry) lifecycle).markState(Lifecycle.State.CREATED);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    @Nullable
    public final Object onRetainNonConfigurationInstance() {
        ViewModelStore viewModelStore = mViewModelStore;
        if (viewModelStore == null) {
            // No one called getViewModelStore(), so see if there was an existing
            // ViewModelStore from our last NonConfigurationInstance
            NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                viewModelStore = nc.viewModelStore;
            }
        }

        if (viewModelStore == null) {
            return null;
        }

        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.viewModelStore = viewModelStore;
        return nci;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (getApplication() == null) {
            throw new IllegalStateException("Your activity is not yet attached to the "
                    + "Application instance. You can't request ViewModel before onCreate call.");
        }
        if (mViewModelStore == null) {
            NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
            if (nc != null) {
                // Restore the ViewModelStore from NonConfigurationInstances
                mViewModelStore = nc.viewModelStore;
            }
            if (mViewModelStore == null) {
                mViewModelStore = new ViewModelStore();
            }
        }
        return mViewModelStore;
    }
}
