package com.snt.phoney.ui.square


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.ui.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.square.popular.PopularRecommendFragment
import kotlinx.android.synthetic.main.fragment_square.*

/**
 * A simple [Fragment] subclass.
 * Use the [SquareFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SquareFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_square, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        squareTab.setupWithViewPager(squarePager)
        squareTab.tabMode = TabLayout.MODE_SCROLLABLE
        squarePager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val officialRecommendFragment: OfficialRecommendFragment = OfficialRecommendFragment.newInstance()
            val popularRecommendFragment: PopularRecommendFragment = PopularRecommendFragment.newInstance()

            val tabOfficialTitle = getString(R.string.tab_square_official)
            val tabPopularTitle = getString(R.string.tab_square_popular)

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
                    position <= 0 -> tabOfficialTitle
                    else -> tabPopularTitle
                }
            }
        }

//        squareTab.addTab(squareTab.newTab().setText("Tab 1"))
//        squareTab.addTab(squareTab.newTab().setText("Tab 2"))
    }

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }
}
