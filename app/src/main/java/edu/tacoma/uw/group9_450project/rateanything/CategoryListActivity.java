package edu.tacoma.uw.group9_450project.rateanything;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tacoma.uw.group9_450project.rateanything.model.Category;
import edu.tacoma.uw.group9_450project.rateanything.startup.SplashPageActivity;
import edu.tacoma.uw.group9_450project.rateanything.utils.HttpJSONTask;

/**
 * An activity representing a list of Categories. This activity no longer
 * has different presentations for handset and tablet-size devices.
 * @author Code supplied by Android Studio and UWT 450 Instructor. Updated
 * by Rich W. and Sam W.
 */
public class CategoryListActivity extends AppCompatActivity {

    /** ArrayList for categories and RecyclerView must be member variables accessible outside
     * of onCreate.
     */
    private List<Category> mCategoryList;
    private RecyclerView mRecyclerView;
    private ProgressBar mCategoryFillerProgressBar;
    private CategoryListActivity mCategoryListActivity;

    /** Constants */
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME ="category_name";
    private static final String TITLE = "About";
    private static final String MEMBER_ID = "member_id";
    private static final String USERNAME = "username";

    /** Private Fields */
    private String m_category_name = "";
    private String m_category_desc_long = "";
    private String m_category_desc_short = "";


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Mandatory method. Code supplied by Android Studio template.
     * @param savedInstanceState a Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        mCategoryListActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mCategoryFillerProgressBar = (ProgressBar) findViewById(R.id.category_list_progress_bar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory(view);
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // This code is used to instantiate mRecyclerView.
        mRecyclerView= findViewById(R.id.item_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

    /**
     * Method to add a category to the list of categories to which items can be added.
     * @param view a view.
     * @author Sam W.
     */
    public void addCategory(View view) {
        Context context = view.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_category_layout, null);
        builder.setView(customLayout);
        final EditText inputCategory = (EditText) customLayout.findViewById(R.id.input_category_name);
        final EditText inputDescShort = (EditText) customLayout.findViewById(R.id.input_category_description_short);
        final EditText inputDescLong = (EditText) customLayout.findViewById(R.id.input_category_description);

        Button bm1 = (Button) customLayout.findViewById(R.id.input_category_add_button);
        Button bm2 = (Button) customLayout.findViewById(R.id.input_category_cancel_button);

        final AlertDialog dialog = builder.create();
        bm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_category_name = inputCategory.getText().toString();
                m_category_desc_short = inputDescShort.getText().toString();
                m_category_desc_long = inputDescLong.getText().toString();
                if (!m_category_name.equals("") && !m_category_desc_short.equals("") && !m_category_desc_long.equals("")) {
                    Map<String, String> postData = new HashMap<>();
                    postData.put("category_name", m_category_name);
                    postData.put("category_description_long", m_category_desc_long);
                    postData.put("category_description_short", m_category_desc_short);
                    HttpJSONTask task = new HttpJSONTask(getString(R.string.add_category), postData);
                    task.execute(getString(R.string.add_category));
                    new CategoryTask().execute(getString(R.string.get_categories));
                } else {
                    dialog.dismiss();
                    Toast toast = new Toast(view.getContext());
                    toast.setText("Fields cannot be empty.");
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                }
                dialog.dismiss();
            }
        });
        bm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    /**
     * Override method used to launch the AsyncTask.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCategoryList == null) {
            new CategoryTask().execute(getString(R.string.get_categories));
        }
    }

    /**
     * Method to setup the RecyclerView.
     * @param recyclerView a RecyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCategoryList != null) {
            mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter
                    (this, mCategoryList, mTwoPane));
        }
    }

    /**
     * Generated method override by Android Studio template. Used to inflate the menu.
     * @param menu a Menu
     * @return a boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_list, menu);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                aboutBox(this);
                break;

            case R.id.action_logout:
                logout();
                break;

            case R.id.action_add_category:
                View rootView = getWindow().getDecorView().getRootView();
                addCategory(rootView);
                break;

            case R.id.action_sync_category:
                syncWithDatabase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void syncWithDatabase() {
        Intent i = new Intent();
        i.setClass(getApplicationContext(), CategoryListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        mCategoryListActivity.finish();
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
     * Method to sign out of the app. Shared preferences are cleared.
     */
    public void logout() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        sharedPreferences.edit().
                putBoolean(getString(R.string.LOGGEDIN), false).apply();
        sharedPreferences.edit().remove(MEMBER_ID).apply();
        sharedPreferences.edit().remove(USERNAME).apply();
        Intent i = new Intent(this, SplashPageActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Inner class for the RecyclerView Adapter depending on type of device. Base code supplied
     * by Android Studio template and UWT TCSS 450 Instructor. The method contains an
     * OnClickListener to start a CategoryDetailFragment.
     * @author Rich W.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        /** Fields of the this RecyclerView Adapter class. */
        private final CategoryListActivity mParentActivity;
        private final List<Category> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category = (Category) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemListActivity.class);
                intent.putExtra(CATEGORY_ID, category.getCategoryID());
                intent.putExtra(CATEGORY_NAME, category.getMyCategoryName());
                context.startActivity(intent);
            }
        };

        /**
         * Generated constructor from Android Studio Template
         * @param parent the CategoryListActivity
         * @param items a List of categories.
         * @param twoPane a boolean for layout
         */
        SimpleItemRecyclerViewAdapter(CategoryListActivity parent,
                                      List<Category> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        /**
         * A method override supplied by Android Studio template.
         * @param parent a ViewGroup
         * @param viewType an int
         * @return a ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Method override supplied by Android Studio template. Used to place content
         * into the ViewHolder
         * @param holder a ViewHolder
         * @param position an int to place the items in order in the ViewHolder
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText((mValues.get(position).getMyCategoryName()));
            holder.mContentView.setText(mValues.get(position).getMyCategoryShortDesc());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }


        /**
         * Getter method for the number of items in the ViewHolder.
         * @return the number of items.
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * The inner class of ViewHolder a RecyclerView
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }


    /**
     * Private class to setup asynchronous loading of the data.
     * Code supplied by UWT 450 Instructor. Modified by Rich W.
     */
    private class CategoryTask extends AsyncTask<String, Void, String> {

        /**
         * Sets the progress bar visible when the asyncTask is executed.
         */
        @Override
        protected void onPreExecute() {
            mCategoryFillerProgressBar.setVisibility(View.VISIBLE);
        }

        /**
         * Override method used to connect to the webservice to gather categories.
         * @param urls a string
         * @return a string that contains the information from a GET query.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * The override method to set-up a recycler view for the categories based on
         * successful connection to the webservice.
         * @param s a string containing the JSON file
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
                    mCategoryList = Category.parseCategoryJson(
                            jsonObject.getString("categories")); // Name of table
                    if(!mCategoryList.isEmpty()) {
                        setupRecyclerView((RecyclerView) mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            mCategoryFillerProgressBar.setVisibility(View.GONE);
        }
    }

}