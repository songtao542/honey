package com.snt.phoney.ui.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.extensions.getStatusBarHeight
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_viewer.*

class PhotoViewerFragment : Fragment(), PhotoViewFragment.OnPhotoSingleTapListener {


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = PhotoViewerFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var adapter: PhotoViewerAdapter
    private var urls: ArrayList<String>? = null
    private var uris: ArrayList<Uri>? = null
    private var index: Int = 0
    private var deletable = false

    private var deletedUrls: ArrayList<String>? = null
    private var deletedUris: ArrayList<Uri>? = null

    private var onResultListener: OnResultListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            urls = it.getStringArrayList(Constants.Extra.URL_LIST)
            uris = it.getParcelableArrayList(Constants.Extra.URI_LIST)
            index = it.getInt(Constants.Extra.INDEX, 0)
            deletable = it.getBoolean(Constants.Extra.DELETABLE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBar.setPadding(0, getStatusBarHeight(), 0, 0)
        back.setOnClickListener { activity?.onBackPressed() }
        if (deletable) {
            delete.setOnClickListener {
                deletePhoto()
            }
        } else {
            delete.visibility = View.GONE
        }
        adapter = PhotoViewerAdapter(childFragmentManager, deletable)
        adapter.onPhotoSingleTapListener = this
        if (uris != null) {
            adapter.uris = uris
        } else {
            adapter.urls = urls
        }

        viewPager.adapter = adapter
        viewPager.currentItem = index
    }

    private fun toggleActionBar() {
        val showing = (actionBar.tag as? Boolean) ?: true
        if (showing) {
            actionBar.tag = false
            actionBar.animate().translationY(-actionBar.height.toFloat())
        } else {
            actionBar.tag = true
            actionBar.animate().translationY(0f)
        }
    }

    private fun deletePhoto() {
        if (uris != null) {
            val uris = uris!!
            if (uris.size > 0) {
                if (deletedUris == null) {
                    deletedUris = ArrayList()
                }
                val item = uris[viewPager.currentItem]
                deletedUris!!.add(item)
                uris.remove(item)
                if (uris.size == 0) {
                    if (activity is PhotoViewerActivity) {
                        val result = Intent().apply {
                            putParcelableArrayListExtra(Constants.Extra.LIST, deletedUris)
                        }
                        activity?.setResult(Activity.RESULT_OK, result)
                        activity?.finish()
                    } else if (onResultListener != null) {
                        onResultListener?.onResult(deletedUris, null)
                        activity?.supportFragmentManager?.popBackStack()
                    }
                } else {
                    Log.d("TTTT", "delete dddddddddddddddddddddddddddddddddddddddddddddddddd")
                    adapter.notifyDataSetChanged()
                }
            }
        } else if (urls != null) {
            val urls = urls!!
            if (urls.size > 0) {
                if (deletedUrls == null) {
                    deletedUrls = ArrayList()
                }
                deletedUrls!!.add(urls[viewPager.currentItem])
                val item = urls[viewPager.currentItem]
                urls.remove(item)

                if (urls.size == 0) {
                    if (activity is PhotoViewerActivity) {
                        val result = Intent().apply {
                            putStringArrayListExtra(Constants.Extra.LIST, deletedUrls)
                        }
                        activity?.setResult(Activity.RESULT_OK, result)
                        activity?.finish()
                    } else if (onResultListener != null) {
                        onResultListener?.onResult(null, deletedUrls)
                        activity?.supportFragmentManager?.popBackStack()
                    }
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    fun setOnResultListener(onResultListener: OnResultListener) {
        this.onResultListener = onResultListener
    }

    override fun onPhotoSingleTap() {
        toggleActionBar()
    }

    interface OnResultListener {
        fun onResult(deletedUris: List<Uri>? = null, deletedUrls: List<String>? = null)
    }
}
