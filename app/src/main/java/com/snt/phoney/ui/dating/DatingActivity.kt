package com.snt.phoney.ui.dating

import com.snt.phoney.base.CommonActivity
import com.snt.phoney.utils.data.Constants


class DatingActivity : CommonActivity() {

    override fun onConfigureTheme(): Int? {
        return intent?.getIntExtra(Constants.Extra.THEME, 0) ?: 0
    }
}
