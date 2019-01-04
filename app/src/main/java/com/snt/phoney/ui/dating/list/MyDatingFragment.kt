package com.snt.phoney.ui.dating.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter

import com.snt.phoney.R
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_dating_mine.*

/**
 *
 */
class MyDatingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dating_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val myPublishFragment: DatingListFragment = DatingListFragment.newInstance(Bundle().apply {
                arguments?.let {
                    putAll(it)
                }
                putInt(Constants.Extra.TYPE, DatingListFragment.TYPE_PUBLISH)
                putInt("TOOLBAR_VISIBILITY", View.GONE)
            })
            val myApplyingFragment: DatingListFragment = DatingListFragment.newInstance(Bundle().apply {
                arguments?.let {
                    putAll(it)
                }
                putInt(Constants.Extra.TYPE, DatingListFragment.TYPE_JOINED)
                putInt("TOOLBAR_VISIBILITY", View.GONE)
            })

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> myPublishFragment
                    else -> myApplyingFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when {
                    position <= 0 -> getString(R.string.my_published)
                    else -> getString(R.string.my_applying)
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle?) = MyDatingFragment().apply {
            this.arguments = arguments
        }
    }
}
