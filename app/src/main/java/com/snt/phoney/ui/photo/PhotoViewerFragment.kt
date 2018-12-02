package com.snt.phoney.ui.photo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snt.phoney.R
import kotlinx.android.synthetic.main.fragment_photo_viewer.*

class PhotoViewerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = PhotoViewerFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }

    lateinit var adapter: PhotoViewerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PhotoViewerAdapter(childFragmentManager)
        viewPager.adapter = adapter


    }


}
