package com.snt.phoney.ui.privacy

import android.os.Bundle
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.utils.data.Constants


class PrivacyActivity : CommonActivity() {

    private var mThemeId: Int = 0

    override fun onConfigureTheme(): Int? = mThemeId

    override fun onCreate(savedInstanceState: Bundle?) {
        mThemeId = intent?.getIntExtra(Constants.Extra.THEME, 0) ?: 0
        super.onCreate(savedInstanceState)
    }

}
