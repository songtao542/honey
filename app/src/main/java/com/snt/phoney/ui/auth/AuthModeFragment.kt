package com.snt.phoney.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
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


    /**
     * 0 未认证
     * 1 认证中
     * 2 认证通过
     * 3 认证未通过
     */
    private var state: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            state = it.getInt(Constants.Extra.STATE, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_mode, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        setState()
    }

    private fun setState() {
        when (state) {
            0 -> {
                titleTextView.setText(R.string.auth_select_mode_title)
                stateLayout.visibility = View.GONE
                authLayout.visibility = View.VISIBLE

                authByVideo.setOnClickListener {
                    startAuthMode(TYPE_VIDEO)
                }

                authByImage.setOnClickListener {
                    startAuthMode(TYPE_IMAGE)
                }
            }
            1 -> {
                titleTextView.setText(R.string.auth_state_title)
                stateLayout.visibility = View.VISIBLE
                authLayout.visibility = View.GONE
                stateIcon.setImageResource(R.drawable.ic_reset_reviewing)
                stateText.setText(R.string.auth_reviewing_tip)
                button.visibility = View.GONE
            }
            2 -> {
                titleTextView.setText(R.string.auth_state_title)
                stateLayout.visibility = View.VISIBLE
                authLayout.visibility = View.GONE
                stateIcon.setImageResource(R.drawable.ic_reset_pass_review)
                stateText.setText(R.string.auth_success_tip)
                button.visibility = View.GONE
            }
            3 -> {
                titleTextView.setText(R.string.auth_state_title)
                stateLayout.visibility = View.VISIBLE
                authLayout.visibility = View.GONE
                stateIcon.setImageResource(R.drawable.ic_reset_review_failed)
                stateText.setText(R.string.auth_failed_tip)
                button.setText(R.string.auth_again)
                button.setOnClickListener {
                    titleTextView.setText(R.string.auth_select_mode_title)
                    stateLayout.visibility = View.GONE
                    authLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun startAuthMode(type: Int) {
        addFragmentSafely(AuthFragment.newInstance(Bundle().apply {
            putInt(Constants.Extra.TYPE, type)
        }), "auth_mode", true, enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
    }


}
