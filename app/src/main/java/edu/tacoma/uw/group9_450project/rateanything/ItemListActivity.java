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
 import android.util.Log;
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
 import com.google.android.material.snackbar.Snackbar;

 import org.json.JSONException;
 import org.json.JSONObject;

 import java.io.BufferedReader;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.io.OutputStreamWriter;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 import edu.tacoma.uw.group9_450project.rateanything.model.Item;
 import edu.tacoma.uw.group9_450project.rateanything.startup.SplashPageActivity;
 import edu.tacoma.uw.group9_450project.rateanything.utils.HttpJSONTask;

 /**
  * This activity represents a list of Items within a Category. The code base was
  * supplied by Android Studio master/detail template and the TCSS 450 instructor.
  * @author Rich W.
  */
 public class ItemListActivity extends AppCompatActivity {

     /** Class members used to store and display a list of items. */
     private List<Item> mItemList;
     private RecyclerView mRecyclerView;
     private String mCategory;
     private JSONObject mItemListJSON;
     private ProgressBar mItemListProgressBar;

     /** Constants */
     private static final String CATEGORY_ID = "category_id";
     private static final String CATEGORY_NAME = "category_name";
     private static final String ITEM_ID = "item_id";
     private static final String ITEM_LIST = "Item List Activity";
     private static final int WHITE = 0xFFFFFFFF;
     private static final String TITLE = "About";
     private static final String MEMBER_ID = "member_id";
     private static final String USERNAME = "username";
     private static final String DEFAULT_MEMBER_ID = "No id";
     private static final String DEFAULT_USERNAME = "Anonymous";


     /** Private Fields */
     private String m_item_name = "";
     private String m_item_desc_long = "";
     private String m_item_desc_short = "";
     private SharedPreferences mSharedPreferences;
     private String mMemberID;
     private String mUsername;

     /**
      * Override method onCreate method. Fills the type of category id from a data passed from the
      * CategoryListActivity. It then calls for a JSON object to be created so that the
      * AsyncTask might be used to get the items for the category.
       * @param savedInstanceState a Bundle
      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mMemberID = mSharedPreferences.getString(MEMBER_ID, DEFAULT_MEMBER_ID);
        mUsername = mSharedPreferences.getString(USERNAME, DEFAULT_USERNAME);

        // Getting data from the bundle
        Bundle bundle = getIntent().getExtras();
        mCategory = bundle.getString(CATEGORY_ID);
        String title = bundle.getString(CATEGORY_NAME);

        // Fill the JSON Object
        mItemListJSON = new JSONObject();
        fillJSON();

        mItemListProgressBar = (ProgressBar) findViewById(R.id.item_fill_list_progress_bar);

        // Toolbar setup
        Toolbar item_toolbar = (Toolbar) findViewById(R.id.toolbar_item_list_activity);
        setSupportActionBar(item_toolbar);
        getSupportActionBar().setTitle(title);
        item_toolbar.setTitleTextColor(WHITE);

        // Floating Action Button setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item_list_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View customLayout = getLayoutInflater().inflate(R.layout.add_item_layout, null);
                builder.setView(customLayout);
                final EditText inputCategory = (EditText) customLayout.findViewById(R.id.input_item_name);
                final EditText inputDescShort = (EditText) customLayout.findViewById(R.id.input_item_description_short);
                final EditText inputDescLong = (EditText) customLayout.findViewById(R.id.input_item_description);

                Button bm1 = (Button) customLayout.findViewById(R.id.input_item_add_button);
                Button bm2 = (Button) customLayout.findViewById(R.id.input_item_cancel_button);

                final AlertDialog dialog = builder.create();
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
        });

        mRecyclerView = findViewById(R.id.item_list_item_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

     /**
      * Helper method to create a JSON object for the AsyncTask
      * @author Rich W.
      */
    private void fillJSON () {
         Log.i("ItemListAct fill JSON", mCategory);
         if (mItemListJSON.length() > 0) {
             mItemListJSON = new JSONObject();
         }
         try {
             mItemListJSON.put(CATEGORY_ID, mCategory);
         } catch (JSONException e) {
             Toast.makeText(this, "Error with JSON creation on item list request: "
                     + e.getMessage(), Toast.LENGTH_LONG).show();
         }
         Log.i("ItemListAct fill JSON",mItemListJSON.toString());
    }

     /**
      * Method override to launch the AsyncTask.
      */
    @Override
     protected void onResume() {
         super.onResume();
         if (mItemList == null) {
             new ItemAsyncTask().execute(getString(R.string.get_items));
         }
    }

     /**
      * Method to set up a RecyclerView for the items in the category.
      * @param recyclerView a RecyclerView
      */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mItemList != null) {
            mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter (this, mItemList));
        }
    }

     /**
      * Class for a RecyclerView Adapter.
      * Base code supplied by UWT 450 Instructor.
      */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Item> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = (Item) view.getTag();
                Context context = view.getContext();

                Intent intent = new Intent(context, RatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ITEM_ID, item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        };


         /**
          * Helper method for the Adapter.
          * @param parent a ItemListActivity
          * @param items a List of items.
          */
        SimpleItemRecyclerViewAdapter(ItemListActivity parent, List<Item> items) {
            mValues = items;
            mParentActivity = parent;
        }

         /**
          * A helper method to that puts a ViewGroup into a ViewHolder.
           * @param parent a ViewGroup
          * @param viewType an int
          * @return a ViewHolder
          */
         @Override
         public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

         /**
          * A method that places the desired data of the items into the viewHolder as
          * well as making each item a clickable item.
          * @param holder a ViewHolder
          * @param position a position
          */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText((mValues.get(position).getMyItemName()));
            holder.mContentView.setText(mValues.get(position).getMyItemShortDesc());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

         /**
          * Helper method to return the number of items in the item list
          * @return an int (number of items)
          */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

         /**
          * The ViewHolder class. Code supplied by UWT 450 Instructor and the Android Studio
          * Templates.
          */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder (View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text_item_list_content);
                mContentView = (TextView) view.findViewById(R.id.content_item_list_content);
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
         getMenuInflater().inflate(R.menu.menu_item_list_activity, menu);
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
             case R.id.action_add_item:
                 //add item code here
                 break;
             case R.id.action_sync_item_list_activity:
                 //add sync code here
                 break;
             case R.id.action_item_activity_logout:
                 SharedPreferences sharedPreferences =
                         getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                 sharedPreferences.edit().
                         putBoolean(getString(R.string.LOGGEDIN), false).apply();
                 sharedPreferences.edit().remove(MEMBER_ID).apply();
                 sharedPreferences.edit().remove(USERNAME).apply();
                 Intent i = new Intent(this, SplashPageActivity.class);
                 startActivity(i);
                 finish();
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
     private class ItemAsyncTask extends AsyncTask<String, Void, String> {

         @Override
         protected void onPreExecute() {
             mItemListProgressBar.setVisibility(View.VISIBLE);
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
                     Log.i(ITEM_LIST, mItemListJSON.toString());
                     wr.write(mItemListJSON.toString());
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
             Log.i(ITEM_LIST, response.toString());
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
                     mItemList = Item.parseItemJson(
                             jsonObject.getString("categories")); // Name of table
                     if(!mItemList.isEmpty()) {
                         setupRecyclerView((RecyclerView) mRecyclerView);
                     }
                 }
             } catch (JSONException e){
                 Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                         Toast.LENGTH_LONG).show();
             }
             mItemListProgressBar.setVisibility(View.GONE);
         }
     }

 }