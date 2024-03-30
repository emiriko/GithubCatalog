package com.example.githubcatalog.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.githubcatalog.R
import org.junit.Test

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }
    
    @Test
    fun testNavigation(){
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.navigation_favorites)).perform(click())
        onView(withId(R.id.navigation_settings)).perform(click())
    }
    
    @Test
    fun changeTheme(){
        onView(withId(R.id.navigation_settings)).perform(click())
        
        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_theme)).perform(click())
    }
}