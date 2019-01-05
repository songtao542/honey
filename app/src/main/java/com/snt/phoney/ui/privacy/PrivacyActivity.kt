package com.snt.phoney.ui.privacy

import android.os.Bundle
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.utils.data.Constants


class PrivacyActivity : CommonActivity() {

    private var mThemeId: Int? = null

    override fun onConfigureTheme(): Int? = if (-1 == mThemeId) null else mThemeId

    override fun onCreate(savedInstanceState: Bundle?) {
        mThemeId = intent?.getIntExtra(Constants.Extra.THEME, -1)
        super.onCreate(savedInstanceState)
    }

}
