package com.snt.phoney.ui.photo

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.media.MediaFile
import com.snt.phoney.utils.media.MultipartUtil
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_view.*
import kotlinx.coroutines.*
import java.io.File


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

        scaleImageView.setOnClickListener {
            onPhotoSingleTapListener?.onPhotoSingleTap()
        }

        loadFile(uri, url, photo)
    }

    fun setOnPhotoSingleTapListener(listener: OnPhotoSingleTapListener?) {
        onPhotoSingleTapListener = listener
    }

    interface OnPhotoSingleTapListener {
        fun onPhotoSingleTap()
    }

    private fun loadFile(uri: Uri? = null, url: String? = null, photo: Photo? = null) {
        loadImageJob?.cancel()
        loadImageJob = GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val file = Glide.with(this@PhotoViewFragment)
                        .asFile()
                        .load(uri ?: url ?: photo?.path)
                        .submit()
                        .get()
                if (isActive) {
                    GlobalScope.launch(Dispatchers.Main) {
                        file?.let { f ->
                            if ("image/gif" == MultipartUtil.getFileMime(f)) {
                                imageView.visibility = View.VISIBLE
                                Glide.with(this@PhotoViewFragment)
                                        .load(f)
                                        .apply(RequestOptions()
                                                .priority(Priority.HIGH)
                                                .fitCenter())
                                        .into(imageView)
                            } else {
                                scaleImageView.visibility = View.VISIBLE
                                scaleImageView.setImage(ImageSource.uri(Uri.fromFile(f)))
                            }
                            return@let
                        }
                    }
                }
                return@withContext
            }
        }
    }

    private var loadImageJob: Job? = null

    override fun onDestroyView() {
        loadImageJob?.cancel()
        super.onDestroyView()
    }

}
