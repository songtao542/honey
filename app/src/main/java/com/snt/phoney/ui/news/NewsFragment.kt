package com.snt.phoney.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.loadMore
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.extensions.snackbar
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_news.*


/**
 *
 */
class NewsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var viewModel: NewsViewModel

    private lateinit var adapter: NewsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.news_title)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsRecyclerViewAdapter(this)
        list.adapter = adapter

        viewModel.newsList.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            adapter.data = it
        })

        viewModel.error.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            snackbar(it)
        })
        swipeRefresh.setProgressViewOffset(false, -dip(40), dip(8))
        swipeRefresh.setSlingshotDistance(dip(64))
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            viewModel.listNews(true, list.loadMore)
        }

        list.setLoadMoreListener {
            viewModel.listNews(false, it)
        }

        viewModel.listNews(true, list.loadMore)
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = NewsFragment().apply {
            this.arguments = arguments
        }
    }
}
