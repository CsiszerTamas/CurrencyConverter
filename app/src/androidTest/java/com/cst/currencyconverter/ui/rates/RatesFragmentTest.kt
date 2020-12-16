package com.cst.currencyconverter.ui.rates

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.cst.currencyconverter.MainActivity
import com.cst.currencyconverter.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@MediumTest
class RatesFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
    Testing if currency Recyclerview comes into view
     */
    @Test
    fun testIsListFragmentVisibleOnAppLaunch() {
        onView(withId(R.id.rates_recycler_view))
            .check(matches(isDisplayed()))
    }
}
