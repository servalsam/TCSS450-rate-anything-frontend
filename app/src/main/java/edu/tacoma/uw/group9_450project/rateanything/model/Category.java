package edu.tacoma.uw.group9_450project.rateanything.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements serializable so that it can be passed as an argument. Category
 * is a data structure to hold the contents of categories stored on the WebService.
 * @author Rich (code structure supplied by UWT TCSS 450 Instructor)
 * @version July 2020
 */
public class Category implements Serializable {

    /** Member variables and constants. */
    private String myCategoryID;
    private String myCategoryName;
    private String myCategoryShortDesc;
    private String myCategoryLongDesc;

    /** Constants should have the same names as the columns in the table on the database */
    public static final String ID = "category_id";
    public static final String NAME = "category_name";
    public static final String SHORT_DESC = "category_description_short";
    public static final String LONG_DESC = "category_description_long";

    /**
     * Public constructor for Category Class
     * @param theId The ID for the category.
     * @param theName The name of the category.
     * @param theShortDesc The short description of the category.
     * @param theLongDesc The long description of the category.
     */
    public Category(String theId, String theName, String theLongDesc, String theShortDesc) {
        this.myCategoryID = theId;
        this.myCategoryName = theName;
        this.myCategoryShortDesc = theShortDesc;
        this.myCategoryLongDesc = theLongDesc;
    }

    /**
     * Getters and Setters for the Category Class.
     */
    public String getCategoryID() { return myCategoryID;}

    public String getMyCategoryName() { return myCategoryName;}

    public String getMyCategoryShortDesc() { return myCategoryShortDesc;}

    public String getMyCategoryLongDesc() { return myCategoryLongDesc;}

    public void setCourseId(String theId) {this.myCategoryID = theId;}

    public void setMyCategoryName(String theName) {this.myCategoryName = theName;}

    public void setMyCategoryShortDesc(String theShort) {this.myCategoryShortDesc = theShort;}

    public void setMyCategoryLongDesc(String theLong) {this.myCategoryLongDesc = theLong;}


    /**
     * A method that allows the conversion of a JSON string into a list of category objects.
     * @param categoryJson a String
     * @return categoryList a list of categories
     * @throws JSONException The JSON Object should contain a array list of of categories.
     */
    public static List<Category> parseCategoryJson(String categoryJson) throws JSONException {
        List<Category> categoryList = new ArrayList<>();
        if (categoryJson != null) {

            JSONArray arr = new JSONArray(categoryJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Category category = new Category (obj.getString(Category.ID),
                                                  obj.getString(Category.NAME),
                                                  obj.getString(Category.LONG_DESC),
                                                  obj.getString(Category.SHORT_DESC));
                categoryList.add(category);
            }
        }
        return categoryList;
    }
}
