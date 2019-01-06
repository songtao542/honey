package com.snt.phoney.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.NoInjectBaseFragment
import com.snt.phoney.extensions.getVersionName
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.browser.WebBrowserActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_about.*

/**
 *
 */
class AboutFragment : NoInjectBaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AboutFragment().apply {
            this.arguments = arguments
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.about_title)

        version.text = getString(R.string.about_version_template, requireContext().getVersionName())

        help.setOnClickListener {
            startActivity<WebBrowserActivity>(Bundle().apply {
                putString(Constants.Extra.TITLE, getString(R.string.use_help))
                putString(Constants.Extra.URL, Constants.Api.HELP_URL)
            })
        }
        star.setOnClickListener { }
        policy.setOnClickListener {
            startActivity<WebBrowserActivity>(Bundle().apply {
                putString(Constants.Extra.TITLE, getString(R.string.user_protocol))
                putString(Constants.Extra.URL, Constants.Api.USER_PROTOCOL_URL)
            })
        }
    }


}
