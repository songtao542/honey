package com.snt.phoney.ui.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_nearby_list.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class NearbyFragment : BaseFragment() {

    private lateinit var viewModel: NearbyViewModel

    private lateinit var adapter: NearbyRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(NearbyViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.recommend_people)

        list.layoutManager = GridLayoutManager(context, 3)
        adapter = NearbyRecyclerViewAdapter()
        list.adapter = adapter

        viewModel.users.observe(this, Observer {
            adapter.data = it
        })

        changeBatch.setOnClickListener {
            viewModel.listRecommendUser()
        }

        viewModel.listRecommendUser()
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle?) = NearbyFragment().apply {
            this.arguments = arguments
        }
    }
}
