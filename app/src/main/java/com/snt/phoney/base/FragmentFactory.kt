package com.snt.phoney.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.snt.phoney.ui.about.AboutFragment
import com.snt.phoney.ui.dating.create.CreateDatingFragment
import com.snt.phoney.ui.dating.detail.DatingDetailFragment
import com.snt.phoney.ui.dating.list.DatingListFragment
import com.snt.phoney.ui.main.mine.AlbumPermissionSettingFragment
import com.snt.phoney.ui.privacy.CreateLockFragment
import com.snt.phoney.ui.report.ReportFragment
import com.snt.phoney.ui.user.EditUserFragment
import com.snt.phoney.ui.user.FollowMeFragment
import com.snt.phoney.ui.user.UserInfoFragment
import com.snt.phoney.ui.user.VisitorFragment
import com.snt.phoney.ui.vip.VipFragment
import com.snt.phoney.ui.wallet.WalletFragment


enum class Page {
    EDIT_USER_INFO,
    VIEW_USER_INFO,
    VIEW_RECENT_VISITOR,
    VIEW_FOLLOW_ME,

    VIEW_DATING_LIST,
    VIEW_DATING_DETAIL,
    CREATE_DATING,

    CREATE_REPORT,

    UPGRADE_VIP,

    VIEW_MY_WALLET,
    VIEW_ABOUT,
    SET_ALBUM_PERMISSION,
    CREATE_PRIVACY_PASS,
}

class FragmentFactory {

    companion object {
        fun create(page: Int, arguments: Bundle? = null): Fragment {
            return when (page) {
                Page.EDIT_USER_INFO.ordinal -> EditUserFragment.newInstance(arguments)
                Page.VIEW_USER_INFO.ordinal -> UserInfoFragment.newInstance(arguments)
                Page.VIEW_RECENT_VISITOR.ordinal -> VisitorFragment.newInstance(arguments)
                Page.VIEW_FOLLOW_ME.ordinal -> FollowMeFragment.newInstance(arguments)

                Page.VIEW_DATING_LIST.ordinal -> DatingListFragment.newInstance(arguments)
                Page.VIEW_DATING_DETAIL.ordinal -> DatingDetailFragment.newInstance(arguments)
                Page.CREATE_DATING.ordinal -> CreateDatingFragment.newInstance(arguments)
                Page.CREATE_REPORT.ordinal -> ReportFragment.newInstance(arguments)

                Page.UPGRADE_VIP.ordinal -> VipFragment.newInstance(arguments)
                Page.VIEW_MY_WALLET.ordinal -> WalletFragment.newInstance(arguments)
                Page.VIEW_ABOUT.ordinal -> AboutFragment.newInstance(arguments)
                Page.SET_ALBUM_PERMISSION.ordinal -> AlbumPermissionSettingFragment.newInstance(arguments)
                Page.CREATE_PRIVACY_PASS.ordinal -> CreateLockFragment.newInstance(arguments)


                else -> Fragment()
            }
        }
    }
}

