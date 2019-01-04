package com.snt.phoney.ui.main.home.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.loadMore
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.extensions.snackbar
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_following_list.*

/**
 * A fragment representing a list of Items.
 */
class FollowingFragment : BaseFragment() {


    lateinit var viewModel: FollowingViewModel
    lateinit var adapter: FollowingRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FollowingViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = FollowingRecyclerViewAdapter(viewModel)
        list.adapter = adapter

        viewModel.users.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            adapter.data = it
        })

        viewModel.success.observe(this, Observer {
            adapter.notifyDataSetChanged()
            snackbar(it)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            load(true, list.loadMore)
        }

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true, list.loadMore)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listMyFollow(refresh, loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FollowingFragment()
    }
}
