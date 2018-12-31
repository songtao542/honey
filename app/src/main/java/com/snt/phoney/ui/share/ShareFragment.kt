package com.snt.phoney.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import kotlinx.android.synthetic.main.fragment_share.*

/**
 *
 */
class ShareFragment : BottomDialogFragment() {

    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppTheme_ShareBottomSheetDialog);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShareViewModel::class.java)

        cancel.setOnClickListener {
            dismiss()
        }
        shareByWechat.setOnClickListener {
            viewModel.shareByWechat()
            dismiss()
        }
        shareByQQ.setOnClickListener {
            activity?.let { activity ->
                //viewModel.shareByQQ(activity)
                viewModel.shareByQzone(activity)
            }
            dismiss()
        }
        shareByWeibo.setOnClickListener {
            activity?.let { activity ->
                viewModel.shareByWeibo(activity)
            }
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = ShareFragment().apply {
            this.arguments = arguments
        }
    }
}
