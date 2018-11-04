package com.snt.phoney.extensions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A lazy property that gets cleaned up when the fragment is destroyed.
 *
 * Accessing this variable in a destroyed fragment will throw NPE.
 */
class AutoClearedValue<T : Any>(val lifecycle: Lifecycle, private var value: T? = null) : ReadWriteProperty<LifecycleOwner, T> {

    init {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                value = null
            }
        })
    }

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        return value ?: throw IllegalStateException(
                "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        this.value = value
    }
}

/**
 * Creates an [AutoClearedValue] associated with this LifecycleOwner.
 */
fun <T : Any> LifecycleOwner.autoCleared() = AutoClearedValue<T>(this.lifecycle)

/**
 * Creates an [AutoClearedValue] associated with this LifecycleOwner.
 */
fun <T : Any> LifecycleOwner.autoCleared(value: T) = AutoClearedValue(this.lifecycle, value)
