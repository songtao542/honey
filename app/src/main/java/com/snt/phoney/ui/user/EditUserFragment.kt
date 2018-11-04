package com.snt.phoney.ui.user

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = EditUserFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: EditUserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditUserViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.finish() }
    }

}
