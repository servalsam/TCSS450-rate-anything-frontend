package edu.tacoma.uw.group9_450project.rateanything.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class used to manage a collection of categories.
 * @author Rich (code base supplied by instructor of UWT TCSS 450 Class)
 * @version July 2020
 */
public class CategoryContent {

    /** An array of categories. */
    public static final List<Category> ITEMS = new ArrayList<>();

    /** A map of category items by ID. */
    public static final Map<String, Category> ITEM_MAP = new HashMap<>();

    /** constant */
    private static final int COUNT = 25;

    /**
     *  Static method to fill the map
     */
    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createCategoryItem(i));
        }
    }

    private static void addItem(Category item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getCategoryID(), item);
    }

    private static Category createCategoryItem(int position) {
        return new Category("Id" + position, "Name" + position,
                "Short desc"+ position, "Long desc" + position);
    }
}
