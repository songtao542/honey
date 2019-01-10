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
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

//        if (uri != null) {
//            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv uri=$uri")
//            Glide.with(this)
//                    .asFile()
//                    .load(uri)
//                    .into(FileViewTarget(this, scaleImageView, imageView))
//
//        } else if (url != null) {
//            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv url=$url")
//            loadFile(url!!)
////            Glide.with(this)
////                    .asFile()
////                    .load(url)
////                    .into(FileViewTarget(this, scaleImageView, imageView))
//        } else if (photo != null && photo!!.path != null) {
//            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv photo=$photo")
//            Glide.with(this)
//                    .asFile()
//                    .load(photo!!.path!!)
//                    .into(FileViewTarget(this, scaleImageView, imageView))
//        }
    }

    fun setOnPhotoSingleTapListener(listener: OnPhotoSingleTapListener?) {
        onPhotoSingleTapListener = listener
    }

    interface OnPhotoSingleTapListener {
        fun onPhotoSingleTap()
    }

    private fun loadFile(uri: Uri? = null, url: String? = null, photo: Photo? = null) {
        Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv uri  =$uri")
        Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv url  =$url")
        Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv photo=$photo")
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val file = Glide.with(this@PhotoViewFragment)
                        .asFile()
                        .load(uri ?: url ?: photo?.path)
                        .submit()
                        .get()
                GlobalScope.launch(Dispatchers.Main) {
                    file?.let { f ->
                        Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv file=$f")
                        Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv mime=${getFileMime(f)}")
                        if ("image/gif" == getFileMime(f)) {
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
                return@withContext
            }
        }
    }

    private fun getFileMime(file: File): String {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)
        return options.outMimeType
    }

    inner class FileViewTarget(private val fragment: Fragment, view: SubsamplingScaleImageView, private val alternative: ImageViewTouch) : CustomViewTarget<SubsamplingScaleImageView, File>(view) {

        override fun onStart() {
            super.onStart()
            Log.d("TTTT", "vvvvvvvvvvvvvv onStart vvvvvvvvvvvvvvvvvv onStart=")
        }

        override fun onStop() {
            super.onStop()
            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv onStop=")
        }

        override fun onResourceLoading(placeholder: Drawable?) {
            super.onResourceLoading(placeholder)
            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv onResourceLoading=")
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv onLoadFailed=")
        }

        override fun onResourceCleared(placeholder: Drawable?) {
            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv onLoadFailed=")
        }

        override fun onResourceReady(resource: File, transition: Transition<in File>?) {
            Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv resource=$resource")
            if ("image/gif" == MediaFile.getMimeTypeForFile(resource.absolutePath)) {
                Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv gif")
                alternative.visibility = View.VISIBLE
                Glide.with(fragment)
                        .load(resource)
                        .apply(RequestOptions()
                                .priority(Priority.HIGH)
                                .fitCenter())
                        .into(alternative)
            } else {
                Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv not gif")
                view.visibility = View.VISIBLE
                view.setImage(ImageSource.uri(Uri.fromFile(resource)))
            }
        }
    }

}
