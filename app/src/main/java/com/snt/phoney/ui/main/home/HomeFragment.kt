package com.snt.phoney.ui.main.home

import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.ui.main.home.following.FollowingFragment
import com.snt.phoney.ui.main.home.friend.FriendFragment
import kotlinx.android.synthetic.main.fragment_home.*
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.SpannableString
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.widget.TabLayout


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(homeToolbar, false)
        homeTab.setupWithViewPager(homePager)
        homeTab.tabMode = TabLayout.MODE_SCROLLABLE
        homePager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val friendFragment: FriendFragment = FriendFragment.newInstance()
            val followingFragment: FollowingFragment = FollowingFragment.newInstance()

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> friendFragment
                    else -> followingFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

        }

        val tabFriendTitle = getString(R.string.tab_home_friend)
        val tabFollowingTitle = SpannableString(getString(R.string.tab_home_following))
        tabFollowingTitle.setSpan(RelativeSizeSpan(1f), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tabFollowingTitle.setSpan(RelativeSizeSpan(0.64f), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        homeTab.getTabAt(0)?.text = tabFriendTitle
        homeTab.getTabAt(1)?.text = tabFollowingTitle

//        homeTab.getTabAt(0)?.text = getString(R.string.tab_home_friend)
//        homeTab.getTabAt(1)?.text = getString(R.string.tab_home_following)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.findCity -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
