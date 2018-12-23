package com.snt.phoney.ui.dating.applying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.ui.dating.list.ApplicantListRecyclerViewAdapter
import com.snt.phoney.ui.main.message.MessageViewModel
import kotlinx.android.synthetic.main.fragment_officialmessage_list.*

/**
 */
class ApplyingListFragment : BaseFragment() {

    private lateinit var adapter: ApplyingListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dating_applying_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = ApplyingListRecyclerViewAdapter()
        list.adapter = adapter

    }


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle?) = ApplyingListFragment().apply {
            this.arguments = arguments
        }
    }
}
