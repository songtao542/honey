package com.snt.phoney.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.snt.phoney.ui.about.AboutFragment
import com.snt.phoney.ui.album.AlbumViewerFragment
import com.snt.phoney.ui.album.PaySettingFragment
import com.snt.phoney.ui.album.PhotoApplyListFragment
import com.snt.phoney.ui.auth.AuthModeFragment
import com.snt.phoney.ui.browser.OfficialMessageFragment
import com.snt.phoney.ui.dating.create.CreateDatingFragment
import com.snt.phoney.ui.dating.detail.DatingDetailFragment
import com.snt.phoney.ui.dating.list.ApplicantListFragment
import com.snt.phoney.ui.dating.list.DatingListFragment
import com.snt.phoney.ui.dating.list.MyDatingFragment
import com.snt.phoney.ui.dating.list.OthersDatingFragment
import com.snt.phoney.ui.location.LocationPickerFragment
import com.snt.phoney.ui.main.mine.AlbumPermissionSettingFragment
import com.snt.phoney.ui.nearby.NearbyFragment
import com.snt.phoney.ui.photo.PhotoViewerFragment
import com.snt.phoney.ui.privacy.CreateLockFragment
import com.snt.phoney.ui.report.ReportFragment
import com.snt.phoney.ui.user.EditUserFragment
import com.snt.phoney.ui.user.FollowMeFragment
import com.snt.phoney.ui.user.UserInfoFragment
import com.snt.phoney.ui.user.VisitorFragment
import com.snt.phoney.ui.vip.VipFragment
import com.snt.phoney.ui.wallet.WalletDetailFragment
import com.snt.phoney.ui.wallet.WalletFragment
import com.snt.phoney.ui.wallet.WithdrawFragment


enum class Page {
    EDIT_USER,
    USER_INFO,
    VISITOR,
    FOLLOW_ME,

    DATING_LIST,
    MY_DATING,
    OTHERS_DATING,
    DATING_DETAIL,
    CREATE_DATING,

    REPORT,

    VIP,

    WALLET,
    ABOUT,
    ALBUM_PERMISSION_SETTING,
    CREATE_PRIVACY_LOCK,

    LOCATION_PICKER,

    PHOTO_VIEWER,

    PAY_SETTING,

    OFFICIAL_MESSAGE,

    DATING_APPLICANT_LIST,

    NEARBY,

    ALBUM_VIEWER,
    PHOTO_APPLY_LIST,

    WALLET_DETAIL,
    WITHDRAW,

    AUTHENTICATE
}

class FragmentFactory {

    companion object {
        fun create(page: Int, arguments: Bundle? = null): Fragment {
            return when (page) {
                Page.EDIT_USER.ordinal -> EditUserFragment.newInstance(arguments)
                Page.USER_INFO.ordinal -> UserInfoFragment.newInstance(arguments)
                Page.VISITOR.ordinal -> VisitorFragment.newInstance(arguments)
                Page.FOLLOW_ME.ordinal -> FollowMeFragment.newInstance(arguments)

                Page.DATING_LIST.ordinal -> DatingListFragment.newInstance(arguments)
                Page.MY_DATING.ordinal -> MyDatingFragment.newInstance(arguments)
                Page.OTHERS_DATING.ordinal -> OthersDatingFragment.newInstance(arguments)
                Page.DATING_DETAIL.ordinal -> DatingDetailFragment.newInstance(arguments)
                Page.CREATE_DATING.ordinal -> CreateDatingFragment.newInstance(arguments)

                Page.REPORT.ordinal -> ReportFragment.newInstance(arguments)

                Page.VIP.ordinal -> VipFragment.newInstance(arguments)

                Page.WALLET.ordinal -> WalletFragment.newInstance(arguments)
                Page.WALLET_DETAIL.ordinal -> WalletDetailFragment.newInstance(arguments)
                Page.WITHDRAW.ordinal -> WithdrawFragment.newInstance(arguments)

                Page.ABOUT.ordinal -> AboutFragment.newInstance(arguments)
                Page.ALBUM_PERMISSION_SETTING.ordinal -> AlbumPermissionSettingFragment.newInstance(arguments)
                Page.CREATE_PRIVACY_LOCK.ordinal -> CreateLockFragment.newInstance(arguments)

                Page.LOCATION_PICKER.ordinal -> LocationPickerFragment.newInstance(arguments)

                Page.PHOTO_VIEWER.ordinal -> PhotoViewerFragment.newInstance(arguments)

                Page.PAY_SETTING.ordinal -> PaySettingFragment.newInstance(arguments)

                Page.OFFICIAL_MESSAGE.ordinal -> OfficialMessageFragment.newInstance(arguments)

                Page.DATING_APPLICANT_LIST.ordinal -> ApplicantListFragment.newInstance(arguments)

                Page.NEARBY.ordinal -> NearbyFragment.newInstance(arguments)

                Page.ALBUM_VIEWER.ordinal -> AlbumViewerFragment.newInstance(arguments)
                Page.PHOTO_APPLY_LIST.ordinal -> PhotoApplyListFragment.newInstance(arguments)

                Page.AUTHENTICATE.ordinal -> AuthModeFragment.newInstance(arguments)

                else -> Fragment()
            }
        }
    }
}

