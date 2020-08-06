package edu.tacoma.uw.group9_450project.rateanything;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import edu.tacoma.uw.group9_450project.rateanything.model.Item;

/**
 * Class used to display details about an item.
 * @author Rich W.
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

    /**
     * Method instantiates the mItem.
     * @param savedInstanceState a Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        mItem = (Item) getArguments().getSerializable(ARG_ITEM_ID);

    }

    /**
     * Method fills the TextViews with details about the item.
     * @param inflater a LayoutInflater
     * @param container a ViewGroup
     * @param savedInstanceState a Bundle
     * @return a View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.long_item_detail_fragment_display))
                    .setText(mItem.getMyItemLongDesc());

            ((TextView) rootView.findViewById(R.id.item_detail_long_desc_item_detail_frag))
                    .setText(NEEDED_CONTENT);
        }
        return rootView;
    }
}