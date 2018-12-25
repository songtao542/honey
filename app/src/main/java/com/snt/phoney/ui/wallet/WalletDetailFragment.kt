package com.snt.phoney.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snt.phoney.R
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_wallet_detail.*


/**
 *
 */
class WalletDetailFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(argument: Bundle? = null) = WalletDetailFragment().apply {
            arguments = argument
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = object : FragmentStatePagerAdapter(this.childFragmentManager) {
            val rechargeFragment: DetailListFragment = DetailListFragment.newInstance(Bundle().apply {
                arguments?.let {
                    putAll(it)
                }
                putInt(Constants.Extra.TYPE, DetailListFragment.TYPE_RECHARGE)
            })
            val consumeFragment: DetailListFragment = DetailListFragment.newInstance(Bundle().apply {
                arguments?.let {
                    putAll(it)
                }
                putInt(Constants.Extra.TYPE, DetailListFragment.TYPE_CONSUME)
            })

            override fun getItem(position: Int): Fragment {
                return when {
                    position <= 0 -> rechargeFragment
                    else -> consumeFragment
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when {
                    position <= 0 -> getString(R.string.recharge_detail)
                    else -> getString(R.string.consume_detail)
                }
            }

        }
    }


}
