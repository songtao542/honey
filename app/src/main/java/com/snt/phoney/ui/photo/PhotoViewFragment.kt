package com.snt.phoney.ui.photo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snt.phoney.R


/**
 */
class PhotoViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = PhotoViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
