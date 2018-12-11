package com.snt.phoney.ui.main.square.popular

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.ui.main.square.SquareViewModel
import kotlinx.android.synthetic.main.fragment_popular_recommend_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class PopularRecommendFragment : BaseFragment() {

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
        adapter = PopularRecommendRecyclerViewAdapter(this, viewModel, disposeBag)
        list.adapter = adapter

        viewModel.popularDating.observe(this, Observer {
            adapter.data = it
        })

        viewModel.listPopularDating(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {

        @JvmStatic
        fun newInstance() = PopularRecommendFragment()
    }
}
