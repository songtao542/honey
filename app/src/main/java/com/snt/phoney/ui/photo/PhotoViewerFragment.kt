package com.snt.phoney.ui.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.extensions.getStatusBarHeight
import com.snt.phoney.utils.KeyEventListener
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_viewer.*

@Suppress("unused")
open class PhotoViewerFragment : Fragment(), PhotoFragment.OnPhotoSingleTapListener, KeyEventListener {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = PhotoViewerFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var adapter: PhotoViewerAdapter
    private var urls: ArrayList<String>? = null
    private var uris: ArrayList<Uri>? = null
    private var photos: ArrayList<Photo>? = null
    private var index: Int = 0
    private var deletable = false

    private var deletedUrls: ArrayList<String>? = null
    private var deletedUris: ArrayList<Uri>? = null
    private var deletedPhotos: ArrayList<Photo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            urls = it.getStringArrayList(Constants.Extra.URL_LIST)
            uris = it.getParcelableArrayList(Constants.Extra.URI_LIST)
            photos = it.getParcelableArrayList(Constants.Extra.PHOTO_LIST)
            index = it.getInt(Constants.Extra.INDEX, 0)
            deletable = it.getBoolean(Constants.Extra.DELETABLE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_viewer, container, false)
    }

    @Suppress("CascadeIf")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBar.setPadding(0, getStatusBarHeight(), 0, 0)
        back.setOnClickListener { finish() }
        if (deletable) {
            delete.setOnClickListener {
                delete()
            }
        } else {
            delete.visibility = View.GONE
        }
        adapter = PhotoViewerAdapter(this, deletable)
        adapter.onPhotoSingleTapListener = this
        if (uris != null) {
            adapter.uris = uris
        } else if (urls != null) {
            adapter.urls = urls
        } else {
            adapter.photos = photos
        }

        viewPager.adapter = adapter
        viewPager.currentItem = index
    }

    open fun newPhotoViewFragment(uri: Uri): PhotoFragment {
        return PhotoFragment.newInstance(uri, arguments)
    }

    open fun newPhotoViewFragment(url: String): PhotoFragment {
        return PhotoFragment.newInstance(url, arguments)
    }

    open fun newPhotoViewFragment(photo: Photo): PhotoFragment {
        return PhotoFragment.newInstance(photo, arguments)
    }

    override fun onPhotoSingleTap() {
        toggleActionBar()
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

    private fun delete() {
        when {
            deleteUri() -> return
            deleteUrl() -> return
            else -> deletePhoto()
        }
    }

    private fun deleteUrl(): Boolean {
        if (urls != null) {
            val urls = urls!!
            if (urls.size > 0) {
                val index = viewPager.currentItem
                val item = urls[index]
                urls.remove(item)
                if (deletedUrls == null) {
                    deletedUrls = ArrayList()
                }
                deletedUrls!!.add(item)
                if (mOnDeleteUrlListener != null) {
                    mOnDeleteUrlListener!!.invoke(index, item)
                }
                if (urls.size == 0) {
                    finish()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
            return true
        } else {
            return false
        }
    }

    private fun deleteUri(): Boolean {
        if (uris != null) {
            val uris = uris!!
            if (uris.size > 0) {
                val index = viewPager.currentItem
                val item = uris[index]
                uris.remove(item)
                if (deletedUris == null) {
                    deletedUris = ArrayList()
                }
                deletedUris!!.add(item)
                if (mOnDeleteUriListener != null) {
                    mOnDeleteUriListener!!.invoke(index, item)
                }
                if (uris.size == 0) {
                    finish()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
            return true
        } else {
            return false
        }
    }

    private fun deletePhoto(): Boolean {
        if (photos != null) {
            val photos = photos!!
            if (photos.size > 0) {
                val index = viewPager.currentItem
                val item = photos[index]
                photos.remove(item)
                if (deletedPhotos == null) {
                    deletedPhotos = ArrayList()
                }
                deletedPhotos!!.add(item)
                if (mOnDeletePhotoListener != null) {
                    mOnDeletePhotoListener!!.invoke(index, item)

                }
                if (photos.size == 0) {
                    finish()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
            return true
        } else {
            return false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setActivityResult()
        }
        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    protected open fun finish() {
        setActivityResult()
        activity?.onBackPressed()
    }

    private fun setActivityResult() {
        if (urls != null && deletedUrls != null) {
            if (mOnUrlResultListener != null) {
                mOnUrlResultListener?.invoke(deletedUrls!!)
            } else {
                val result = Intent().apply {
                    putStringArrayListExtra(Constants.Extra.LIST, deletedUrls)
                }
                activity?.setResult(Activity.RESULT_OK, result)
            }
        } else if (uris != null && deletedUris != null) {
            if (mOnUriResultListener != null) {
                mOnUriResultListener?.invoke(deletedUris!!)
            } else {
                val result = Intent().apply {
                    putParcelableArrayListExtra(Constants.Extra.LIST, deletedUris)
                }
                activity?.setResult(Activity.RESULT_OK, result)
            }
        } else if (photos != null && deletedPhotos != null) {
            if (mOnPhotoResultListener != null) {
                mOnPhotoResultListener?.invoke(deletedPhotos!!)
            } else {
                val result = Intent().apply {
                    putParcelableArrayListExtra(Constants.Extra.LIST, deletedPhotos)
                }
                activity?.setResult(Activity.RESULT_OK, result)
            }
        }
    }

    private var mOnDeleteUrlListener: ((index: Int, url: String) -> Unit)? = null
    fun setOnUrlDeleteListener(listener: ((index: Int, url: String) -> Unit)) {
        this.mOnDeleteUrlListener = listener
    }

    private var mOnDeleteUriListener: ((index: Int, uri: Uri) -> Unit)? = null
    fun setOnUriDeleteListener(listener: ((index: Int, uri: Uri) -> Unit)) {
        this.mOnDeleteUriListener = listener
    }

    private var mOnDeletePhotoListener: ((index: Int, photo: Photo) -> Unit)? = null
    fun setOnPhotoDeleteListener(listener: ((index: Int, photo: Photo) -> Unit)) {
        this.mOnDeletePhotoListener = listener
    }


    private var mOnUriResultListener: ((deletedUris: List<Uri>) -> Unit)? = null
    fun setOnUriResultListener(listener: ((deletedUris: List<Uri>) -> Unit)) {
        mOnUriResultListener = listener
    }

    private var mOnUrlResultListener: ((deletedUrls: List<String>) -> Unit)? = null
    fun setOnUrlResultListener(listener: ((deletedUrls: List<String>) -> Unit)) {
        mOnUrlResultListener = listener
    }

    private var mOnPhotoResultListener: ((deletedPhotos: List<Photo>) -> Unit)? = null
    fun setOnPhotoResultListener(listener: ((deletedPhotos: List<Photo>) -> Unit)) {
        mOnPhotoResultListener = listener
    }

}
