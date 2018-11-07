package com.snt.phoney.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_followme_list.*

/**
 * A fragment representing a list of Items.
 */
class FollowmeFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followme_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Set the adapter
        list?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = FollowmeRecyclerViewAdapter()
        }
        toolbar.setNavigationOnClickListener { activity?.finish() }
    }


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = FollowmeFragment().apply {
            this.arguments = arguments
        }
    }
}