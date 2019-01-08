package com.snt.phoney.ui.wallet

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.snt.phoney.R
import com.snt.phoney.extensions.enableOptionsMenu
import com.snt.phoney.utils.Picker
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_wallet_detail.*
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 */
class WalletDetailFragment : Fragment(),Toolbar.OnMenuItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance(argument: Bundle? = null) = WalletDetailFragment().apply {
            arguments = argument
        }
    }

    private val df = SimpleDateFormat("yyyy-MM-dd")

    private val today = Date()
    private val calendar = Calendar.getInstance()

    private val filterColumn = Array<String>(90) { i ->
        calendar.time = today
        calendar.add(Calendar.DATE, -i)
        return@Array df.format(calendar.time)
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
        enableOptionsMenu(toolbar, false, R.menu.wallet_detail)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setOnMenuItemClickListener(this)
        titleTextView.setText(R.string.view_records)

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

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        menu.clear()
//        inflater.inflate(R.menu.wallet_detail, menu)
//    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.query -> {
                Picker.showPicker(activity, getString(R.string.select_daytime),
                        filterColumn, 0,
                        isColumn2SubOfColumn1 = true) { value1, value2 ->
                    ((viewPager.adapter as FragmentStatePagerAdapter).getItem(viewPager.currentItem)
                            as DetailListFragment).filter(filterColumn[value1], filterColumn[value2])
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
