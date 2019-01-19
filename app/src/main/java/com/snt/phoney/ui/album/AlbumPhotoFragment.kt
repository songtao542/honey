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
import com.snt.phoney.ui.member.MemberActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_burn.*
import kotlinx.android.synthetic.main.fragment_photo_view.*
import javax.inject.Inject

const val REQUEST_MEMBER_CODE = 60

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

    private var viewerIsMember = false

    private var parent: AlbumViewerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewerIsMember = it.getBoolean(Constants.Extra.IS_MEMBER, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel::class.java)
    }

    private fun setNotBurnedState(photo: Photo) {
        burnLayout.visibility = View.VISIBLE
        burnStateView.visibility = View.VISIBLE

        buyMemberButton.visibility = View.GONE
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
        if (viewerIsMember) {
            burnStateView.setText(R.string.photo_has_burned)
            buyMemberButton.visibility = View.GONE
        } else {
            burnStateView.setText(R.string.open_member_no_limit_to_view)
            buyMemberButton.visibility = View.VISIBLE
            buyMemberButton.setOnClickListener {
                startActivityForResult<MemberActivity>(Page.MEMBER, REQUEST_MEMBER_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEMBER_CODE && resultCode == Activity.RESULT_OK) {
            viewerIsMember = true
            parent?.updateToMemberState()

        }
    }

    override fun loadFile(uri: Uri?, url: String?, photo: Photo?) {
        if (photo?.flag == 0 && !viewerIsMember) {
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

    fun setParentFragment(parent: AlbumViewerFragment) {
        this.parent = parent
    }
}
