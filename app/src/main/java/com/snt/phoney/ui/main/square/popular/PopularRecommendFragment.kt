package com.snt.phoney.ui.main.square.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.setLoadMoreEnable
import com.snt.phoney.extensions.setLoadMoreListener
import com.snt.phoney.ui.main.PagerFragment
import com.snt.phoney.ui.main.square.SquareViewModel
import cust.widget.loadmore.LoadMoreAdapter
import kotlinx.android.synthetic.main.fragment_popular_recommend_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class PopularRecommendFragment : PagerFragment() {

    lateinit var viewModel: SquareViewModel

    lateinit var adapter: PopularRecommendRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular_recommend_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SquareViewModel::class.java)

        // Set the adapter
        list.layoutManager = LinearLayoutManager(context)
        adapter = PopularRecommendRecyclerViewAdapter(this, viewModel)
        list.adapter = adapter

        viewModel.popularSuccess.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            adapter.notifyDataSetChanged()
        })

        viewModel.popularError.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.popularDating.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            adapter.data = it
        })
        swipeRefresh.setProgressViewOffset(false, -dip(40), dip(8))
        swipeRefresh.setSlingshotDistance(dip(64))
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            load(true)
        }

        list.setLoadMoreListener {
            load(false, it)
        }

        load(true)
    }

    private fun load(refresh: Boolean, loadMore: LoadMoreAdapter.LoadMore? = null) {
        if (refresh) {
            list.setLoadMoreEnable(true)
        }
        viewModel.listPopularDating(refresh, loadMore)
    }

    companion object {

        @JvmStatic
        fun newInstance() = PopularRecommendFragment()
    }
}
