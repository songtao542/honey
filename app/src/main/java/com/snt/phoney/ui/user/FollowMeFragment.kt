package com.snt.phoney.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.extensions.loadMore
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.extensions.snackbar
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_followme_list.*

/**
 * A fragment representing a list of Items.
 */
class FollowMeFragment : BaseFragment() {

    lateinit var viewModel: FollowMeViewModel
    lateinit var adapter: FollowMeRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followme_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FollowMeViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.finish() }
        titleTextView.setText(R.string.followme_title)

        list.layoutManager = LinearLayoutManager(context)
        adapter = FollowMeRecyclerViewAdapter(activity as CommonActivity)
        list.adapter = adapter


        viewModel.follower.observe(this, Observer {
            adapter.data = it
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true, list.loadMore)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(refresh)
        }
        viewModel.listFollowMe(refresh, loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = FollowMeFragment().apply {
            this.arguments = arguments
        }
    }
}
