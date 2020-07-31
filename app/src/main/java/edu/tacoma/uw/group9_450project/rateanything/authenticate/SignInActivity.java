package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.tacoma.uw.group9_450project.rateanything.CategoryListActivity;
import edu.tacoma.uw.group9_450project.rateanything.R;

/**
 * Class that signs the user into the app.
 * @author Code supplied by UWT 450 Instructor, modified by Rich W.
 * @version July 2020
 */
public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener {

    /** Member variable to hold user login until data is cleared. */
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        // checking to see if there are stored shared preferences
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_container, new LoginFragment())
                    .commit();
        } else {
            Intent intent = new Intent (this, CategoryListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void login(String email, String pwd) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();
        Intent i = new Intent(this, CategoryListActivity.class);
        startActivity(i);
        finish();
    }
}