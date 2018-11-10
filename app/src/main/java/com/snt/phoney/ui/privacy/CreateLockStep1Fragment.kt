package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import kotlinx.android.synthetic.main.fragment_create_lock_step1.*

class CreateLockStep1Fragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = CreateLockStep1Fragment().apply {
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
        confirmAgain.setOnClickListener {
            activity?.addFragmentSafely(R.id.container, CreateLockStep2Fragment.newInstance(), "step2", true)
        }
    }

}
