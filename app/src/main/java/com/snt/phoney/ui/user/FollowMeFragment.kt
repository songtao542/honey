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
import kotlinx.android.synthetic.main.fragment_followme_list.*

/**
 * A fragment representing a list of Items.
 */
class FollowMeFragment : BaseFragment() {

    lateinit var viewModel: FollowMeViewModel
    lateinit var adapter: FollowMeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followme_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FollowMeViewModel::class.java)
        // Set the adapter

        list.layoutManager = LinearLayoutManager(context)
        adapter = FollowMeRecyclerViewAdapter(activity as CommonActivity)
        list.adapter = adapter

        toolbar.setNavigationOnClickListener { activity?.finish() }

        viewModel.follower.observe(this, Observer {
            adapter.data = it
        })

        viewModel.listFollow()
    }


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = FollowMeFragment().apply {
            this.arguments = arguments
        }
    }
}
