package com.snt.phoney.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.ui.main.message.MessageViewModel
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_officialmessage_list.*

/**
 */
class OfficialMessageFragment : BaseFragment() {

    private lateinit var viewModel: MessageViewModel
    private lateinit var adapter: OfficialMessageRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_officialmessage_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.official_message_title)

        list.layoutManager = LinearLayoutManager(requireContext())
        adapter = OfficialMessageRecyclerViewAdapter()
        list.adapter = adapter

        viewModel.messages.observe(this, Observer {
            adapter.data = it
        })

        viewModel.listOfficialMessage()
    }


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle?) = OfficialMessageFragment().apply {
            this.arguments = arguments
        }
    }
}
