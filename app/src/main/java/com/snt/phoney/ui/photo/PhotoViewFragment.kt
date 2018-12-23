package com.snt.phoney.ui.photo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.utils.data.Constants
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_view.*


/**
 */
class PhotoViewFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(url: String) = PhotoViewFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.Extra.URL, url)
            }
        }

        @JvmStatic
        fun newInstance(uri: Uri) = PhotoViewFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.URI, uri)
            }
        }

        @JvmStatic
        fun newInstance(photo: Photo) = PhotoViewFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.PHOTO, photo)
            }
        }
    }

    private var onPhotoSingleTapListener: OnPhotoSingleTapListener? = null
    private var uri: Uri? = null
    private var url: String? = null
    private var photo: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getParcelable(Constants.Extra.URI)
            url = it.getString(Constants.Extra.URL)
            photo = it.getParcelable(Constants.Extra.PHOTO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageView.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
        imageView.setSingleTapListener {
            onPhotoSingleTapListener?.onPhotoSingleTap()
        }

        if (uri != null) {
            val size = PhotoMetadataUtils.getBitmapSize(uri, requireActivity())
            Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions()
                            .override(size.x, size.y)
                            .priority(Priority.HIGH)
                            .fitCenter())
                    .into(imageView)
        } else if (url != null) {
            Glide.with(this)
                    .load(url)
                    .apply(RequestOptions()
                            .priority(Priority.HIGH)
                            .fitCenter())
                    .into(imageView)
        } else if (photo != null && photo!!.path != null) {
            Glide.with(this)
                    .load(photo!!.path!!)
                    .apply(RequestOptions()
                            .priority(Priority.HIGH)
                            .fitCenter())
                    .into(imageView)
        }

        Log.d("TTTT", "pathhhhhhhhhhhhhhhhhhhhhhhhhh$photo")
    }

    fun setOnPhotoSingleTapListener(listener: OnPhotoSingleTapListener?) {
        onPhotoSingleTapListener = listener
    }

    interface OnPhotoSingleTapListener {
        fun onPhotoSingleTap()
    }

}
