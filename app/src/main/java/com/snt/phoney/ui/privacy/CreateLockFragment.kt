package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.addFragmentSafely
import kotlinx.android.synthetic.main.fragment_create_lock.*

class CreateLockFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateLockFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: CreateLockViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_lock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateLockViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.finish() }

        setPassword.setOnClickListener {
            addFragmentSafely(CreateLockStep1Fragment.newInstance(), "step1", true)
        }
    }

}
