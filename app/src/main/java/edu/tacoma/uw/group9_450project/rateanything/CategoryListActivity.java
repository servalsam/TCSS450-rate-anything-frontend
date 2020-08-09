package edu.tacoma.uw.group9_450project.rateanything;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.group9_450project.rateanything.model.Category;
import edu.tacoma.uw.group9_450project.rateanything.startup.SplashPageActivity;

/**
 * An activity representing a list of Categories. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CategoryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * @author Code supplied by Android Studio and UWT 450 Instructor. Updated
 * by Rich W.
 */
public class CategoryListActivity extends AppCompatActivity {

    /** ArrayList for categories and RecyclerView must be member variables accessible outside
     * of onCreate.
     */
    private List<Category> mCategoryList;
    private RecyclerView mRecyclerView;
    private ProgressBar mCategoryFillerProgressBar;

    /** Constants */
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME ="category_name";
    private static final String TITLE = "About";


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Mandatory method. Code supplied by Android Studio template.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_category_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mCategoryFillerProgressBar = (ProgressBar) findViewById(R.id.category_list_progress_bar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
     * Inner class for the RecyclerView Adapter depending on type of device. Base code supplied
     * by Android Studio template and UWT TCSS 450 Instructor. The method contains an
     * OnClickListener to start a CategoryDetailFragment.
     * @author Rich W.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CategoryListActivity mParentActivity;
        private final List<Category> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category = (Category) view.getTag();
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putSerializable(CategoryDetailFragment.ARG_ITEM_ID, category);
//                    CategoryDetailFragment fragment = new CategoryDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemListActivity.class);
                    intent.putExtra(CATEGORY_ID, category.getCategoryID());
                    intent.putExtra(CATEGORY_NAME, category.getMyCategoryName());
                    context.startActivity(intent);
//                }
            }
        };

        /**
         * Generated method from Android Studio Template
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
                SharedPreferences sharedPreferences =
                        getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                sharedPreferences.edit().
                        putBoolean(getString(R.string.LOGGEDIN), false).commit();
                Intent i = new Intent(this,SplashPageActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_add_category:
                //add category code
                break;
            case R.id.action_sync_category:
                //add sync code
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
     * Private class to setup asynchronous loading of the data.
     * Code supplied by UWT 450 Instructor. Modified by Rich W.
     */
    private class CategoryTask extends AsyncTask<String, Void, String> {

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