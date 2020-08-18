package edu.tacoma.uw.group9_450project.rateanything.model;

import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.group9_450project.rateanything.R;


/**
 * This class is object that holds the item rating of an item object. It also contains
 * methods to analyze a list of item ratings as well as parsing a JSON object to create
 * a list of item ratings.
 * @author Rich W.
 * @version August 2020
 */
public class ItemRating implements Serializable {

    /** Members */
    private String myItemId;
    private String myRatingOwner;
    private String myRatingOwnerUsername;
    private String myRatingDescription;
    private String myRating;
    private Boolean willNameBeHidden;

    /** Constants */
    private static final String ITEM_ID = "item_id";
    private static final String RATING_OWNER = "member_id";
    private static final String OWNER_NAME = "username";
    private static final String RATING_DESC = "rating_description";
    private static final String RATING = "rating";
    private static final String SHOW_OWNER_NAME = "is_anonymous";

    /** regex statement */
    static String regex = "\\d+";

    /**
     * Constructor for ItemRating
     */
    public ItemRating(String theItemID, String theRatingOwner, String theOwnerName,
                      String theRatingDescription, String theRating, Boolean hiddenNameFlag) {

        if (theItemID == null | theRatingOwner == null |
                theOwnerName == null | theRatingDescription == null |
                theRating == null | hiddenNameFlag == null) {
            throw new IllegalArgumentException("Arguments may not be null");
        } else {
            if (checkForValidID(theItemID) || checkForValidID(theRatingOwner)) {
                myItemId = theItemID;
                myRatingOwner = theRatingOwner;
                myRatingOwnerUsername = theOwnerName;
                myRatingDescription = theRatingDescription;
                myRating = theRating; // alternate code --> Float.parseFloat(theRating);
                willNameBeHidden = hiddenNameFlag;
            } else {
                throw new IllegalArgumentException("Item ID and Member ID may only be digits");
            }
        }
    }

    /** Getters */
    public String getMyItemId() {
        return myItemId;
    }

    public String getMyRatingOwner() {
        return myRatingOwner;
    }

    public String getMyRatingOwnerUsername() {
        return myRatingOwnerUsername;
    }

    public String getMyRatingDescription() {
        return myRatingDescription;
    }

    public String getMyRating() {
        return myRating;
    }

    public Boolean getMyOwnerFlag() {
        return willNameBeHidden;
    }


    /**
     * Check for validity of ID. I.e. only digits are allowed for id's.
     * @param id a string holding an ID
     * @return a boolean validating (or invalidating) the use of the string as an ID.
     */
    public static boolean checkForValidID(String id) {
        if (id != null) {
            return id.matches(regex);
        } else {
            throw new IllegalArgumentException ("Argument may not be null");
        }
    }


    /**
     * This method returns the average rating of all item ratings.
     * @param itemRatings a List of ItemRatings
     * @return a float that holds the average rating.
     * @author Rich W.
     */
    public static float findAvgRatingFromList(List<ItemRating> itemRatings) {
        if (itemRatings == null) {
            throw new IllegalArgumentException("List of ItemRatings may not be null");
        } else {
            float sum = 0.0f;
            float avg = 0.0f;
            int count = 0;
            for (ItemRating itemRating : itemRatings) {
                float num = Float.parseFloat(itemRating.getMyRating());
                sum += num;
                count += 1;
            }
            if (count > 0) {
                avg = sum / count;
            }
            return avg;
        }
    }

    /**
     * Static method to return the proper drawable depending upon the rating value.
     * @param avg a float holding the value of the rating.
     * @return The int representing the R value for the drawable to be used.
     */
    public static int getRatingImage(float avg) {
        if (avg < 1.0) {
            return R.drawable.star_1;
        } else if (avg >= 1.0 && avg < 1.5) {
            return R.drawable.star_2;
        } else if (avg >= 1.5 && avg < 2.0) {
            return R.drawable.star_3;
        } else if (avg >= 2.0 && avg < 2.5) {
            return R.drawable.star_4;
        } else if (avg >= 2.5 && avg < 3.0) {
            return R.drawable.star_5;
        } else if (avg >= 3.0 && avg < 3.5) {
            return R.drawable.star_6;
        } else if (avg >= 3.5 && avg < 4.0) {
            return R.drawable.star_7;
        } else if (avg >= 4.0 && avg < 4.5) {
            return R.drawable.star_8;
        } else if (avg >= 4.5 && avg < 4.7) {
            return R.drawable.star_9;
        }

        return R.drawable.star_10;
    }

    /**
     * A method that allows the conversion of a JSON string into a list of rating objects.
     * @param ratingsJson a String
     * @return ratingsList a list of ratings
     * @throws JSONException The JSON Object should contain a array list of of ratings.
     */
    public static List<ItemRating> parseRatingJson(String ratingsJson) throws JSONException {
        List<ItemRating> itemRatings = new ArrayList<>();
        if (ratingsJson != null) {
            try {
                JSONArray arr = new JSONArray(ratingsJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    ItemRating rating = new ItemRating (
                            obj.getString(ItemRating.ITEM_ID),
                            obj.getString(ItemRating.RATING_OWNER),
                            obj.getString(ItemRating.OWNER_NAME),
                            obj.getString(ItemRating.RATING_DESC),
                            obj.getString(ItemRating.RATING),
                            obj.getBoolean(ItemRating.SHOW_OWNER_NAME));
                    itemRatings.add(rating);
                }
            } catch (JSONException e) {
                throw new JSONException("String holding JSON of ratings could not be parsed" + e);
            }
        }
        return itemRatings;
    }
}