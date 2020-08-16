package edu.tacoma.uw.group9_450project.rateanything;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.tacoma.uw.group9_450project.rateanything.model.ItemRating;

/**
 * A fragment that holds the list of ratings of an item.
 * @author Rich W.
 */
public class RatingDetailFragment extends Fragment {

    ArrayList<ItemRating> myRatings;
    private RecyclerView mRecyclerView;
    private static final String RATING_LIST = "Rating List Activity";


    public RatingDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        myRatings = (ArrayList<ItemRating>) bundle.getSerializable(RATING_LIST);
        Log.i("RatingDetFrag", myRatings.get(0).getMyRating());
        Log.i("RatingDetFrag", myRatings.get(0).getMyRatingDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_rating_detail,
                container, false);


        mRecyclerView = view.findViewById(R.id.rating_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        assert mRecyclerView != null;
        if (myRatings != null) {
            mRecyclerView.setAdapter(new SimpleRatingRecyclerViewAdapter(myRatings));
        }
        return view;
    }


    public static class SimpleRatingRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleRatingRecyclerViewAdapter.RecyclerViewHolder> {
        private final ArrayList<ItemRating> myRatingsList;

        public SimpleRatingRecyclerViewAdapter(ArrayList<ItemRating> ratingsList) {
            myRatingsList = ratingsList;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_rating_layout, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            if (!myRatingsList.get(position).getMyOwnerFlag()) {
                holder.myRatingOwnerName.setText((myRatingsList.get(position).getMyRatingOwnerUsername()));
            }
            holder.myRatingValue.setText((myRatingsList.get(position).getMyRating()));
            holder.myCommentView.setText((myRatingsList.get(position).getMyRatingDescription()));
        }

        @Override
        public int getItemCount() {
            return myRatingsList.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            final TextView myRatingOwnerName;
            final TextView myRatingValue;
            final TextView myCommentView;
            public RecyclerViewHolder (@NonNull View ratingView) {
                super(ratingView);
                myRatingOwnerName = (TextView) ratingView.findViewById(R.id.rating_author);
                myRatingValue = (TextView) ratingView.findViewById(R.id.rating_value);
                myCommentView = (TextView) ratingView.findViewById(R.id.rating_comment);
            }
        }

    }

}