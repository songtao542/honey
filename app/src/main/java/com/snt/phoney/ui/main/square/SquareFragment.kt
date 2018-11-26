package com.snt.phoney.ui.main.square


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.main.square.popular.PopularRecommendFragment
import com.snt.phoney.widget.TabLayout
import kotlinx.android.synthetic.main.fragment_square.*

/**
 * A simple [Fragment] subclass.
 * Use the [SquareFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SquareFragment : BaseFragment() {

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
        enableOptionsMenu(squareToolbar, false)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.square, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.publishDating -> {
                context?.let { it.startActivity(CommonActivity.newIntent<DatingActivity>(it, Page.CREATE_DATING)) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }
}
