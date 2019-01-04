package com.snt.phoney.ui.privacy

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.news.NewsActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.data.MD5
import kotlinx.android.synthetic.main.fragment_lock.*
import javax.inject.Inject


/**
 */
class LockFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @Inject
    lateinit var userAccessor: UserAccessor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = userAccessor.getUser()
        user?.let { user ->
            name.text = user.nickname
            Glide.with(this).load(user.avatar)
                    .apply(RequestOptions()
                            .circleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(icon)
            return@let
        }

        inputPassword.setOnInputOverListener {
            val pwd = inputPassword.password.toString()
            if (!TextUtils.isEmpty(pwd)) {
                //if (userAccessor.tryUnlock(MD5.md5(pwd))) {
                if (userAccessor.tryUnlock(MD5.md5(StringBuffer(pwd).reverse().toString()))) {
                    startActivity(MainActivity.newIntent(requireContext()))
                    activity?.finish()
                } else {
                    inputPassword.clear()
                    playJumpAnimation()
                    stateView.setText(R.string.input_password_error)
                    startActivity<NewsActivity>(Page.NEWS)
                }
            }
        }

    }

    private fun playJumpAnimation() {
        val downAnimator = ObjectAnimator.ofFloat(stateView, "translationX", 0f, dip(10).toFloat())
        val set = AnimatorSet()
        set.playSequentially(downAnimator)
        set.interpolator = CycleInterpolator(2f)
        set.duration = 300
        set.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = LockFragment().apply {
            this.arguments = arguments
        }
    }
}
