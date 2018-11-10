package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_lock_step1.*

class CreateLockStep2Fragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = CreateLockStep2Fragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateLockViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_lock_step1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateLockViewModel::class.java)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        toolbar.setTitle(R.string.input_password_again_title)
        confirmAgain.setText(R.string.confirm)
    }

}
