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
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_visitor_list.*

/**
 * A fragment representing a list of Items.
 */
class VisitorFragment : BaseFragment() {


    lateinit var viewModel: VisitorViewModel
    lateinit var adapter: VisitorRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visitor_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VisitorViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.finish() }
        titleTextView.setText(R.string.recent_visitor)

        list.layoutManager = LinearLayoutManager(context)
        adapter = VisitorRecyclerViewAdapter(activity as CommonActivity)
        list.adapter = adapter

        viewModel.visitors.observe(this, Observer {
            adapter.data = it
        })

//        list.setLoadMoreListener {
//            load(false, it)
//        }

        load(true)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listVisitor(refresh, loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = VisitorFragment().apply {
            this.arguments = arguments
        }
    }
}
