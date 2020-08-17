package edu.tacoma.uw.group9_450project.rateanything;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.tacoma.uw.group9_450project.rateanything.model.Item;
import edu.tacoma.uw.group9_450project.rateanything.model.ItemRating;
import edu.tacoma.uw.group9_450project.rateanything.utils.HttpJSONTask;

/**
 * A fragment that holds the list of ratings of an item.
 * @author Rich W.
 */
public class RatingDetailFragment extends Fragment {

    /** fields */
    ArrayList<ItemRating> myRatings;
    private RecyclerView mRecyclerView;
    private String mMemberID;
    private View myEditRatingView;
    private RatingDetailFragment myRatingDetailFragment;
    private JSONObject myEditRatingJSON;
    private float myNewRating;
    private String mItemID;
    private ProgressBar mEditRatingProgressBar;
    private Item myItem;


    /** Constants */
    private static final String RATING_LIST = "Rating List Activity";
    private static final String ITEM_ID = "item_id";
    private static final String MEMBER_ID = "member_id";
    private static final String USERNAME = "username";
    private static final String RATING_COMMENT = "rating_description";
    private static final String RATING = "rating";
    private static final String POST_NAME_FLAG = "is_anonymous";
    private static final String EDIT_RATING = "Edit Your Rating";

    public RatingDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        myRatings = (ArrayList<ItemRating>) bundle.getSerializable(RATING_LIST);
        mMemberID = bundle.getString(MEMBER_ID);
        myItem = (Item) bundle.getSerializable(ITEM_ID);

        myRatingDetailFragment = this;
        myEditRatingJSON = new JSONObject();
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
            mRecyclerView.setAdapter(new SimpleRatingRecyclerViewAdapter(myRatings, mMemberID, myRatingDetailFragment));
        }
        return view;
    }

    public void editRating(final String itemID, final String memberID, final String username,
                           String comments, final String rating, Boolean flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customLayout = getLayoutInflater().inflate(R.layout.add_rating_layout, null);
        builder.setView(customLayout);
        final CheckBox postPreference = (CheckBox) customLayout.findViewById(R.id.posting_choice_rating_layout);
        final RatingBar ratingBar = (RatingBar) customLayout.findViewById(R.id.rating_value_rating_layout);
        final EditText inputComment = (EditText) customLayout.findViewById(R.id.input_rating_comments);
        TextView title = (TextView) customLayout.findViewById(R.id.add_item_dialog_title);
        title.setText(EDIT_RATING);

        Button btnAdd = (Button) customLayout.findViewById(R.id.input_rating_add_button);
        Button btnCancel = (Button) customLayout.findViewById(R.id.input_rating_cancel_button);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean postAsAnon = postPreference.isChecked();
                float newRating = ratingBar.getRating();
                String ratingComment = inputComment.getText().toString();
                String ratingString = "";

                if (!ratingComment.equals("") || newRating < 0.49) {
                    try {
                        myNewRating = newRating;
                        mItemID = itemID;
                        myEditRatingJSON.put(ITEM_ID, itemID);
                        myEditRatingJSON.put(MEMBER_ID, memberID);
                        myEditRatingJSON.put(USERNAME, username);
                        myEditRatingJSON.put(RATING_COMMENT, ratingComment);
                        myEditRatingJSON.put(RATING, String.format(ratingString.valueOf(newRating), "%.1f"));
                        myEditRatingJSON.put(POST_NAME_FLAG, postAsAnon);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Error with JSON creation on edit request: "
                                + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    new EditRatingAsyncTask().execute(getString(R.string.edit_rating));

                } else {
                    dialog.dismiss();
                    Toast.makeText(view.getContext(), "Fields cannot be empty.",
                            Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public static class SimpleRatingRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleRatingRecyclerViewAdapter.RecyclerViewHolder> {

        private final ArrayList<ItemRating> myRatingsList;
        private final String mUserID;
        private final RatingDetailFragment myFragment;

        public SimpleRatingRecyclerViewAdapter(ArrayList<ItemRating> ratingsList,
                                               String userID, RatingDetailFragment theFragment ) {
            myRatingsList = ratingsList;
            mUserID = userID;
            myFragment = theFragment;
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

            String tempText = "Rating: " + myRatingsList.get(position).getMyRating() + " / 5";
            holder.myRatingValue.setText(tempText);
            holder.myCommentView.setText((myRatingsList.get(position).getMyRatingDescription()));
            if (myRatingsList.get(position).getMyRatingOwner().equals(mUserID)) {
                final String item_ID = myRatingsList.get(position).getMyItemId();
                final String username = myRatingsList.get(position).getMyRatingOwnerUsername();
                final String comments = myRatingsList.get(position).getMyRatingDescription();
                final String rating = myRatingsList.get(position).getMyRating();
                final Boolean anonymousFlag = myRatingsList.get(position).getMyOwnerFlag();
                holder.mEditRatingButton.setVisibility(View.VISIBLE);
                holder.mEditRatingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myFragment.editRating(item_ID, mUserID, username, comments, rating, anonymousFlag);
                        Toast.makeText(view.getContext(), "Testing Edit Button", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return myRatingsList.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            final TextView myRatingOwnerName;
            final TextView myRatingValue;
            final TextView myCommentView;
            final Button mEditRatingButton;
            public RecyclerViewHolder (@NonNull View ratingView) {
                super(ratingView);
                myRatingOwnerName = (TextView) ratingView.findViewById(R.id.rating_author);
                myRatingValue = (TextView) ratingView.findViewById(R.id.rating_value);
                myCommentView = (TextView) ratingView.findViewById(R.id.rating_comment);
                mEditRatingButton = (Button) ratingView.findViewById(R.id.rating_edit_button);
            }
        }
    }

    /**
     * Private class to for asynchronous add a rating to an item.
     * Code supplied by UWT 450 Instructor. Modified by Rich W.
     */
    private class EditRatingAsyncTask extends AsyncTask<String, Void, String> {

        /**
         * Method override to start the progressbar.
         */
//        @Override
//        protected void onPreExecute() {
//            mEditRatingProgressBar.setVisibility(View.VISIBLE);
//        }

        /**
         * Method override to connect to the webservice to add a rating to the database.
         * @param urls a string
         * @return a string that contains the information from a POST query.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = ((HttpURLConnection) urlObject.openConnection());
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                    Log.i("RatingAct POST check", myEditRatingJSON.toString());
                    wr.write(myEditRatingJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add rating to item, Reason: " + e.getMessage();

                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("RatingAct Rating return", response.toString());
            return response;
        }

        /**
         * Method that checks for success of a returned JSON object from the webservice.
         * With a success, another JSON is created to change the average rating within
         * the item.
         * Code base supplied by TCSS 450 Instructor. Modified by Rich W.
         * @param s a String representing a JSON object.
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(myRatingDetailFragment.getActivity(), "Unable to connect " + s,
                        Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {

                    // making a new hashmap for use to update the rating on the item.
                    Map<String, String> addRatingMap = new HashMap<>();

                    //calc. new avg
                    float oldAvg = ItemRating.findAvgRatingFromList(myRatings);
                    int numOfRatings = myRatings.size();
                    float newRating = (oldAvg * numOfRatings + myNewRating) / (numOfRatings + 1);

                    // fill map
                    addRatingMap.put(ITEM_ID, mItemID);
                    addRatingMap.put(RATING, newRating + "");

                    HttpJSONTask task = new HttpJSONTask(getString(R.string.set_rating), addRatingMap);
                    task.execute(getString(R.string.set_rating));

                    Intent intent = new Intent(myRatingDetailFragment.getActivity(), RatingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ITEM_ID, myItem);
                    intent.putExtras(bundle);
                    myRatingDetailFragment.getActivity().finish();
                    myRatingDetailFragment.getActivity().startActivity(intent);

                } else {
                    Toast.makeText(myRatingDetailFragment.getActivity(), "There seems to be a " +
                            " problem editing your rating", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                Toast.makeText(myRatingDetailFragment.getActivity(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
//            mEditRatingProgressBar.setVisibility(View.GONE);
        }
    }
}