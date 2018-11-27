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
import kotlinx.android.synthetic.main.fragment_visitor_list.*

/**
 * A fragment representing a list of Items.
 */
class VisitorFragment : BaseFragment() {


    lateinit var viewModel: VisitorViewModel
    lateinit var adapter: VisitorRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visitor_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VisitorViewModel::class.java)

        list.layoutManager = LinearLayoutManager(context)
        adapter = VisitorRecyclerViewAdapter(activity as CommonActivity)
        list.adapter = adapter

        toolbar.setNavigationOnClickListener { activity?.finish() }

        viewModel.visitors.observe(this, Observer {
            adapter.data = it
        })

        viewModel.listVisitor()
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = VisitorFragment().apply {
            this.arguments = arguments
        }
    }
}
