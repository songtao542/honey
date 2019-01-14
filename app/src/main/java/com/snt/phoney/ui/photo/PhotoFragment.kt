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
import com.davemorrissey.labs.subscaleview.ImageSource
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.media.MultipartUtil
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_burn.*
import kotlinx.android.synthetic.main.fragment_photo_view.*
import kotlinx.coroutines.*

/**
 */
open class PhotoFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(url: String) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.Extra.URL, url)
            }
        }

        @JvmStatic
        fun newInstance(uri: Uri) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.URI, uri)
            }
        }

        @JvmStatic
        fun newInstance(photo: Photo) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Extra.PHOTO, photo)
            }
        }
    }

    private var onPhotoSingleTapListener: OnPhotoSingleTapListener? = null

    private var loadImageJob: Job? = null

    protected var uri: Uri? = null
    protected var url: String? = null
    protected var photo: Photo? = null

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

    protected open fun loadFile(uri: Uri? = null, url: String? = null, photo: Photo? = null) {
        loadImageJob?.cancel()
        loadImageJob = GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val file = Glide.with(this@PhotoFragment)
                            .asFile()
                            .load(uri ?: url ?: photo?.path)
                            .submit()
                            .get()
                    if (isActive) {
                        GlobalScope.launch(Dispatchers.Main) {
                            file?.let { f ->
                                if (f.exists()) {
                                    if ("image/gif" == MultipartUtil.getFileMime(f)) {
                                        scaleImageView.visibility = View.GONE
                                        imageView.visibility = View.VISIBLE
                                        Glide.with(this@PhotoFragment)
                                                .load(f)
                                                .apply(RequestOptions()
                                                        .priority(Priority.HIGH)
                                                        .fitCenter())
                                                .into(imageView)
                                    } else {
                                        imageView.visibility = View.GONE
                                        scaleImageView.visibility = View.VISIBLE
                                        scaleImageView.setImage(ImageSource.uri(Uri.fromFile(f)))
                                    }
                                }
                                return@let
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PhotoFragment", "load photo error!", e)
                }
                return@withContext
            }
        }
    }

    override fun onDestroyView() {
        loadImageJob?.cancel()
        super.onDestroyView()
    }

    fun setOnPhotoSingleTapListener(listener: OnPhotoSingleTapListener?) {
        onPhotoSingleTapListener = listener
    }

    interface OnPhotoSingleTapListener {
        fun onPhotoSingleTap()
    }

}
