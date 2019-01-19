package com.snt.phoney.ui.privacy

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.ProgressDialog
import com.snt.phoney.extensions.*
import com.snt.phoney.ui.auth.CaptureVideoFragment
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_privacy_forget_lock_password.*
import java.io.File

/**
 */
class ForgetLockFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = ForgetLockFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: ForgetPasswordViewModel

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_forget_lock_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.reset_lock_title)

        //toolbar.navigationIcon = null

        viewModel.state.observe(this, Observer {
            loading.visibility = View.GONE
            resetLayout.visibility = View.VISIBLE
            setState(it)
        })

        viewModel.success.observe(this, Observer {
            dismissProgress()
            reviewing()
        })

        viewModel.error.observe(this, Observer {
            dismissProgress()
            snackbar(it)
        })

        viewModel.getResetPasswordState()

        resetStateIcon.setOnClickListener { viewModel.cancelResetPassword() }
    }


    private fun setState(state: Int) {
        //0--可以重置， 1--重置中(审核中), 2---认证成功（待重置）， 3--认证失败（点击重新认证）
        when (state) {
            0 -> noneReset()
            1 -> reviewing()
            2 -> reviewSuccess()
            3 -> reviewFailed()
        }
    }

    private fun reviewing() {
        resetStateIcon.setImageResource(R.drawable.ic_reset_reviewing)
        resetStateText.setText(R.string.reset_password_reviewing_tip)
        resetStateText.visibility = View.VISIBLE
        resetTip.visibility = View.INVISIBLE
        resetButton.setText(R.string.wait_continue)
        resetButton.setOnClickListener { activity?.onBackPressed() }
    }

    private fun reviewSuccess() {
        resetStateIcon.setImageResource(R.drawable.ic_reset_pass_review)
        resetStateText.setText(R.string.reset_password_review_success_tip)
        resetStateText.visibility = View.VISIBLE
        resetTip.visibility = View.INVISIBLE
        resetButton.setText(R.string.start_reset_password)
        resetButton.setOnClickListener {
            replaceFragmentSafely(CreateLockStep1Fragment.newInstance(Bundle().apply {
                putInt(Constants.Extra.MODE, MODE_RESET)
            }), "reset_lock", enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
        }
    }

    private fun reviewFailed() {
        resetStateIcon.setImageResource(R.drawable.ic_reset_review_failed)
        resetStateText.setText(R.string.reset_password_review_failed_tip)
        resetStateText.visibility = View.VISIBLE
        resetTip.visibility = View.INVISIBLE
        resetButton.setText(R.string.reset_password_again)
        resetButton.setOnClickListener {
            if (checkAndRequestPermission(*getPermissions())) {
                openCapture()
            }
        }
    }

    private fun noneReset() {
        resetStateIcon.setImageResource(R.drawable.ic_reset_record_video)
        resetStateText.visibility = View.INVISIBLE
        resetTip.visibility = View.INVISIBLE
        resetButton.setText(R.string.start_capture_video)
        resetButton.setOnClickListener {
            if (checkAndRequestPermission(*getPermissions())) {
                openCapture()
            }
        }
    }

    private fun openCapture() {
        val recordVideoFragment = CaptureVideoFragment.newInstance()
        recordVideoFragment.setOnResultListener {
            it?.let { path ->
                uploadFile(path)
            }
        }
        addFragmentSafely(recordVideoFragment, "capture_video", true,
                enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
    }

    private fun uploadFile(path: String) {
        showProgress(getString(R.string.on_going_upload))
        viewModel.uploadResetPasswordFile(File(path))
    }

    private fun showProgress(tip: String) {
        progressDialog = ProgressDialog.newInstance(tip)
                .cancelable(false)
                .show(childFragmentManager)
    }

    private fun dismissProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkAppPermission(*getPermissions())) {
            openCapture()
        } else {
            snackbar(getString(R.string.has_no_right_to_camera))
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}
