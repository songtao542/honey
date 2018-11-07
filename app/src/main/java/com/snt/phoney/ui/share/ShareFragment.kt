package com.snt.phoney.ui.share

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import kotlinx.android.synthetic.main.fragment_share.*

/**
 *
 */
class ShareFragment : BottomDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppTheme_ShareBottomSheetDialog);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cancel.setOnClickListener {
            dismiss()
        }
        shareByWechat.setOnClickListener { }
        shareByQQ.setOnClickListener { }
        shareByWeibo.setOnClickListener { }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = ShareFragment().apply {
            this.arguments = arguments
        }
    }
}
