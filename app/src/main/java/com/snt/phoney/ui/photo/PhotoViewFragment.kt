package com.snt.phoney.ui.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.davemorrissey.labs.subscaleview.ImageSource
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.extensions.startActivityForResult
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.media.MultipartUtil
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_burn.*
import kotlinx.android.synthetic.main.fragment_photo_view.*
import kotlinx.coroutines.*

const val REQUEST_VIP_CODE = 60

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

    private var countDownTimer: CountDownTimer? = null

    private fun startCountDown(photo: Photo) {
        burnLayout.visibility = View.GONE
        val timeMillis = if (photo.burnTime <= 0) 5000 else photo.burnTime.toLong() * 1000
        progressView.startAnim(timeMillis) {
            burned(photo)
        }
//        countDownTimer = object : CountDownTimer(timeMillis, 1000) {
//            override fun onFinish() {
//                burnLayout.visibility = View.VISIBLE
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//
//            }
//        }
//        countDownTimer?.start()
    }

    private fun notBurned(photo: Photo) {
        burnLayout.visibility = View.VISIBLE
        progressView.visibility = View.VISIBLE
        buyVipButton.visibility = View.GONE
        burnStateView.setText(R.string.press_to_view)
        rootLayout.setOnLongClickListener {
            Log.d("TTTT", "dddddddddddddddddddddddddddddd")
            if (photo.burn == 0) {
                rootLayout.setInterceptTouchEvent(true)
                startCountDown(photo)
            }
            return@setOnLongClickListener true
        }
        rootLayout.setOnTouchListener { v, event ->
            val action = event.action
            Log.d("TTTT", "action=======$action")
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("TTTT", "uuuuuuuuuuuuuuuuuuuuuuuuuuuu")
                    burned(photo)
                }
            }
            return@setOnTouchListener rootLayout.intercept
        }
    }

    private fun burned(photo: Photo) {
        photo.burn = 1
        rootLayout.setInterceptTouchEvent(false)
        rootLayout.setOnTouchListener(null)
        burnLayout.visibility = View.VISIBLE
        progressView.visibility = View.GONE
        progressView.cancelAnimation()
        buyVipButton.visibility = View.VISIBLE
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
        }
    }

    private fun loadFile(uri: Uri? = null, url: String? = null, photo: Photo? = null) {
        loadImageJob?.cancel()
        if (photo?.burn == 0) {
            notBurned(photo)
        } else if (photo?.burn == 1) {
            burned(photo)
        }
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
        progressView.cancelAnimation()
        loadImageJob?.cancel()
        super.onDestroyView()
    }

}
