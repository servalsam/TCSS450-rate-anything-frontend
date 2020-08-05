package edu.tacoma.uw.group9_450project.rateanything;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Class used to show the details of an item selected from a category.
 * Base code supplied by Android Studio templates and UWT TCSS 450 Instructor
 * @author Rich W.
 */
public class ItemDetailActivity extends AppCompatActivity {

    /**
     * Mandatory method. Creates a floating action button and sets prepares arguments for
     * display. Menu and app bar needs formatting!
     * @param savedInstanceState a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar_item_detail);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item_detail_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with add Rating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getSerializableExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container_item_detail_activity, fragment)
                    .commit();
        }
    }

    /**
     * Method for selecting Menu items. Menu not yet implemented.
     * @param item a MenuItem
     * @return a boolean indicating a particular menu item was selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            navigateUpTo(new Intent(this, CategoryListActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}