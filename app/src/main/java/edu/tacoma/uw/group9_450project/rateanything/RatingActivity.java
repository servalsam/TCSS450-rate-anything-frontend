package edu.tacoma.uw.group9_450project.rateanything;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.group9_450project.rateanything.model.Item;
import edu.tacoma.uw.group9_450project.rateanything.model.ItemRating;

public class RatingActivity extends AppCompatActivity {

    /** Class members used to store and display a list of ratings. */
    private List<ItemRating> mRatingList;
    private Item mItem;
    private JSONObject mRatingListJSON;
    private ProgressBar mRatingListProgressBar;
    private Toolbar mToolbar;

    /** Constants */
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String RATING_LIST = "Rating List Activity";
    private static final int WHITE = 0xFFFFFFFF;
    private static final String TITLE = "About";

    /**
     * Override method onCreate. Code base supplied by Android Studio Template and
     * UWT TCSS 450 Instructor.
     * @param savedInstanceState a Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mItem = (Item) bundle.getSerializable(ITEM_ID);
        Log.i("RatingAct Item", mItem.getMyItemName());

        mRatingListJSON = new JSONObject();
        fillRatingJSON();

        mRatingListProgressBar = (ProgressBar) findViewById(R.id.rating_activity_progress_bar_new);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_rating_activity_new);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mItem.getMyItemName());
        mToolbar.setTitleTextColor(WHITE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_rating_activity_new);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with add rating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                //add item code here
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
                Toast.makeText(getApplicationContext(), "Unable to download" + s,
                        Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    mRatingList = ItemRating.parseRatingJson(
                            jsonObject.getString("categories")); // Name of table
                    if(!mRatingList.isEmpty()) {
                        // Find and display the average rating for all the provided ratings
                        getSupportActionBar().setSubtitle("Rating: "
                                + ItemRating.findAvgRatingFromList(mRatingList));
                        // Insert code to launch the fragment passing the mRatingsList as
                        // part of the bundle
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


}