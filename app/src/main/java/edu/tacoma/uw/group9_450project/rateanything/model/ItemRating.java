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
    private String myRatingDescription;
    private String myRating;

    /** Constants */
    private static final String ITEM_ID = "item_id";
    private static final String RATING_OWNER = "member_id";
    private static final String RATING_DESC = "rating_description";
    private static final String RATING = "rating";

    /**
     * Constructor for ItemRating
     */
    public ItemRating(String theItemID, String theRatingOwner,
                      String theRatingDescription, String theRating) {
        myItemId = theItemID;
        myRatingOwner = theRatingOwner;
        myRatingDescription = theRatingDescription;
        myRating = theRating; //Float.parseFloat(theRating);
    }

    /** Getters and Setters */
    public String getMyItemId() {
        return myItemId;
    }

    public void setMyItemId(String myItemId) {
        this.myItemId = myItemId;
    }

    public String getMyRatingOwner() {
        return myRatingOwner;
    }

    public void setMyRatingOwner(String myRatingOwner) {
        this.myRatingOwner = myRatingOwner;
    }

    public String getMyRatingDesciption() {
        return myRatingDescription;
    }

    public void setMyRatingDesciption(String myRatingDesciption) {
        this.myRatingDescription = myRatingDesciption;
    }

    public String getMyRating() {
        return myRating;
    }

    public void setMyRating(String myRating) {
        this.myRating = myRating;
    }

    /**
     * This method returns the average rating of all item ratings.
     * @param itemRatings a List of ItemRatings
     * @return a float that holds the average rating.
     * @author Rich W.
     */
    public static float findAvgRatingFromList(List<ItemRating> itemRatings) {
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

    /**
     *
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

            JSONArray arr = new JSONArray(ratingsJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                ItemRating rating = new ItemRating (obj.getString(ItemRating.ITEM_ID),
                        obj.getString(ItemRating.RATING_OWNER),
                        obj.getString(ItemRating.RATING_DESC),
                        obj.getString(ItemRating.RATING));
                itemRatings.add(rating);
            }
        }
        return itemRatings;
    }
}