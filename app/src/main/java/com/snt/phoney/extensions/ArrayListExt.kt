package com.snt.phoney.extensions

inline fun <E> ArrayList<E>.addList(list: List<E>?): ArrayList<E> {
    list?.let {
        this.addAll(it)
    }
    return this
}

inline fun <E> ArrayList<E>.removeList(list: List<E>?): ArrayList<E> {
    list?.let {
        this.removeAll(it)
    }
    return this
}

inline fun <E> ArrayList<E>.empty(): ArrayList<E> {
    this.clear()
    return this
}