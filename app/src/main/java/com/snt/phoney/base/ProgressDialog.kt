package com.snt.phoney.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.snt.phoney.R

class ProgressDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    fun cancelable(cancelable: Boolean): ProgressDialog {
        isCancelable = cancelable
        return this
    }

}