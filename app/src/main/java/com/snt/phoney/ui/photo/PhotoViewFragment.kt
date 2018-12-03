package com.snt.phoney.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.utils.data.Constants
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import kotlinx.android.synthetic.main.fragment_photo_view.*


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
    }

    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(Constants.Extra.URL)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageView.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
        Glide.with(requireActivity()).load(url).into(imageView)

    }

}
