package com.snt.phoney.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.ProgressDialog
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.KeyEventListener
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_auth.*
import java.io.File

/**
 *
 */
class AuthFragment : BaseFragment(), KeyEventListener {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AuthFragment().apply {
            this.arguments = arguments
        }
    }

    private var type = TYPE_VIDEO

    private lateinit var viewModel: AuthViewModel

    private var progressDialog: ProgressDialog? = null

    private var authSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(Constants.Extra.TYPE, TYPE_VIDEO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthViewModel::class.java)

        toolbar.setNavigationOnClickListener { finish() }

        viewModel.randomMessage.observe(this, Observer {
            authText.text = it
            startAuthButton.isEnabled = true
        })

        viewModel.success.observe(this, Observer {
            authSuccess = true
            dismissProgress()
            successText.visibility = View.VISIBLE
            startAuthButton.setText(R.string.back)
            startAuthButton.setOnClickListener { finish() }
            snackbar(it)
        })

        viewModel.error.observe(this, Observer {
            dismissProgress()
            snackbar(it)
        })

        if (type == TYPE_VIDEO) {
            stepOneTip.setText(R.string.auth_video_step1_tip)
            stepTwoTip.setText(R.string.auth_video_step2_tip)
        }

        startAuthButton.setOnClickListener {
            if (checkAndRequestPermission(*getPermissions())) {
                startAuth()
            }
        }

        viewModel.getAuthRandomMessage(type)
    }

    private fun finish() {
        if (authSuccess) {
            val data = Intent()
            data.putExtra(Constants.Extra.DATA, true)
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        } else {
            activity?.onBackPressed()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return true
    }

    private fun startAuth() {
        if (type == TYPE_VIDEO) {
            val recordVideoFragment = CaptureVideoFragment.newInstance()
            recordVideoFragment.setOnResultListener {
                it?.let { path ->
                    startAuth(File(path))
                }
            }
            addFragmentSafely(recordVideoFragment, "video_auth", true)
        } else {
            val captureImageFragment = CaptureImageFragment.newInstance()
            captureImageFragment.setOnResultListener {
                it?.let { path ->
                    startAuth(File(path))
                }
            }
            addFragmentSafely(captureImageFragment, "image_auth", true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkAppPermission(*getPermissions())) {
            startAuth()
        } else {
            snackbar(getString(R.string.has_no_right_to_camera))
        }
    }

    private fun getPermissions(): Array<String> {
        return if (type == TYPE_VIDEO) {
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun startAuth(file: File) {
        showProgress(getString(R.string.on_going_upload))
        viewModel.auth(type, file)
    }

    private fun showProgress(tip: String) {
        progressDialog = ProgressDialog.newInstance(tip)
        progressDialog!!.isCancelable = false
        progressDialog!!.show(childFragmentManager, "progress")
    }

    private fun dismissProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }

}
