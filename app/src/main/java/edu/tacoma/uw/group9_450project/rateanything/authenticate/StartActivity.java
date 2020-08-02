package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.group9_450project.rateanything.CategoryListActivity;
import edu.tacoma.uw.group9_450project.rateanything.R;
import edu.tacoma.uw.group9_450project.rateanything.model.Category;

/**
 * Class that signs the user into the app.
 * @author Code supplied by UWT 450 Instructor, modified by Rich W.
 * @version July 2020
 */
public class StartActivity extends AppCompatActivity
        implements View.OnClickListener, LoginFragment.LoginFragmentListener {

    /** Member variable to hold user login until data is cleared. */
    private SharedPreferences mSharedPreferences;

    /** Member variable flags. */
    private boolean mToLogon;
    private boolean mToSaveLogon;
    private static boolean mToRegister;


    /** Constants */
    private static final String LOGIN = "Login as an existing Rate Anything User";
    private static final String LOGGED_IN = "Proceed to list of categories";

    /** Getter methods for flags*/
    public static boolean getRegisterStatus() { return mToRegister;}
    public static void setRegisterStatus() {mToRegister = false;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        // Button Creation
        Button loginBtn = (Button) findViewById(R.id.login_button);
        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), true)) {
            loginBtn.setText(LOGGED_IN);
            mToSaveLogon = true;
        } else {
            loginBtn.setText(LOGIN);
            mToSaveLogon = false;
        }
        Button registerBtn = (Button) findViewById(R.id.register_button);
        Button guestBtn = (Button) findViewById(R.id.proceed_as_guest_button);

        // Tying Buttons to Listener
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        guestBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        // create a frame layout
        FrameLayout fragmentLayout = new FrameLayout(this);
        // set the layout params to fill the activity
        fragmentLayout.setLayoutParams
                (new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        // set the container id to the layout
        fragmentLayout.setId(R.id.sign_in_fragment_container);
        // set the layout as Activity content
        setContentView(fragmentLayout);

        switch (view.getId()) {
            case R.id.login_button:
                if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
                    mToLogon = true;
                    Fragment nFrag = new LoginFragment();
                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.sign_in_fragment_container, nFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Intent intent = new Intent(this, CategoryListActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.register_button:
                mToRegister = true;
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_container, new RegisterFragment())
                    .addToBackStack(null).commit();
                break;

            case R.id.proceed_as_guest_button:
                Toast.makeText(this,"Proceed as guest pressed", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
