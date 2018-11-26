package com.snt.phoney.ui.main.home.following

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
        adapter = FollowingRecyclerViewAdapter(viewModel, disposeBag)
        list.adapter = adapter

        viewModel.users.observe(this, Observer {
            adapter.data = it
        })

        viewModel.listFollow()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FollowingFragment()
    }
}
