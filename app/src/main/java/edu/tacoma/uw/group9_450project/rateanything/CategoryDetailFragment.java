package edu.tacoma.uw.group9_450project.rateanything;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import edu.tacoma.uw.group9_450project.rateanything.model.Category;
import edu.tacoma.uw.group9_450project.rateanything.model.CategoryContent;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the course content specified by the fragment arguments
            mCategory = (Category) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(mCategory.getMyCategoryName() + ": ");
//                sb.append(mCategory.getMyCategoryLongDesc());
//                appBarLayout.setTitle(sb.toString());
                appBarLayout.setTitle(mCategory.getMyCategoryName());
                appBarLayout.setExpandedTitleMarginBottom(BOTTOM_MARGIN);
                TextView textView = (TextView) activity.findViewById(R.id.long_detail_for_display);
                textView.setText(mCategory.getMyCategoryLongDesc());

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_detail, container, false);

        if (mCategory != null) {
//            ((TextView) rootView.findViewById(R.id.item_detail_short_desc))
//                    .setText(mCategory.getMyCategoryShortDesc());
            ((TextView) rootView.findViewById(R.id.item_detail_long_desc))
                    .setText("PLACE ITEM LIST HERE");
        }
        return rootView;
    }
}