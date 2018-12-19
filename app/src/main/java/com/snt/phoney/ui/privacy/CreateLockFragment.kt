package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.snackbar
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
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        setPassword.setOnClickListener {
            activity?.addFragmentSafely(R.id.container, CreateLockStep1Fragment.newInstance(), "step1", true)
        }

        viewModel.closeSuccess.observe(this, Observer {
            snackbar(getString(R.string.close_privacy_password_success))
            activity?.finish()
        })

        viewModel.hasPrivacyPassword.observe(this, Observer {
            if (it) {
                closePassword.visibility = View.VISIBLE
                setPassword.visibility = View.GONE
            } else {
                closePassword.visibility = View.GONE
                setPassword.visibility = View.VISIBLE
            }
        })

        closePassword.setOnClickListener {
            viewModel.closePrivacyPassword()?.disposedBy(disposeBag)
        }
    }

}
