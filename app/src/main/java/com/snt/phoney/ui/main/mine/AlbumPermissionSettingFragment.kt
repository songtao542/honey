package com.snt.phoney.ui.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment

/**
 */
class AlbumPermissionSettingFragment : BottomDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_permission_setting, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AlbumPermissionSettingFragment().apply {
            this.arguments = arguments
        }
    }
}
