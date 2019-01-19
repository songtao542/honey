package com.snt.phoney.base

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.snt.phoney.R
import kotlinx.android.synthetic.main.fragment_alert_dialog.*

/**
 *
 */
class AlertDialogFragment : DialogFragment() {

    private var alertParams: AlertParams? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setBackgroundDrawable(ColorDrawable(0x00000000)) // .setBackgroundDrawableResource(R.drawable.pay_picker_bg)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        alertParams?.let { params ->
            isCancelable = params.mCancelable
            if (params.mCancelable) {
                dialog?.setOnCancelListener { params.mOnCancelListener?.invoke() }
            }
            dialog?.setOnDismissListener { params.mOnDismissListener?.invoke() }
            params.mTitle?.let { titleView.text = it }
            params.mPositiveButtonText?.let { positiveButton.text = it }
            params.mNegativeButtonText?.let { negativeButton.text = it }
            params.mMessage?.let { messageView.text = it }
            positiveButton.setOnClickListener { params.mPositiveButtonListener?.invoke(this@AlertDialogFragment) }
            negativeButton.setOnClickListener { params.mNegativeButtonListener?.invoke(this@AlertDialogFragment) }
            if (TextUtils.isEmpty(params.mNegativeButtonText)) {
                negativeButton.visibility = View.GONE
                positiveButton.setBackgroundResource(R.drawable.alert_dialog_button_selector)
            } else {
                negativeButton.visibility = View.VISIBLE
                positiveButton.setBackgroundResource(R.drawable.alert_dialog_positive_selector)
            }
            return@let
        }
    }

    class AlertParams(context: Context) {
        var mContext: Context? = context
        var mTitle: CharSequence? = null
        var mMessage: CharSequence? = null
        var mPositiveButtonText: CharSequence? = null
        var mPositiveButtonListener: ((dialog: AlertDialogFragment) -> Unit)? = null
        var mNegativeButtonText: CharSequence? = null
        var mNegativeButtonListener: ((dialog: AlertDialogFragment) -> Unit)? = null

        var mCancelable: Boolean = false
        var mOnCancelListener: (() -> Unit)? = null
        var mOnDismissListener: (() -> Unit)? = null
    }

    class Builder constructor(val context: Context, @StyleRes private val themeResId: Int = 0) {

        private val params: AlertParams = AlertParams(if (themeResId == 0) context else ContextThemeWrapper(context, themeResId))

        fun setTitle(@StringRes titleId: Int): Builder {
            params.mTitle = context.getText(titleId)
            return this
        }

        fun setTitle(@Nullable title: CharSequence): Builder {
            params.mTitle = title
            return this
        }

        fun setMessage(@StringRes messageId: Int): Builder {
            params.mMessage = context.getText(messageId)
            return this
        }

        fun setMessage(@Nullable message: CharSequence): Builder {
            params.mMessage = message
            return this
        }

        fun setPositiveButton(@StringRes textId: Int, listener: ((dialog: AlertDialogFragment) -> Unit)?): Builder {
            params.mPositiveButtonText = context.getText(textId)
            params.mPositiveButtonListener = listener
            return this
        }

        fun setPositiveButton(text: CharSequence, listener: ((dialog: AlertDialogFragment) -> Unit)?): Builder {
            params.mPositiveButtonText = text
            params.mPositiveButtonListener = listener
            return this
        }

        fun setNegativeButton(@StringRes textId: Int, listener: ((dialog: AlertDialogFragment) -> Unit)?): Builder {
            params.mNegativeButtonText = context.getText(textId)
            params.mNegativeButtonListener = listener
            return this
        }

        fun setNegativeButton(text: CharSequence, listener: ((dialog: AlertDialogFragment) -> Unit)?): Builder {
            params.mNegativeButtonText = text
            params.mNegativeButtonListener = listener
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            params.mCancelable = cancelable
            return this
        }

        fun setOnCancelListener(onCancelListener: (() -> Unit)?): Builder {
            params.mOnCancelListener = onCancelListener
            return this
        }

        fun setOnDismissListener(onDismissListener: (() -> Unit)?): Builder {
            params.mOnDismissListener = onDismissListener
            return this
        }

        fun create(): AlertDialogFragment {
            val dialog = AlertDialogFragment()
            dialog.alertParams = params
            return dialog
        }

        fun show(fragmentManager: FragmentManager): AlertDialogFragment {
            val dialog = create()
            dialog.show(fragmentManager, "dialog")
            return dialog
        }
    }
}
