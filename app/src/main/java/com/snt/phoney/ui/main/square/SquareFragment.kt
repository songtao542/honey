package com.snt.phoney.ui.main.square


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.main.square.popular.PopularRecommendFragment
import com.snt.phoney.widget.TabLayout
import kotlinx.android.synthetic.main.fragment_square.*
import java.lang.Exception

/**
 */
class SquareFragment : BaseFragment(), Toolbar.OnMenuItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_square, container, false)
    }

    override fun openUmeng() = false

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

    fun setChildFragmentUserVisibleHint(isVisibleToUser: Boolean) {
        try {
            (squarePager.adapter as FragmentStatePagerAdapter).getItem(squarePager.currentItem).userVisibleHint = isVisibleToUser
        } catch (e: Exception) {
            Log.d("SquareFragment", "error:${e.message},e")
        }
    }


}
