package com.snt.phoney.ui.main.square


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.main.UMengPageName
import com.snt.phoney.ui.main.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.main.square.popular.PopularRecommendFragment
import com.snt.phoney.widget.TabLayout
import kotlinx.android.synthetic.main.fragment_square.*

/**
 */
class SquareFragment : BaseFragment(), Toolbar.OnMenuItemClickListener, UMengPageName {

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }

    private var viewPager: ViewPager? = null

    override fun enableUMengAgent(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_square, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(squareToolbar, false, R.menu.square)
        squareToolbar.setOnMenuItemClickListener(this)

        squareTab.setupWithViewPager(squarePager)
        squareTab.tabMode = TabLayout.MODE_SCROLLABLE
        squarePager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val officialRecommendFragment: OfficialRecommendFragment = OfficialRecommendFragment.newInstance()
            val popularRecommendFragment: PopularRecommendFragment = PopularRecommendFragment.newInstance()

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> officialRecommendFragment
                    else -> popularRecommendFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when {
                    position <= 0 -> getString(R.string.tab_square_official)
                    else -> getString(R.string.tab_square_popular)
                }
            }
        }
        squarePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val oldPageName = if (position == 0) getPageName(1) else getPageName(0)
                val newPageName = getPageName(position)
                (activity as MainActivity).onPageChanged(oldPageName, newPageName)
            }

        })
        viewPager = squarePager
    }

//    override fun onResume() {
//        super.onResume()
//        setMenuVisibility(true)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater?.inflate(R.menu.square, menu)
//    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.publishDating -> {
                context?.let { it.startActivity<DatingActivity>(Page.CREATE_DATING) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPageName(position: Int): String {
        return if (position == 0) "OfficialRecommendFragment" else "PopularRecommendFragment"
    }

    override fun getPageName(): String {
        val position = viewPager?.currentItem ?: 0
        return getPageName(position)
    }

}
