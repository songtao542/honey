package com.snt.phoney.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.replaceFragmentSafely
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_auth_mode.*

const val CAPTURE_REQUEST_CODE = 67

const val TYPE_IMAGE = 0
const val TYPE_VIDEO = 1

/**
 */
class AuthModeFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AuthModeFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_mode, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.auth_select_mode_title)

        authByVideo.setOnClickListener {
            startAuthMode(TYPE_VIDEO)
        }

        authByImage.setOnClickListener {
            startAuthMode(TYPE_IMAGE)
        }
    }

    private fun startAuthMode(type: Int) {
        addFragmentSafely(AuthFragment.newInstance(Bundle().apply {
            putInt(Constants.Extra.TYPE, type)
        }), "auth_mode", true, enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
    }


}
