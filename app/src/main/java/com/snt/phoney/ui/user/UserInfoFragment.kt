package com.snt.phoney.ui.user

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_info.*

class UserInfoFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = UserInfoFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: UserInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserInfoViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.finish() }
    }

}
