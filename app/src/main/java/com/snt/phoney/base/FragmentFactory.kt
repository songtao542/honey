package com.snt.phoney.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.snt.phoney.ui.dating.create.CreateDatingFragment
import com.snt.phoney.ui.dating.list.DatingListFragment
import com.snt.phoney.ui.report.ReportFragment
import com.snt.phoney.ui.user.EditUserFragment
import com.snt.phoney.ui.user.FollowmeFragment
import com.snt.phoney.ui.user.UserInfoFragment
import com.snt.phoney.ui.user.VisitorFragment


enum class Page {
    EDIT_USER_INFO,
    VIEW_USER_INFO,
    VIEW_RECENT_VISITOR,
    VIEW_FOLLOW_ME,

    VIEW_DATING_LIST,
    CREATE_DATING,
    CREATE_REPORT
}

class FragmentFactory {

    companion object {
        fun create(page: Int, arguments: Bundle? = null): Fragment {
            return when (page) {
                Page.EDIT_USER_INFO.ordinal -> EditUserFragment.newInstance(arguments)
                Page.VIEW_USER_INFO.ordinal -> UserInfoFragment.newInstance(arguments)
                Page.VIEW_RECENT_VISITOR.ordinal -> VisitorFragment.newInstance(arguments)
                Page.VIEW_FOLLOW_ME.ordinal -> FollowmeFragment.newInstance(arguments)
                Page.VIEW_DATING_LIST.ordinal -> DatingListFragment.newInstance(arguments)
                Page.CREATE_DATING.ordinal -> CreateDatingFragment.newInstance(arguments)
                Page.CREATE_REPORT.ordinal -> ReportFragment.newInstance(arguments)
                else -> Fragment()
            }
        }
    }
}

