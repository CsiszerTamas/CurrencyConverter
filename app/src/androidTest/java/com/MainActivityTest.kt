package com

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cst.currencyconverter.MainActivity
import com.cst.currencyconverter.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testActivityInView() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main))
            .check(matches(isDisplayed()))
    }
}
