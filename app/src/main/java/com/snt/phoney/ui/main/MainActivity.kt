package com.snt.phoney.ui.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.disableShiftMode
import com.snt.phoney.extensions.setStatusBarColor
import com.snt.phoney.ui.home.HomeFragment
import com.snt.phoney.ui.message.MessageFragment
import com.snt.phoney.ui.mine.MineFragment
import com.snt.phoney.ui.square.SquareFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val homeFragment = HomeFragment.newInstance()
    private val squareFragment = SquareFragment.newInstance()
    private val messageFragment = MessageFragment.newInstance()
    private val mineFragment = MineFragment.newInstance()

    private var currentFragment: Fragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                showFragment("home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_square -> {
                showFragment("square")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                showFragment("message")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                showFragment("mine")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setStatusBarColor(resources.getColor(R.color.colorPrimary))
        setStatusBarColor(colorOf(R.color.colorPrimary))
        navigation.disableShiftMode()
        showFragment("home")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun showFragment(tag: String) {
        var willShow = getFragmentByTag(tag)
        Log.d("TTTT", "willShow=${willShow} currentFragment=${currentFragment}")
        if (willShow != currentFragment) {
            var hasAdded = supportFragmentManager.findFragmentByTag(tag) != null
            when {
                hasAdded -> supportFragmentManager?.beginTransaction()?.hide(currentFragment)?.show(willShow)?.commit()
                currentFragment != null -> supportFragmentManager?.beginTransaction()?.hide(currentFragment)?.add(R.id.fragmentContainer, willShow, tag)?.commit()
                else -> supportFragmentManager?.beginTransaction()?.add(R.id.fragmentContainer, willShow, tag)?.commit()
            }
            currentFragment = willShow
        }
    }

    private fun getFragmentByTag(tag: String): Fragment {
        return when (tag) {
            "home" -> homeFragment
            "square" -> squareFragment
            "message" -> messageFragment
            else -> mineFragment
        }
    }
}
