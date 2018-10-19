package com.snt.phoney.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.ui.home.following.FollowingFragment
import com.snt.phoney.ui.home.friend.FriendFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setHasOptionsMenu(true)
//        var activity = activity
//        if (activity is AppCompatActivity) {
//            activity.setSupportActionBar(homeToolbar)
//        }
        homeAppBar.bringToFront()
        homeTab.setupWithViewPager(homePager)
        homeTab.tabMode = TabLayout.MODE_SCROLLABLE
        homePager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val friendFragment: FriendFragment = FriendFragment.newInstance()
            val followingFragment: FollowingFragment = FollowingFragment.newInstance()

            val tabFriendTitle = getString(R.string.tab_home_friend)
            val tabFollowingTitle = getString(R.string.tab_home_following)

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> friendFragment
                    else -> followingFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when {
                    position <= 0 -> tabFriendTitle
                    else -> tabFollowingTitle
                }
            }
        }

//        homeTab.addTab(homeTab.newTab().setText("Tab 1"))
//        homeTab.addTab(homeTab.newTab().setText("Tab 2"))
    }

}
