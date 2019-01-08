package com.snt.phoney.ui.main.home

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.ui.main.home.following.FollowingFragment
import com.snt.phoney.ui.main.home.friend.FriendFragment
import com.snt.phoney.ui.main.home.friend.FriendViewModel
import com.snt.phoney.utils.Picker
import com.snt.phoney.widget.TabLayout
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : BaseFragment(), Toolbar.OnMenuItemClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: FriendViewModel

    private var mPickerIndex1 = 0
    private var mPickerIndex2 = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendViewModel::class.java)

        enableOptionsMenu(homeToolbar, false, R.menu.home)
        homeToolbar.setOnMenuItemClickListener(this)

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

            override fun getPageTitle(position: Int): CharSequence? {
                return when {
                    position <= 0 -> getString(R.string.tab_home_friend)
                    else -> getString(R.string.tab_home_following)
                }
            }

        }
        homePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                homeToolbar.menu.findItem(R.id.findCity)?.isVisible = position == 0
            }
        })
    }

//    override fun onResume() {
//        super.onResume()
//        setMenuVisibility(true)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater?.inflate(R.menu.home, menu)
//    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.findCity -> {
                Picker.showPicker(activity, getString(R.string.select_city), mPickerIndex1, mPickerIndex2, provider = { picker ->
                    if (viewModel.cities.value != null) {
                        val cities = viewModel.cities.value!!
                        val arrayMap = LinkedHashMap<String, ArrayList<String>>()
                        //不选择的情况
                        arrayMap[getString(R.string.none_city)] = ArrayList<String>().apply { add(" ") }
                        for (city in cities) {
                            var pList = arrayMap[city.province]
                            if (pList == null) {
                                pList = ArrayList()
                                arrayMap[city.province] = pList!!
                            }
                            pList?.add(city.name)
                        }
                        picker.setColumn(columns = arrayMap)
                    } else {
                        viewModel.cities.observe(this, Observer { cities ->
                            val arrayMap = LinkedHashMap<String, ArrayList<String>>()
                            //不选择的情况
                            arrayMap[getString(R.string.none_city)] = ArrayList<String>().apply { add(" ") }
                            for (city in cities) {
                                var pList = arrayMap[city.province]
                                if (pList == null) {
                                    pList = ArrayList()
                                    arrayMap[city.province] = pList!!
                                }
                                pList?.add(city.name)
                            }
                            picker.setColumn(columns = arrayMap)
                        })
                    }
                }, handler = { index1, index2 ->
                    mPickerIndex1 = index1
                    mPickerIndex2 = index2
                }) { _, value2 ->
                    if (TextUtils.isEmpty(value2.trim())) {
                        item.setTitle(R.string.find_city)
                    } else {
                        item.title = value2
                    }
                    viewModel.setFilterByCity(value2.trim())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
