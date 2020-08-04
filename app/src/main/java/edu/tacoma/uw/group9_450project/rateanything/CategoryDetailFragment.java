package edu.tacoma.uw.group9_450project.rateanything;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import edu.tacoma.uw.group9_450project.rateanything.model.Category;

/**
 * A fragment representing a single Category detail screen.
 * This fragment is either contained in a {@link CategoryListActivity}
 * in two-pane mode (on tablets) or a {@link CategoryDetailActivity}
 * on handsets.
 */
public class CategoryDetailFragment extends Fragment {

    /** The fragment argument representing the item ID that this fragment represents. */
    public static final String ARG_ITEM_ID = "item_id";

    /** The category content this fragment is presenting. */
    private Category mCategory;

    /** Constant for use in alignment of fragment layout */
    private static final int BOTTOM_MARGIN = 150;
    private static final String NEEDED_CONTENT = "PLACE ITEM LIST HERE";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryDetailFragment() {}


    /**
     * Mandatory method. The method loads the category content as well as setting the placing
     * the long description of the category into the appbar.
     * @param savedInstanceState a Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the category content specified by the fragment arguments
            mCategory = (Category) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mCategory.getMyCategoryName());
                appBarLayout.setExpandedTitleMarginBottom(BOTTOM_MARGIN);
                TextView textView = (TextView) activity.findViewById(R.id.long_detail_for_display);
                textView.setText(mCategory.getMyCategoryLongDesc());

            }
        }
    }


    /**
     * An override method that places a placeholder into the field below the appbar. Will
     * likely be used later to show items with the categories.
     * @param inflater a LayoutInflater
     * @param container a ViewGroup
     * @param savedInstanceState a Bundle
     * @return a View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_detail, container, false);

        if (mCategory != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_long_desc))
                    .setText(NEEDED_CONTENT);
        }
        return rootView;
    }
}