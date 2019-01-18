package com.snt.phoney.ui.privacy

import com.snt.phoney.base.CommonActivity
import com.snt.phoney.utils.data.Constants


class PrivacyActivity : CommonActivity() {

    override fun onApplyTheme(themeId: Int): Int {
        return intent?.getIntExtra(Constants.Extra.THEME, themeId) ?: themeId
    }

}
