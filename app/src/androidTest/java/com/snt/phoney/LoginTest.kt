package com.snt.phoney

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.rule.ActivityTestRule
import com.snt.phoney.ui.signup.SignupActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 21)
@LargeTest
class LoginTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<SignupActivity> = ActivityTestRule<SignupActivity>(SignupActivity::class.java)

    private var mActivity: SignupActivity? = null

    @Before
    fun startSignupActivity() {
        mActivity = mActivityRule.activity
    }

//    @Test
//    fun testLogin() {
//        onView(withId(R.id.signin)).perform(click())
//        onView(withId(R.id.phone)).perform(typeText("13419517416"))
//        onView(withId(R.id.getVerificationCode)).perform(click())
//    }

    @Test
    fun testQQLogin() {
        onView(withId(R.id.qq)).perform(click())
    }
}
