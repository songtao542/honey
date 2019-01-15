package com.snt.phoney.ui.album

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.di.Injectable
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.extensions.startActivityForResult
import com.snt.phoney.ui.photo.PhotoFragment
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_burn.*
import kotlinx.android.synthetic.main.fragment_photo_view.*
import javax.inject.Inject

const val REQUEST_VIP_CODE = 60

/**
 */
class AlbumPhotoFragment : PhotoFragment(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance(url: String, extras: Bundle? = null) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.Extra.URL, url)
                extras?.let { putAll(extras) }
            }
        }

        @JvmStatic
        fun newInstance(uri: Uri, extras: Bundle? = null) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.URI, uri)
                extras?.let { putAll(extras) }
            }
        }

        @JvmStatic
        fun newInstance(photo: Photo, extras: Bundle? = null) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.PHOTO, photo)
                extras?.let { putAll(extras) }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AlbumViewModel

    private var viewerIsVip = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewerIsVip = it.getBoolean(Constants.Extra.IS_VIP, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel::class.java)
    }

    private fun setNotBurnedState(photo: Photo) {
        burnLayout.visibility = View.VISIBLE
        burnStateView.visibility = View.VISIBLE

        buyVipButton.visibility = View.GONE
        burnStateView.setText(R.string.press_to_view)
        if (photo.burnTime > 0) {
            progressView.visibility = View.VISIBLE
            progressView.maxProgress = photo.burnTimeInMillis.toInt()
        }
        rootLayout.setOnLongPressListener { press ->
            if (press) {
                if (photo.burn == 0) {
                    setBurningState(photo)
                }
            } else {
                setBurnedState(true, photo)
                viewModel.burnPhoto(photo)
            }
        }
    }

    private fun setBurningState(photo: Photo) {
        burnLayout.animate().alpha(0f).start()
        burnStateView.animate().alpha(0f).start()
        if (photo.burnTime > 0) {
            progressView.maxProgress = photo.burnTimeInMillis.toInt()
            progressView.visibility = View.VISIBLE
            progressView.startAnimation(photo.burnTimeInMillis) {
                if (!isHidden) {
                    setBurnedState(true, photo)
                    viewModel.burnPhoto(photo)
                }
            }
        }
    }

    private fun setBurnedState(animate: Boolean, photo: Photo) {
        photo.burn = 1
        rootLayout.setOnLongPressListener(null)

        burnLayout.visibility = View.VISIBLE
        if (animate) burnLayout.animate().alpha(1f).start()

        progressView.visibility = View.INVISIBLE
        progressView.cancelAnimation()

        burnStateView.visibility = View.VISIBLE
        if (animate) burnStateView.animate().alpha(1f).start()
        if (viewerIsVip) {
            burnStateView.setText(R.string.photo_has_burned)
            buyVipButton.visibility = View.GONE
        } else {
            burnStateView.setText(R.string.open_vip_no_limit_to_view)
            buyVipButton.visibility = View.VISIBLE
            buyVipButton.setOnClickListener {
                startActivityForResult<VipActivity>(Page.VIP, REQUEST_VIP_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIP_CODE && resultCode == Activity.RESULT_OK) {

        }
    }

    override fun loadFile(uri: Uri?, url: String?, photo: Photo?) {
        if (photo?.flag == 0) {
            if (photo.burn == 0) {
                setNotBurnedState(photo)
            } else if (photo.burn == 1) {
                setBurnedState(false, photo)
            }
        }
        super.loadFile(uri, url, photo)
    }

    override fun onDestroyView() {
        progressView.cancelAnimation()
        super.onDestroyView()
    }
}
