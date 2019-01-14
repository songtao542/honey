package com.snt.phoney.ui.photo

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.snt.phoney.domain.model.Photo

class PhotoViewerAdapter(private val parentFragment: PhotoViewerFragment, private val deletable: Boolean)
    : FragmentStatePagerAdapter(parentFragment.childFragmentManager) {

    var urls: List<String>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }
    var uris: List<Uri>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }
    var photos: List<Photo>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    var onPhotoSingleTapListener: PhotoFragment.OnPhotoSingleTapListener? = null

    @Suppress("CascadeIf")
    override fun getItem(position: Int): Fragment {
        return if (uris != null) {
            parentFragment.newPhotoViewFragment(uris!![position]).apply { setOnPhotoSingleTapListener(onPhotoSingleTapListener) }
        } else if (urls != null) {
            parentFragment.newPhotoViewFragment(urls!![position]).apply { setOnPhotoSingleTapListener(onPhotoSingleTapListener) }
        } else {
            parentFragment.newPhotoViewFragment(photos!![position]).apply { setOnPhotoSingleTapListener(onPhotoSingleTapListener) }
        }
    }

    override fun getCount(): Int = uris?.size ?: urls?.size ?: photos?.size ?: 0

    override fun getItemPosition(obj: Any): Int {
        return if (deletable) PagerAdapter.POSITION_NONE else super.getItemPosition(obj)
    }
}