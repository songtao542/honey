package com.snt.phoney.extensions

inline fun <E> ArrayList<E>.addList(list: List<E>?): ArrayList<E> {
    list?.let {
        this.addAll(it)
    }
    return this
}