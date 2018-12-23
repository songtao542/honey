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
import com.snt.phoney.domain.model.Photo
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
    private var photos: ArrayList<Photo>? = null
    private var index: Int = 0
    private var deletable = false

    private var deletedUrls: ArrayList<String>? = null
    private var deletedUris: ArrayList<Uri>? = null
    private var deletedPhotos: ArrayList<Photo>? = null

    private var onResultListener: OnResultListener? = null

    private var onDeleteListener: OnDeleteListener? = null

    private var backPressed: Boolean = true


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBar.setPadding(0, getStatusBarHeight(), 0, 0)
        back.setOnClickListener { activity?.onBackPressed() }
        if (deletable) {
            delete.setOnClickListener {
                delete()
            }
        } else {
            delete.visibility = View.GONE
        }
        adapter = PhotoViewerAdapter(childFragmentManager, deletable)
        adapter.onPhotoSingleTapListener = this
        if (uris != null) {
            adapter.uris = uris
        } else if (urls != null) {
            adapter.urls = urls
        } else {
            adapter.photos = photos
        }

        Log.d("TTTT", "pppppppppppppppppppppp$photos")

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
                if (onDeleteListener != null) {
                    onDeleteListener!!.onDelete(index, deletedUrl = item)
                    if (urls.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            activity?.finish()
                        } else {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    if (deletedUrls == null) {
                        deletedUrls = ArrayList()
                    }
                    deletedUrls!!.add(item)
                    if (urls.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            val result = Intent().apply {
                                putStringArrayListExtra(Constants.Extra.LIST, deletedUrls)
                            }
                            activity?.setResult(Activity.RESULT_OK, result)
                            activity?.finish()
                        } else if (onResultListener != null) {
                            onResultListener?.onResult(deletedUrls = deletedUrls)
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
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
                if (onDeleteListener != null) {
                    onDeleteListener!!.onDelete(index, deletedUri = item)
                    if (uris.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            activity?.finish()
                        } else {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    if (deletedUris == null) {
                        deletedUris = ArrayList()
                    }
                    deletedUris!!.add(item)
                    if (uris.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            val result = Intent().apply {
                                putParcelableArrayListExtra(Constants.Extra.LIST, deletedUris)
                            }
                            activity?.setResult(Activity.RESULT_OK, result)
                            activity?.finish()
                        } else if (onResultListener != null) {
                            onResultListener?.onResult(deletedUris = deletedUris)
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
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
                if (onDeleteListener != null) {
                    onDeleteListener!!.onDelete(index, deletePhoto = item)
                    if (photos.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            activity?.finish()
                        } else {
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    if (deletedPhotos == null) {
                        deletedPhotos = ArrayList()
                    }
                    deletedPhotos!!.add(item)
                    if (photos.size == 0) {
                        backPressed = false
                        if (activity is PhotoViewerActivity) {
                            val result = Intent().apply {
                                putParcelableArrayListExtra(Constants.Extra.LIST, deletedPhotos)
                            }
                            activity?.setResult(Activity.RESULT_OK, result)
                            activity?.finish()
                        } else if (onResultListener != null) {
                            onResultListener?.onResult(deletedPhotos = deletedPhotos)
                            activity?.supportFragmentManager?.popBackStack()
                        }
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            return true
        } else {
            return false
        }
    }

    override fun onDestroyView() {
        if (backPressed) {
            if (urls != null && deletedUrls != null) {
                if (activity is PhotoViewerActivity) {
                    val result = Intent().apply {
                        putStringArrayListExtra(Constants.Extra.LIST, deletedUrls)
                    }
                    activity?.setResult(Activity.RESULT_OK, result)
                } else if (onResultListener != null) {
                    onResultListener?.onResult(deletedUrls = deletedUrls)
                }
            } else if (uris != null && deletedUris != null) {
                if (activity is PhotoViewerActivity) {
                    val result = Intent().apply {
                        putParcelableArrayListExtra(Constants.Extra.LIST, deletedUris)
                    }
                    activity?.setResult(Activity.RESULT_OK, result)
                } else if (onResultListener != null) {
                    onResultListener?.onResult(deletedUris = deletedUris)
                }
            } else if (photos != null && deletedPhotos != null) {
                if (activity is PhotoViewerActivity) {
                    val result = Intent().apply {
                        putParcelableArrayListExtra(Constants.Extra.LIST, deletedPhotos)
                    }
                    activity?.setResult(Activity.RESULT_OK, result)
                } else if (onResultListener != null) {
                    onResultListener?.onResult(deletedPhotos = deletedPhotos)
                }
            }
        }
        super.onDestroyView()
    }

    fun setOnResultListener(onResultListener: OnResultListener) {
        this.onResultListener = onResultListener
    }

    fun setOnDeleteListener(onDeleteListener: OnDeleteListener) {
        this.onDeleteListener = onDeleteListener
    }

    override fun onPhotoSingleTap() {
        toggleActionBar()
    }

    interface OnDeleteListener {
        fun onDelete(index: Int, deletedUrl: String? = null, deletedUri: Uri? = null, deletePhoto: Photo? = null)
    }

    interface OnResultListener {
        fun onResult(deletedUris: List<Uri>? = null, deletedUrls: List<String>? = null, deletedPhotos: List<Photo>? = null)
    }
}
