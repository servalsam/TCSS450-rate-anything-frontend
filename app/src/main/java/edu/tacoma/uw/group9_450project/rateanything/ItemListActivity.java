 package edu.tacoma.uw.group9_450project.rateanything;

 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
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
 import java.util.List;

import edu.tacoma.uw.group9_450project.rateanything.model.Item;

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

     /** Constants */
     private static final String CATEGORY_ID = "category_id";
     private static final String ITEM_LIST = "Item List Activity";

     /**
      * Private class to for asynchronous loading of data.
      * Code supplied by UWT 450 Instructor. Modified by Rich W.
      */
     private class ItemAsyncTask extends AsyncTask<String, Void, String> {

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
         }
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Bundle bundle = getIntent().getExtras();
        mCategory = bundle.getString(CATEGORY_ID);

        mItemListJSON = new JSONObject();
        fillJSON();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item_list_activity);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item_list_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with add item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = findViewById(R.id.item_list_item_list); //must fix this!!!
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mItemList != null) {
            mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter (this, mItemList));
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Item> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = (Item) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item);
                context.startActivity(intent);
            }
        };



        SimpleItemRecyclerViewAdapter(ItemListActivity parent, List<Item> items) {
            mValues = items;
            mParentActivity = parent;
        }
                                                                                        // must check ALL R.layouts
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText((mValues.get(position).getMyItemName()));
            holder.mContentView.setText(mValues.get(position).getMyItemShortDesc());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

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

    // Add Menu Options code here

}