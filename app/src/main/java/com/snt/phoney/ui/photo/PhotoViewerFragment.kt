package com.snt.phoney.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_photo_viewer.*

class PhotoViewerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = PhotoViewerFragment().apply {
            this.arguments = arguments
        }
    }

    lateinit var adapter: PhotoViewerAdapter
    lateinit var urls: List<String>
    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            urls = it.getStringArrayList(Constants.Extra.LIST)
            index = it.getInt(Constants.Extra.INDEX, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PhotoViewerAdapter(childFragmentManager)
        adapter.urls = urls
        viewPager.adapter = adapter
        viewPager.currentItem = index
    }


}
