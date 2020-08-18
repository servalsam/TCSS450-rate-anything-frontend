package edu.tacoma.uw.group9_450project.rateanything;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import edu.tacoma.uw.group9_450project.rateanything.model.ItemRating;


public class ItemRatingTest {

    /** Fields to use in testing. */
    ItemRating itemRatingSample;
    List<ItemRating> myItemRatingList;
    JSONObject myJSONList;

    /** Constants used in testing. */
    private static final String ITEM_ID = "item_id";
    private static final String RATING_OWNER_ID = "member_id";
    private static final String RATING_OWNER_NAME = "username";
    private static final String RATING_COMMENT = "rating_description";
    private static final String RATING = "rating";
    private static final String SHOW_OWNER_NAME = "is_anonymous";

    private static final String GOOD_ITEM_ID = "1";
    private static final String GOOD_RATING_OWNER_ID = "2";
    private static final String GOOD_RATING_OWNER_USERNAME = "testname";
    private static final String GOOD_RATING_COMMENT = "This item is really good, but not the best.";
    private static final String GOOD_RATING = "4.5";
    private static final Boolean GOOD_OWNER_SHOW_NAME_FLAG = true;
    private static final String BAD_ID = "badID_001";
    private static final float EXPECTED_AVERAGE = 2.75f;
    private static final String CATEGORIES = "categories";
    private static final String JSON_STRING =
            "[{\"item_id\": \"1\"," +
                    "\"member_id\": \"2\"," +
                    "\"username\": \"username2\"," +
                    "\"rating_description\": \"Comments by user 1\"," +
                    "\"rating\": \"0.5\"," +
                    "\"is_anonymous\": true}," +
             "{\"item_id\": \"1\"," +
                    "\"member_id\": \"3\"," +
                    "\"username\": \"username3\"," +
                    "\"rating_description\": \"Comments by user 2\"," +
                    "\"rating\": \"1.0\"," +
                    "\"is_anonymous\": true}," +
             "{\"item_id\": \"1\"," +
                    "\"member_id\": \"4\"," +
                    "\"username\": \"username4\"," +
                    "\"rating_description\": \"Comments by user 3\"," +
                    "\"rating\": \"1.5\"," +
                    "\"is_anonymous\": true}," +
             "{\"item_id\": \"1\"," +
                    "\"member_id\": \"5\"," +
                    "\"username\": \"username5\"," +
                    "\"rating_description\": \"Comments by user 4\"," +
                    "\"rating\": \"2.0\"," +
                    "\"is_anonymous\": true}," +
              "{\"item_id\": \"1\"," +
                    "\"member_id\": \"6\"," +
                    "\"username\": \"username6\"," +
                    "\"rating_description\": \"Comments by user 5\"," +
                    "\"rating\": \"2.5\"," +
                    "\"is_anonymous\": true}]";


    /** Good ItemRating Object. */
    private void setTestItemRating() {
        itemRatingSample = new ItemRating
                (GOOD_ITEM_ID,
                GOOD_RATING_OWNER_ID,
                GOOD_RATING_OWNER_USERNAME,
                GOOD_RATING_COMMENT,
                GOOD_RATING,
                GOOD_OWNER_SHOW_NAME_FLAG);
    }

    /** Method to generate a list of ItemRating Objects. */
    private void generateItemRatingList() {
        myItemRatingList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            String tempUserID = ("" + (i + 1));
            String tempUserName = (RATING_OWNER_NAME + (i + 1));
            String tempUserComment = ("Comments by user "+ i);
            float rating = i / 2f;
            String tempRating = String.valueOf(rating);
            ItemRating tempItemRating = new ItemRating(
                    GOOD_ITEM_ID,
                    tempUserID,
                    tempUserName,
                    tempUserComment,
                    tempRating,
                    GOOD_OWNER_SHOW_NAME_FLAG );
            myItemRatingList.add(tempItemRating);
        }
    }

    private void generateJSONList() {
        myJSONList = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i = 1; i <= 10; i++) {
            JSONObject ratingJSON = new JSONObject();
            String tempUserID = ("" + (i + 1));
            String tempUserName = (RATING_OWNER_NAME + i);
            String tempUserComment = "Item " + i + " comment";
            float rating = i / 2f;
            String tempRating = String.valueOf(rating);

            try {
                ratingJSON.put(ITEM_ID, GOOD_ITEM_ID);
                ratingJSON.put(RATING_OWNER_ID, tempUserID);
                ratingJSON.put(RATING_OWNER_NAME, tempUserName);
                ratingJSON.put(RATING_COMMENT, tempUserComment);
                ratingJSON.put(RATING, tempRating);
                ratingJSON.put(SHOW_OWNER_NAME, GOOD_OWNER_SHOW_NAME_FLAG);

            } catch (JSONException ignored) {}
            jsonArray.put(ratingJSON);
        }
        try {
            myJSONList.put(CATEGORIES, jsonArray);
        } catch (JSONException ignored) {}
    }


    /**
     * Test for non null ItemRating creation.
     */
    @Test
    public void testItemRatingConstructor() {
        setTestItemRating();
        assertNotNull(itemRatingSample);
    }

    /**
     * Test for correct catch of null being passed to constructor.
     */
    @Test
    public void testNullCatchItemRatingConstructor() {
        try {
            new ItemRating(null, null, null,
                    null, null, null);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBadIDCatchInConstructor() {
        try {
            new ItemRating(BAD_ID, BAD_ID, GOOD_RATING_OWNER_USERNAME,
                           GOOD_RATING_COMMENT, GOOD_RATING, GOOD_OWNER_SHOW_NAME_FLAG);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void assertTrueCheckForValidID() {
        assertTrue(ItemRating.checkForValidID(GOOD_ITEM_ID));
    }

    @Test
    public void assertFalseCheckForValidID() {
        assertFalse(ItemRating.checkForValidID(BAD_ID));
    }

    @Test
    public void checkForNullCatchCheckForValidID() {
        try  {
            ItemRating.checkForValidID(null);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testItemRatingGetters() {
        setTestItemRating();
        assertEquals(itemRatingSample.getMyItemId(), GOOD_ITEM_ID);
        assertEquals(itemRatingSample.getMyRatingOwner(), GOOD_RATING_OWNER_ID);
        assertEquals(itemRatingSample.getMyRatingOwnerUsername(), GOOD_RATING_OWNER_USERNAME);
        assertEquals(itemRatingSample.getMyRatingDescription(), GOOD_RATING_COMMENT);
        assertEquals(itemRatingSample.getMyRating(), GOOD_RATING);
        assertEquals(itemRatingSample.getMyOwnerFlag(), GOOD_OWNER_SHOW_NAME_FLAG);
    }

    /**
     * Tests the expected average rating returned from from mItemRatingList which contains a list
     * of ItemRating objects. The expected result in this case is 2.75 which is the average of:
     * 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5 and 5.0
     */
    @Test
    public void testFindAverageFromList() {
        generateItemRatingList();
        float value = ItemRating.findAvgRatingFromList(myItemRatingList);
        assertTrue(value == EXPECTED_AVERAGE);
    }

    /**
     * Tests the expected star image returned from the average ratings from myItemRatingList.
     * In this case the star image should have 2.5 stars (or R.drawable.star_5).
     */
    @Test
    public void testGetItemImage() {
        generateItemRatingList();
        assertEquals(
                ItemRating.getRatingImage(ItemRating.findAvgRatingFromList(myItemRatingList)),
                R.drawable.star_5);
    }

    /**
     * Test to verify the expected output from the static method.
     * @throws JSONException e
     */
    @Test
    public void testParseJSONRatings() throws JSONException {
        generateItemRatingList();
        generateJSONList();
        List<ItemRating> returnedList = ItemRating.parseRatingJson(JSON_STRING);
        for (int i = 0; i < returnedList.size(); i++) {
            assertEquals(myItemRatingList.get(i).getMyItemId(),
                    returnedList.get(i).getMyItemId());
            assertEquals(myItemRatingList.get(i).getMyRatingOwner(),
                    returnedList.get(i).getMyRatingOwner());
            assertEquals(myItemRatingList.get(i).getMyRatingOwnerUsername(),
                    returnedList.get(i).getMyRatingOwnerUsername());
            assertEquals(myItemRatingList.get(i).getMyRatingDescription(),
                    returnedList.get(i).getMyRatingDescription());
            assertEquals(myItemRatingList.get(i).getMyRating(),
                    returnedList.get(i).getMyRating());
            assertEquals(myItemRatingList.get(i).getMyOwnerFlag(),
                    returnedList.get(i).getMyOwnerFlag());
        }
    }
}
