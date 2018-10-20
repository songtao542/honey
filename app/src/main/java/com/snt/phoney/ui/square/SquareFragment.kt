package com.snt.phoney.ui.square


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.ui.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.square.popular.PopularRecommendFragment
import kotlinx.android.synthetic.main.fragment_home.*
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

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> officialRecommendFragment
                    else -> popularRecommendFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

        }

        val tabOfficialTitle = SpannableString(getString(R.string.tab_square_official))
        tabOfficialTitle.setSpan(RelativeSizeSpan(1f), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tabOfficialTitle.setSpan(RelativeSizeSpan(0.64f), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val tabPopularTitle = SpannableString(getString(R.string.tab_square_popular))
        tabPopularTitle.setSpan(RelativeSizeSpan(1f), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tabPopularTitle.setSpan(RelativeSizeSpan(0.64f), 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        squareTab.getTabAt(0)?.text = tabOfficialTitle
        squareTab.getTabAt(1)?.text = tabPopularTitle
    }

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }
}
