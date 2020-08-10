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

import edu.tacoma.uw.group9_450project.rateanything.model.Item;

/**
 * Class used to show the details of an item selected from a category.
 * Base code supplied by Android Studio templates and UWT TCSS 450 Instructor
 * @author Rich W.
 */
public class ItemDetailActivity extends AppCompatActivity {

    /** Constant */
    private static final String ARG_ITEM_ID = "item_id";
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * Mandatory method. Creates a floating action button and sets prepares arguments for
     * display. Menu and app bar needs formatting!
     * @param savedInstanceState a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Bundle bun = getIntent().getExtras();
        Item item =  (Item) bun.getSerializable(ARG_ITEM_ID);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item_detail_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(item.getMyItemName());
        //getSupportActionBar().setSubtitle(item.getMyItemName());
        toolbar.setTitleTextColor(WHITE);
        //getSupportActionBar().setSubtitle(item.getMyItemLongDesc());*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_item_detail_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with add Rating", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


 //       if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
//            arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID,
//                    getIntent().getSerializableExtra(ItemDetailFragment.ARG_ITEM_ID));

            arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container_item_detail_activity, fragment)
                    .commit();

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