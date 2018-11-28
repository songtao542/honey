package com.snt.phoney.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.disableShiftMode
import com.snt.phoney.extensions.forEach
import com.snt.phoney.extensions.hideIcon
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.ui.main.home.HomeFragment
import com.snt.phoney.ui.main.message.MessageFragment
import com.snt.phoney.ui.main.mine.MineFragment
import com.snt.phoney.ui.main.square.SquareFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

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
        return@OnNavigationItemSelectedListener false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLayoutFullscreen()
        //setStatusBarColor(colorOf(R.color.colorPrimaryFemale))
        navigation.disableShiftMode()
        navigation.hideIcon()
        showFragment("home")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentContainer.setOnApplyWindowInsetsListener { view, insets ->
            var consumed = false
            if (view is ViewGroup) {
                view.forEach { child ->
                    val childResult = child.dispatchApplyWindowInsets(insets)
                    consumed = childResult.isConsumed
                }
            }
            if (consumed) insets.consumeSystemWindowInsets() else insets
        }
    }

    private fun showFragment(tag: String) {
        var willShow = getFragmentByTag(tag)
        if (willShow != currentFragment) {
            var hasAdded = supportFragmentManager.findFragmentByTag(tag) != null
            var fragmentManager = supportFragmentManager
            var transaction = fragmentManager.beginTransaction()
            currentFragment?.let {
                transaction.hide(it)
            }
            when {
                hasAdded -> {
                    transaction.show(willShow)
                    transaction.commit()
                }
                else -> {
                    transaction.add(R.id.fragmentContainer, willShow, tag)
                    transaction.commit()
                }
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
