package edu.tacoma.uw.group9_450project.rateanything.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class implements serializable so that it can be passed as an argument. Item
 * is a data structure to hold the contents of item stored on the WebService.
 * @author Rich (code structure supplied by UWT TCSS 450 Instructor)
 * @version August 2020
 */
public class Item implements Serializable {

    /** Member variables and constants. */
    private String myItemID;
    private String myCategory;
    private String myItemName;
    private String myItemLongDesc;
    private String myItemShortDesc;
    private String myItemOwner; // Might be used in future feature implementation
    private int myItemRating;
    private String myMemberID;
    private ArrayList<ItemRating> myRatings;


    /** Constants should have the same names as the columns in the table on the database */
    public static final String ID = "item_id";
    public static final String CATEGORY = "category_id";
    public static final String NAME = "item_name";
    public static final String LONG_DESC = "item_description_long";
    public static final String SHORT_DESC = "item_description_short";
    public static final String RATING = "rating";
    public static final String MEMBER = "member_id";

    /**
     * public constructor for the Item class.
     * @param theItemID a string holding the item id.
     * @param theCategory a string holding the category from which it comes.
     * @param theItemName a string holding the name of the item.
     * @param theItemLongDesc a string holding a long description of the item.
     * @param theItemShortDesc a string holding a short description of the item.
     * @param theItemRating an int holding the rating of the item.
     * @param theMemberID a string holding the member ID of the item.
     */
    public Item(String theItemID, String theCategory, String theItemName, String theItemLongDesc,
                String theItemShortDesc, int theItemRating, String theMemberID) {
        this.myItemID = theItemID;
        this.myCategory = theCategory;
        this.myItemName = theItemName;
        this.myItemLongDesc = theItemLongDesc;
        this.myItemShortDesc = theItemShortDesc;
        this.myItemRating = theItemRating;
        this.myMemberID = theMemberID;

        myRatings = new ArrayList<>();
    }

    /** getters */
    public String getMyItemID() {
        return myItemID;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public String getMyItemName() {
        return myItemName;
    }

    public String getMyItemLongDesc() {
        return myItemLongDesc;
    }

    public String getMyItemShortDesc() {
        return myItemShortDesc;
    }

    public String getMyItemOwner() {
        return myItemOwner;
    }

    public int getMyItemRating() {
        return myItemRating;
    }

    public String getMyMemberID() {
        return myMemberID;
    }

    public ArrayList<ItemRating> getMyRatings() {
        return myRatings;
    }

    /**
     * A method to add a ratings to the list
     * @param rating an ItemRating used to add to the rating list
     */
    public void addToRatingList(ItemRating rating) {
        myRatings.add(rating);
    }

    /**
     * A method that allows the conversion of a JSON string into a list of item objects.
     * @param itemJson a String
     * @return itemList a list of items
     * @throws JSONException The JSON Object should contain a array list of of items.
     */
    public static List<Item> parseItemJson(String itemJson) throws JSONException {
        List<Item> itemList = new ArrayList<>();
        if (itemJson != null) {

            JSONArray arr = new JSONArray(itemJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Item item = new Item (
                        obj.getString(Item.ID),
                        obj.getString(Item.CATEGORY),
                        obj.getString(Item.NAME),
                        obj.getString(Item.LONG_DESC),
                        obj.getString(Item.SHORT_DESC),
                        obj.getInt(Item.RATING),
                        obj.getString(Item.MEMBER));
                itemList.add(item);
            }
        }
        return itemList;
    }
}