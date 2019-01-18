package com.snt.phoney.ui.album

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.di.Injectable
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.ui.photo.PhotoFragment
import com.snt.phoney.ui.photo.PhotoViewerFragment
import com.snt.phoney.utils.data.Constants
import cust.app.swipeback.SwipeBackLayout
import javax.inject.Inject

class AlbumViewerFragment : PhotoViewerFragment(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AlbumViewerFragment().apply {
            this.arguments = arguments
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AlbumViewModel

    private var viewerIsVip = false
    private var vipStateChanged = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel::class.java)

        setOnPhotoDeleteListener { _, photo ->
            viewModel.deletePhotos(ArrayList<Photo>().apply { add(photo) })
        }
    }

    override fun newPhotoViewFragment(photo: Photo): PhotoFragment {
        return AlbumPhotoFragment.newInstance(photo, arguments).also { it.setParentFragment(this@AlbumViewerFragment) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (setResult()) {
                false
            } else {
                super.onKeyDown(keyCode, event)
            }
        }
        return false
    }

    override fun finish() {
        if (setResult()) {
            activity?.onBackPressed()
        } else {
            super.finish()
        }
    }

    private fun setResult(): Boolean {
        if (viewModel.burnedPhoto.isNotEmpty() || vipStateChanged) {
            val result = Intent().apply {
                putParcelableArrayListExtra(Constants.Extra.LIST, viewModel.burnedPhoto)
                putExtra(Constants.Extra.IS_VIP, viewerIsVip)
            }
            activity?.setResult(Activity.RESULT_OK, result)
            activity?.onBackPressed()
            return true
        }
        return false
    }

    fun updateToVipState() {
        viewerIsVip = true
        vipStateChanged = true
        if (arguments != null) {
            arguments!!.putBoolean(Constants.Extra.IS_VIP, viewerIsVip)
        } else {
            arguments = Bundle().apply { putBoolean(Constants.Extra.IS_VIP, viewerIsVip) }
        }
        adapter.forceNotifyDataSetChanged()
    }

}