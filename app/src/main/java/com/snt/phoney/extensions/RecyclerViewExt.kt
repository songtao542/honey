package com.snt.phoney.extensions

import androidx.recyclerview.widget.RecyclerView
import cust.widget.loadmore.LoadMoreAdapter
import cust.widget.loadmore.LoadMoreAdapter.LoadMore
import cust.widget.loadmore.LoadMoreWrapper

fun RecyclerView.setLoadMoreListener(loadMoreListener: ((enabled: LoadMore) -> Unit)) { //void onLoadMore(LoadMore enabled);
    LoadMoreWrapper.with(adapter)
            //.setFooterView(...) // view or layout resource
            .setShowNoMoreEnabled(true) // enable show NoMoreView，default false
            .setListener(loadMoreListener)
            .into(this)
}

fun RecyclerView.setLoadMoreEnable(enable: Boolean) { //void onLoadMore(LoadMore enabled);
    adapter?.let {
        if (it is LoadMoreAdapter) {
            it.isLoadMoreEnable = enable
        }
    }
}

//fun RecyclerView.getLoadMore(): LoadMore? { //void onLoadMore(LoadMore enabled);
//    adapter?.let {
//        if (it is LoadMoreAdapter) {
//            return it.loadMore
//        }
//    }
//    return null
//}

val RecyclerView.loadMore: LoadMore?
    get() {
        adapter?.let {
            if (it is LoadMoreAdapter) {
                return it.loadMore
            }
        }
        return null
    }