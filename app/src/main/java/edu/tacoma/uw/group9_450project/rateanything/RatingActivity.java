package edu.tacoma.uw.group9_450project.rateanything;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tacoma.uw.group9_450project.rateanything.model.Item;
import edu.tacoma.uw.group9_450project.rateanything.model.ItemRating;
import edu.tacoma.uw.group9_450project.rateanything.utils.HttpJSONTask;

public class RatingActivity extends AppCompatActivity {

    /** Class members used to store and display a list of ratings. */
    private List<ItemRating> mRatingList;
    private Item mItem;
    private JSONObject mRatingListJSON;
    private ProgressBar mRatingListProgressBar;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreferences;
    private String mMemberID;
    private String mUsername;
    private Float mNewRating;
    private String mNewRatingComment;
    private Boolean mPostAsAnonymous;
    private JSONObject mAddRatingJSON;


    /** Constants */
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String RATING_LIST = "Rating List Activity";
    private static final int WHITE = 0xFFFFFFFF;
    private static final String TITLE = "About";
    private static final String MEMBER_ID = "member_id";
    private static final String DEFAULT_MEMBER_ID = "1";
    private static final String USERNAME = "username";
    private static final String DEFAULT_USERNAME = "Anonymous";
    private static final String RATING = "rating";
    private static final String RATING_COMMENT = "rating_description";
    private static final String ANONYMOUS = "is_anonymous";

    /**
     * Override method onCreate. Code base supplied by Android Studio Template and
     * UWT TCSS 450 Instructor.
     * @param savedInstanceState a Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
//        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
//                Context.MODE_PRIVATE);
        mMemberID = preferences.getString(MEMBER_ID, DEFAULT_MEMBER_ID);
        mUsername = preferences.getString(USERNAME, DEFAULT_USERNAME);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mItem = (Item) bundle.getSerializable(ITEM_ID);
        Log.i("RatingAct Item", mItem.getMyItemName());

        mRatingListJSON = new JSONObject();
        mAddRatingJSON = new JSONObject();
        fillRatingJSON();

        mNewRating = 0f;
        mNewRatingComment = "";

        mRatingListProgressBar = (ProgressBar) findViewById(R.id.rating_activity_progress_bar_new);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_rating_activity_new);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mItem.getMyItemName());
        mToolbar.setTitleTextColor(WHITE);

        // showing the long description of the item
        TextView longDesc =
                (TextView) findViewById(R.id.item_long_desc_activity_rating);
        longDesc.setText(mItem.getMyItemLongDesc());

        Button add_rating_button = findViewById(R.id.rating_activity_add_rating_button);
        add_rating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRating();
            }
        });

        Button btnShare = (Button) findViewById(R.id.rating_activity_share_button);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareItem();
            }
        });

    }

    /**
     * Helper method to fill the JSON that will be sent as part of the POST to the webservice.
     * @author Rich W.
     */
    private void fillRatingJSON() {
        Log.i("RatingAct fill JSON", mItem.getMyItemID());
        if(mRatingListJSON.length() > 0) {
            mRatingListJSON = new JSONObject();
        }
        try {
            mRatingListJSON.put(ITEM_ID, mItem.getMyItemID());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on rating request: "
                    + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Log.i("RatingAct fill JSON", mRatingListJSON.toString());
    }

    /**
     * Method override to launch the AsyncTask.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mRatingList == null) {
            new RatingAsyncTask().execute(getString(R.string.get_ratings));
        }
    }

    /**
     * Method called when the "Rate This!" is pressed. It will add a rating to the item.
     */
    public void addRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_rating_layout, null);
        builder.setView(customLayout);
        final CheckBox postPreference = (CheckBox) customLayout.findViewById(R.id.posting_choice_rating_layout);
        final RatingBar ratingBar = (RatingBar) customLayout.findViewById(R.id.rating_value_rating_layout);
        final EditText inputComment = (EditText) customLayout.findViewById(R.id.input_rating_comments);

        Button btnAdd = (Button) customLayout.findViewById(R.id.input_rating_add_button);
        Button btnCancel = (Button) customLayout.findViewById(R.id.input_rating_cancel_button);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPostAsAnonymous = postPreference.isChecked();
                mNewRating = ratingBar.getRating();
                String ratingString = "";
                mNewRatingComment = inputComment.getText().toString();

                if (!mNewRatingComment.equals("") || mNewRating < 0.49) {
                    try {
                        mAddRatingJSON.put(ITEM_ID, mItem.getMyItemID());
                        mAddRatingJSON.put(MEMBER_ID, mMemberID);
                        mAddRatingJSON.put(USERNAME, mUsername);
                        mAddRatingJSON.put(RATING_COMMENT, mNewRatingComment);
                        mAddRatingJSON.put(RATING, String.format(ratingString.valueOf(mNewRating), "%.1f"));
                        mAddRatingJSON.put(ANONYMOUS, mPostAsAnonymous);
                    } catch (JSONException e) {
                        Toast.makeText(view.getContext(), "Error with JSON creation on add rating" +
                                e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                 new AddRatingAsyncTask().execute(getString(R.string.add_rating));
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


    /**
     * Method to share an item through email.
     */
    protected void shareItem() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this item on RateAnything!");
        TextView descriptiveText = (TextView) findViewById(R.id.item_long_desc_activity_rating);
        TextView avgRatingText = (TextView)  findViewById(R.id.toolbar_rating_text);
        String itemNameString = mItem.getMyItemName();
        String descriptionString = descriptiveText.getText().toString();
        String ratingString = avgRatingText.getText().toString();
        String ratingSent = "";
        if (ratingString.length() != 0) {
            String[] arr = ratingString.split("[()]");
            ratingSent = arr[1];
        } else {
            ratingSent = " No rating";
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, itemNameString + "\n\n" + descriptionString + "\nRating: " + ratingSent);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RatingActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Generated method override by Android Studio template. Used to inflate the menu.
     * @param menu a Menu
     * @return a boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rating_activity, menu);
        return true;
    }

    /**
     * This method launches the correct response based upon the menu item selected. If sign
     * out is selected, then the shared preferences are removed and the SplashPageActivity
     * is launched.
     * @param item the menu item selected.
     * @return a boolean
     */
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_button_item_list_activity:
                aboutBox(this);
                break;
            case R.id.action_add_rating:
                // Temporary code for testing Shared Preferences
                String message = USERNAME + ": " + mUsername + ", " + MEMBER_ID + ": " + mMemberID;
                View view = findViewById(android.R.id.content).getRootView();
                Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.action_sync_rating_activity:
                //add sync code here
                break;
            case R.id.action_rating_activity_logout:
                //add code for sign out here
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method builds and displays the About for the app inside of a alert dialog box.
     * @author Rich W.
     * @param activity an Activity
     */
    public void aboutBox(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View about = getLayoutInflater().inflate(R.layout.about_layout, null);
        builder.setView(about);
        builder.setTitle(TITLE);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog aboutDialog = builder.create();
        aboutDialog.show();
    }

    /**
     * Private class to for asynchronous loading of data.
     * Code supplied by UWT 450 Instructor. Modified by Rich W.
     */
    private class RatingAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            mRatingListProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Method override to connect to the webservice to get the contents of a table
         * containing a list of items for a category.
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
                    Log.i("RatingAct POST check", mRatingListJSON.toString());
                    wr.write(mRatingListJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download item list, Reason: " + e.getMessage();

                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i("RatingAct JSON return", response.toString());
            return response;
        }

        /**
         * Method that checks for success of a returned JSON object from the webservice.
         * With a success, the JSON is parsed and an list of items is filled.
         * Code base supplied by TCSS 450 Instructor. Modified by Rich W.
         * @param s a String representing a JSON object.
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to connect " + s,
                        Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {

                        // Adding the list of ratings.
                        mRatingList = ItemRating.parseRatingJson(
                                jsonObject.getString("categories")); // Name of table
                        if(!mRatingList.isEmpty()) {

                            // Find and display the average rating for all the provided ratings
                            float avg = ItemRating.findAvgRatingFromList(mRatingList);
                            TextView ratingTxt = mToolbar.findViewById(R.id.toolbar_rating_text);
                            ImageView ratingImg = mToolbar.findViewById(R.id.toolbar_rating_image);
                            ratingTxt.setText("(" + avg + ")");
                            ratingImg.setImageResource(ItemRating.getRatingImage(avg));

                            // code to launch the fragment passing ratings as part of the bundle
                            Bundle args = new Bundle();
                            ArrayList<ItemRating> arrayListOfRatings = new ArrayList<>(mRatingList.size());
                            arrayListOfRatings.addAll(mRatingList);
                            args.putSerializable(RATING_LIST, arrayListOfRatings);
                            RatingDetailFragment ratings = new RatingDetailFragment();
                            ratings.setArguments(args);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.rating_activity_frameLayout_new, ratings).commit();

                            Log.i("RatingAct List Fill?", mRatingList.toString());

                    }
                }
            } catch (JSONException e){
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            mRatingListProgressBar.setVisibility(View.GONE);
        }
    }



     /**
     * Private class to for asynchronous add a rating to an item.
     * Code supplied by UWT 450 Instructor. Modified by Rich W.
     */
    private class AddRatingAsyncTask extends AsyncTask<String, Void, String> {

         /**
          * Method override to start the progressbar.
          */
         @Override
        protected void onPreExecute() {
            mRatingListProgressBar.setVisibility(View.VISIBLE);
        }

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
                    Log.i("RatingAct POST check", mAddRatingJSON.toString());
                    wr.write(mAddRatingJSON.toString());
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
                Toast.makeText(getApplicationContext(), "Unable to connect " + s,
                        Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {

                    // making a new hashmap for use to update the rating on the item.
                    Map<String, String> addRatingMap = new HashMap<>();

                    //calc. new avg
                    float oldAvg = ItemRating.findAvgRatingFromList(mRatingList);
                    int numOfRatings = mRatingList.size();
                    float newRating = (oldAvg * numOfRatings + mNewRating) / (numOfRatings + 1);

                    // fill map
                    addRatingMap.put(ITEM_ID, mItem.getMyItemID());
                    addRatingMap.put(RATING, newRating + "");

                    HttpJSONTask task = new HttpJSONTask(getString(R.string.set_rating), addRatingMap);
                    task.execute(getString(R.string.set_rating));
                    new RatingAsyncTask().execute(getString(R.string.get_ratings));

                } else {
                    Toast.makeText(getApplicationContext(), "You've already rating this item." +
                            " Edit your rating instead.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e){
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            mRatingListProgressBar.setVisibility(View.GONE);
        }
    }

}