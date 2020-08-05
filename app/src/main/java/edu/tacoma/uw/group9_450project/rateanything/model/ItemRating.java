package edu.tacoma.uw.group9_450project.rateanything.model;

import java.io.Serializable;

public class ItemRating implements Serializable {

    /** Members */
    private String myItemName;
    private String myItemID;
    private String myOwner;
    private String myRating;
    private String myComments;

    /**
     * Constructor for ItemRating
     */
    public void ItemRating(String theItemID, String theItemName, String theItemOwner,
                           String theRating, String theComments) {
        myItemID = theItemID;
        myItemName = theItemName;
        myOwner = theItemOwner;
        myRating = theRating;
        myComments = theComments;
    }

    /** Getters and Setters */
    public String getMyItemName() {
        return myItemName;
    }

    public void setMyItemName(String myItemName) {
        this.myItemName = myItemName;
    }

    public String getMyItemID() {
        return myItemID;
    }

    public void setMyItemID(String myItemID) {
        this.myItemID = myItemID;
    }

    public String getMyOwner() {
        return myOwner;
    }

    public void setMyOwner(String myOwner) {
        this.myOwner = myOwner;
    }

    public String getMyRating() {
        return myRating;
    }

    public void setMyRating(String myRating) {
        this.myRating = myRating;
    }

    public String getMyComments() {
        return myComments;
    }

    public void setMyComments(String myComments) {
        this.myComments = myComments;
    }

    /**
     //     * A method that allows the conversion of a JSON string into a list of category objects.
     //     * @param categoryJson a String
     //     * @return categoryList a list of categories
     //     * @throws JSONException The JSON Object should contain a array list of of categories.
     //     */
//    public static List<Category> parseCategoryJson(String categoryJson) throws JSONException {
//        List<Category> categoryList = new ArrayList<>();
//        if (categoryJson != null) {
//
//            JSONArray arr = new JSONArray(categoryJson);
//
//            for (int i = 0; i < arr.length(); i++) {
//                JSONObject obj = arr.getJSONObject(i);
//                Category category = new Category (obj.getString(Category.ID),
//                                                  obj.getString(Category.NAME),
//                                                  obj.getString(Category.LONG_DESC),
//                                                  obj.getString(Category.SHORT_DESC));
//                categoryList.add(category);
//            }
//        }
//        return categoryList;
//    }
}
