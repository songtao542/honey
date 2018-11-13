package com.snt.phoney.extensions

fun <T> Array<T>.toStringArray(): Array<String> {
    return Array(this.size) {
        "${get(it)}"
    }
}