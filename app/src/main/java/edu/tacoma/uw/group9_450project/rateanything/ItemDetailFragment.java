package edu.tacoma.uw.group9_450project.rateanything;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import javax.xml.datatype.XMLGregorianCalendar;

import edu.tacoma.uw.group9_450project.rateanything.model.Item;

/**

 * .
 */
public class ItemDetailFragment extends Fragment {

    /** The fragment argument representing the item ID that this fragment represents.*/
    public static final String ARG_ITEM_ID = "item_id";

    /** The item content this fragment is presenting */
    private Item mItem;

    /** Constants for use in alignment of fragment layout */
    private static final int BOTTOM_MARGIN = 150;
    private static final String NEEDED_CONTENT = "Place Ratings Here";

    /**
     * Mandatory empty constructor.
     */
    public ItemDetailFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = (Item) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout_item_detail_activity);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getMyItemName());
                appBarLayout.setExpandedTitleMarginBottom(BOTTOM_MARGIN);
                TextView textView = (TextView) activity.
                        findViewById(R.id.long_item_detail_for_display_activity_item_detail);
                textView.setText(mItem.getMyItemLongDesc());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_long_desc_item_detail_frag))
                    .setText(NEEDED_CONTENT);
        }
        return rootView;
    }
}