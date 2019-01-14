package com.snt.phoney.ui.album

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.extensions.startActivityForResult
import com.snt.phoney.ui.photo.PhotoFragment
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_burn.*
import kotlinx.android.synthetic.main.fragment_photo_view.*

const val REQUEST_VIP_CODE = 60

/**
 */
class AlbumPhotoFragment : PhotoFragment() {

    companion object {
        @JvmStatic
        fun newInstance(url: String) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.Extra.URL, url)
            }
        }

        @JvmStatic
        fun newInstance(uri: Uri) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.URI, uri)
            }
        }

        @JvmStatic
        fun newInstance(photo: Photo) = AlbumPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.PHOTO, photo)
            }
        }
    }

    private fun startBurning(photo: Photo) {
        burnLayout.visibility = View.GONE
        burnStateView.visibility = View.INVISIBLE
        progressView.startAnimation(photo.burnTimeInMillis) {
            if (!isHidden) {
                burned(photo)
            }
        }
    }

    private fun notBurned(photo: Photo) {
        burnLayout.visibility = View.VISIBLE
        stateLayout.visibility = View.VISIBLE
        progressView.visibility = View.VISIBLE
        progressView.maxProgress = photo.burnTimeInMillis.toInt()
        buyVipButton.visibility = View.GONE
        burnStateView.setText(R.string.press_to_view)

        rootLayout.setOnLongPressListener { press ->
            Log.d("TTTT", "xxxxmmmmmmmmmmmmmmmmmmmm press=$press")
            if (press) {
                if (photo.burn == 0) {
                    startBurning(photo)
                }
            } else {
                burned(photo)
            }
        }
    }

    private fun burned(photo: Photo) {
        photo.burn = 1
        Log.d("TTTT", "========== rootLayout=$rootLayout   path=${photo.path}")
        rootLayout.setOnLongPressListener(null)
        burnLayout.visibility = View.VISIBLE
        stateLayout.visibility = View.VISIBLE
        progressView.visibility = View.GONE
        progressView.cancelAnimation()
        buyVipButton.visibility = View.VISIBLE
        burnStateView.visibility = View.VISIBLE
        burnStateView.setText(R.string.open_vip_no_limit_to_view)
        buyVipButton.setOnClickListener {
            startActivityForResult<VipActivity>(Page.VIP, REQUEST_VIP_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIP_CODE && resultCode == Activity.RESULT_OK) {
            photo?.burn = -1
            burnLayout.visibility = View.GONE
            stateLayout.visibility = View.GONE
        }
    }

    override fun loadFile(uri: Uri?, url: String?, photo: Photo?) {
        if (photo?.flag == 0) {
            if (photo.burn == 0) {
                notBurned(photo)
            } else if (photo.burn == 1) {
                burned(photo)
            }
        }
        super.loadFile(uri, url, photo)
    }

    override fun onDestroyView() {
        progressView.cancelAnimation()
        super.onDestroyView()
    }

}
