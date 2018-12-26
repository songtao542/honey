package com.snt.phoney.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.snt.phoney.utils.data.Constants
import com.zhihu.matisse.internal.entity.CaptureStrategy
import com.zhihu.matisse.internal.utils.MediaStoreCompat
import kotlinx.android.synthetic.main.fragment_auth.*
import java.io.File

/**
 *
 */
class AuthFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AuthFragment().apply {
            this.arguments = arguments
        }
    }

    private var type = TYPE_VIDEO

    private var mMediaStoreCompat: MediaStoreCompat? = null

    private lateinit var viewModel: AuthViewModel

    private var progressDialog: ProgressDialog? = null

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

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        viewModel.randomMessage.observe(this, Observer {
            authText.text = it
            startAuthButton.isEnabled = true
        })

        viewModel.success.observe(this, Observer {
            dismissProgress()
            successText.visibility = View.VISIBLE
            startAuthButton.setText(R.string.back)
            startAuthButton.setOnClickListener { activity?.finish() }
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
            if (checkAndRequestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                startAuth()
            }
        }

        viewModel.getAuthRandomMessage(type)
    }

    private fun startAuth() {
        if (type == TYPE_VIDEO) {
            addFragmentSafely(VideoAuthFragment.newInstance(), "video_auth", true)
        } else {
            context?.let { context ->
                if (mMediaStoreCompat == null) {
                    mMediaStoreCompat = MediaStoreCompat(activity, this)
                    mMediaStoreCompat?.setCaptureStrategy(CaptureStrategy(true, "com.snt.phoney.fileprovider"))
                }
                mMediaStoreCompat?.dispatchCaptureIntent(context, CAPTURE_REQUEST_CODE)
                return@let
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkAppPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startAuth()
        } else {
            snackbar(getString(R.string.has_no_right_to_camera))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handCaptureImage(requestCode, resultCode, data)
    }

    private fun handCaptureImage(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val path = mMediaStoreCompat?.currentPhotoPath
            if (!TextUtils.isEmpty(path)) {
                showProgress(getString(R.string.on_going_upload))
                viewModel.auth(type, File(path))
            }
        }
    }

    private fun showProgress(tip: String) {
        progressDialog = ProgressDialog.newInstance(tip)
        progressDialog!!.show(childFragmentManager, "progress")
    }

    private fun dismissProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }

}
