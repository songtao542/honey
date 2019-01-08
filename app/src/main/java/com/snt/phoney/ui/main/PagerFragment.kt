package com.snt.phoney.ui.main

import android.os.Bundle
import android.util.Log
import com.snt.phoney.base.BaseFragment
import com.umeng.analytics.MobclickAgent

open class PagerFragment : BaseFragment() {

    private var isCreated = false

    override fun openUmeng(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreated = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isCreated) {
            return
        }
        Log.d("TTTT", "Fragment:${javaClass.simpleName}  visible to user:$isVisibleToUser")
        if (isVisibleToUser) {
            MobclickAgent.onPageStart(javaClass.simpleName)
        } else {
            MobclickAgent.onPageEnd(javaClass.simpleName)
        }
    }
}