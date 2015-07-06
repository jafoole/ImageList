package com.oliverbud.android.imagelist;

import android.test.ActivityInstrumentationTestCase2;

import com.oliverbud.android.imagelist.UI.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by oliverbudiardjo on 7/6/15.
 */
public class MainEspressoTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public MainEspressoTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSearch(){


        onView(withId(R.id.search)).perform(click());

        onView(withId(R.id.search_src_text)).perform(typeText("jumper"));
        onView(withId(R.id.search_src_text)).check(matches(withText("jumper")));

    }
}
