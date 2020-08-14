package edu.tacoma.uw.group9_450project.rateanything;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
     * Method called when the "Rate This!" is pressed
     */
    public void addRating() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_rating_layout, null);
        builder.setView(customLayout);
        final CheckBox postPreference = (CheckBox) customLayout.findViewById(R.id.posting_choice_rating_layout);
        final RatingBar rating = (RatingBar) customLayout.findViewById(R.id.rating_value_rating_layout);
        final EditText inputComment = (EditText) customLayout.findViewById(R.id.input_rating_comments);

        Button btnAdd = (Button) customLayout.findViewById(R.id.input_rating_add_button);
        Button btnCancel = (Button) customLayout.findViewById(R.id.input_rating_cancel_button);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the code to run the AsyncTask
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

     bm1.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
     m_item_name = inputCategory.getText().toString();
     m_item_desc_short = inputDescShort.getText().toString();
     m_item_desc_long = inputDescLong.getText().toString();
     if (!m_item_name.equals("") && !m_item_desc_short.equals("") && !m_item_desc_long.equals("")) {
     Map<String, String> postData = new HashMap<>();
     postData.put("category_id", CATEGORY_ID);
     postData.put("item_name", m_item_name);
     postData.put("category_description_long", m_item_desc_long);
     postData.put("category_description_short", m_item_desc_short);
     postData.put("rating", "5.0");
     HttpJSONTask task = new HttpJSONTask(getString(R.string.add_item), postData);
     task.execute(getString(R.string.add_item));
     new ItemListActivity.ItemAsyncTask().execute(getString(R.string.get_items));
     } else {
     dialog.dismiss();
     Toast toast = new Toast(view.getContext());
     toast.setText("Fields cannot be empty.");
     toast.setDuration(Toast.LENGTH_LONG);
     toast.show();





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


}