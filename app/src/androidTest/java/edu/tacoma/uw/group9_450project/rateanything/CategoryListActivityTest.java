package edu.tacoma.uw.group9_450project.rateanything;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class CategoryListActivityTest {

    /** Constants */
    private static final String ABOUT_TITLE = "About";
    private static final String ITEM_TEXT = "Gene Coulon Memorial Beach Park";
    private static final String ADD_CATEGORY = "Add a Category";
    private static final String REGISTER = "Register";
    private static final String FIELDS_EMPTY = "Fields may not be empty";
    private static final String PLACES = "Places";


    @Rule
    public ActivityTestRule<CategoryListActivity> mActivityRule =
            new ActivityTestRule<>(CategoryListActivity.class);

    @Test
    public void displayRecyclerView() {
        // First, scroll to the position that needs to matched
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        // Match the text in an item and check that it's displayed
        onView(withText(ITEM_TEXT)).check(matches(isDisplayed()));
    }


    @Test
    public void floatingActionButtonWorks() {
        onView(withId(R.id.fab)).perform(click());
        onView(withText(ADD_CATEGORY)).check(matches(isDisplayed()));
    }


    @Test
    public void aboutBoxWorks() {
        onView(withId(R.id.action_about)).perform(click());
        onView(withText(ABOUT_TITLE)).check(matches(isDisplayed()));
    }


    @Test
    public void testAddCategoryMenuButtonWorks() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(Matchers.anyOf(withText(R.string.action_add_category), withId(R.id.action_add_category))).perform(click());
        onView(withText(ADD_CATEGORY)).check(matches(isDisplayed()));
    }


    @Test
    public void testSyncWithDataBaseWorks() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(Matchers.anyOf(withText(R.string.sync_list), withId(R.id.action_sync_category))).perform(click());
        onView(withText(PLACES)).check(matches(isDisplayed()));
    }


    @Test
    public void testLogOutWorks() {
        onView(withId(R.id.action_logout)).perform(click());
        onView(withText(REGISTER)).check(matches(isDisplayed()));
    }


}
