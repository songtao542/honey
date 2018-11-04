package com.snt.phoney.ui.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_visitor_list.*

/**
 * A fragment representing a list of Items.
 */
class VisitorFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visitor_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = FollowmeRecyclerViewAdapter()
        }
        toolbar.setNavigationOnClickListener { activity?.finish() }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = VisitorFragment().apply {
            this.arguments = arguments
        }
    }
}
