package com.snt.phoney.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.base.Page
import com.snt.phoney.extensions.disableShiftMode
import com.snt.phoney.extensions.hideIcon
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.main.home.HomeFragment
import com.snt.phoney.ui.main.message.MessageFragment
import com.snt.phoney.ui.main.mine.MineFragment
import com.snt.phoney.ui.main.square.SquareFragment
import com.snt.phoney.ui.privacy.PrivacyActivity
import com.snt.phoney.utils.data.Constants
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG_HOME = "home"
private const val TAG_SQUARE = "square"
private const val TAG_MESSAGE = "message"
private const val TAG_MINE = "mine"
private const val EXTRA_TAG = "current_tag"

class MainActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    private var currentFragment: Fragment? = null

    lateinit var viewModel: MainViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragmentProxy(TAG_HOME)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_square -> {
                showFragmentProxy(TAG_SQUARE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_message -> {
                showFragmentProxy(TAG_MESSAGE)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_mine -> {
                showFragmentProxy(TAG_MINE)
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    private fun showFragmentProxy(tag: String) {
        //友盟统计，先对前一个 page 调用 onPageEnd
        getUMengPageName()?.let {
            Log.d("UMENG", "end   name:$it")
            MobclickAgent.onPageEnd(it)
        }
        showFragment(tag)
        //再对切换后的 page 调用 onPageStart
        getUMengPageName()?.let {
            Log.d("UMENG", "start name:$it")
            MobclickAgent.onPageStart(it)
        }
    }

    override fun onResume() {
        super.onResume()
        checkPrivacyLock()
        getUMengPageName()?.let {
            Log.d("UMENG", "start name:$it")
            MobclickAgent.onPageStart(it)
        }
    }

    override fun onPause() {
        super.onPause()
        getUMengPageName()?.let {
            Log.d("UMENG", "end   name:$it")
            MobclickAgent.onPageEnd(it)
        }
    }

    private fun getUMengPageName(): String? {
        val current = currentFragment
        return if (current is UMengPageName) {
            current.getPageName()
        } else {
            null
        }
    }

    fun onPageChanged(oldPageName: String, newPageName: String) {
        //用于 HomeFragment SquareFragment 内部 ViewPager 切换时主动通知页面切换
        Log.d("UMENG", "end   name:$oldPageName")
        MobclickAgent.onPageEnd(oldPageName)
        Log.d("UMENG", "start name:$newPageName")
        MobclickAgent.onPageStart(newPageName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //先检查是否设置了密码
        //checkPrivacyLock()
        setContentView(R.layout.activity_main)
        setLayoutFullscreen()
        //setStatusBarColor(colorOf(R.color.colorPrimaryFemale))
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        navigation.disableShiftMode()
        navigation.hideIcon()

        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState.getString(EXTRA_TAG, TAG_HOME))
        } else {
            showFragment(TAG_HOME)
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        viewModel.init()
    }

    private fun showFragment(tag: String) {
        val willShow = getFragmentByTag(tag)
        if (willShow != currentFragment) {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            currentFragment?.let { transaction.hide(it) }

            fragmentManager.findFragmentByTag(tag)?.apply {
                transaction.show(this)
            } ?: transaction.add(R.id.fragmentContainer, willShow, tag)

            transaction.commit()
            currentFragment = willShow
        }
    }

    private fun restoreFragment(tag: String) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        when (tag) {
            TAG_HOME -> {
                currentFragment = fragmentManager.findFragmentByTag(TAG_HOME)?.apply { transaction.show(this) }
                fragmentManager.findFragmentByTag(TAG_SQUARE)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_MESSAGE)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_MINE)?.apply { transaction.hide(this) }
            }
            TAG_SQUARE -> {
                fragmentManager.findFragmentByTag(TAG_HOME)?.apply { transaction.hide(this) }
                currentFragment = fragmentManager.findFragmentByTag(TAG_SQUARE)?.apply { transaction.show(this) }
                fragmentManager.findFragmentByTag(TAG_MESSAGE)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_MINE)?.apply { transaction.hide(this) }
            }
            TAG_MESSAGE -> {
                fragmentManager.findFragmentByTag(TAG_HOME)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_SQUARE)?.apply { transaction.hide(this) }
                currentFragment = fragmentManager.findFragmentByTag(TAG_MESSAGE)?.apply { transaction.show(this) }
                fragmentManager.findFragmentByTag(TAG_MINE)?.apply { transaction.hide(this) }
            }
            TAG_MINE -> {
                fragmentManager.findFragmentByTag(TAG_HOME)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_SQUARE)?.apply { transaction.hide(this) }
                fragmentManager.findFragmentByTag(TAG_MESSAGE)?.apply { transaction.hide(this) }
                currentFragment = fragmentManager.findFragmentByTag(TAG_MINE)?.apply { transaction.show(this) }
            }
        }
        if (currentFragment == null) {
            currentFragment = getFragmentByTag(tag).apply { transaction.add(R.id.fragmentContainer, this, tag) }
        }
        transaction.commit()
    }

    @Suppress("IfThenToElvis")
    private fun getFragmentByTag(tag: String): Fragment {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return if (fragment != null) fragment else {
            when (tag) {
                TAG_HOME -> HomeFragment.newInstance()
                TAG_SQUARE -> SquareFragment.newInstance()
                TAG_MESSAGE -> MessageFragment.newInstance()
                else -> MineFragment.newInstance()
            }
        }
    }

    private fun getCurrentFragmentIndex(): String {
        return when (currentFragment) {
            is HomeFragment -> TAG_HOME
            is SquareFragment -> TAG_SQUARE
            is MessageFragment -> TAG_MESSAGE
            is MineFragment -> TAG_MINE
            else -> TAG_HOME
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TAG, getCurrentFragmentIndex())
    }

    private fun checkPrivacyLock() {
        if (userAccessor.isLocked()) {
            startActivity<PrivacyActivity>(Page.LOCK, Bundle().apply {
                putInt(Constants.Extra.THEME, R.style.AppTheme_Light)
            })
            finish()
        }
    }

}
