package com.greatergoodguy.stellarseattle

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.greatergoodguy.stellarseattle.presentation.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun verifyVenuesListIsShownWhenApiIsSuccessful() {
        onView(withId(R.id.inputField)).perform(ViewActions.typeText("Pizza"))
        onView(withId(R.id.searchButton)).perform(ViewActions.click())

        // Verify UI State when API call is running
        onView(withId(R.id.inputField)).check(matches(not(isEnabled())))
        onView(withId(R.id.searchButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())))

        Thread.sleep(5000)

        // Verify UI State when API call is finished
        onView(withId(R.id.inputField)).check(matches(isEnabled()))
        onView(withId(R.id.searchButton)).check(matches(isEnabled()))
        onView(withId(R.id.spinner)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        onView(withId(R.id.tvErrorMessage)).check(matches(not(isDisplayed())))

    }
}
