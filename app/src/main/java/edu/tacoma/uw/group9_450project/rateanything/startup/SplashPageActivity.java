package edu.tacoma.uw.group9_450project.rateanything.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.tacoma.uw.group9_450project.rateanything.R;
import edu.tacoma.uw.group9_450project.rateanything.authenticate.StartActivity;


/**
 * This is the first page of the Rate Anything App. The user will either register as new user,
 * login as an existing user or as a future option, proceed as a guest user with the ability to
 * view content but not contribute. The app looks for stored shared preferences that will enable
 * the user to proceed to category list and bypass authentication (as they are already logged-in).
 * @author Rich W.
 * @version July 2020
 */
public class SplashPageActivity extends AppCompatActivity implements View.OnClickListener {

    /** Member variable to hold user login until data is cleared. */
    private SharedPreferences mSharedPreferences;

    /** Member variable flags. */
    private boolean mLoggedIn;


    /** Constants */
    private static final String LOGIN = "Login as an existing Rate Anything User";
    private static final String LOGGED_IN = "Proceed to list of categories";
    public static String REG_MODE = "register_mode";




    /**
     * Mandatory method. In the method, the buttons are created and listeners are attached.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        // Button Creation
        Button loginBtn = (Button) findViewById(R.id.login_button);
        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), true)) {
            loginBtn.setText(LOGGED_IN);
            mLoggedIn = true;
        } else {
            loginBtn.setText(LOGIN);
            mLoggedIn = false;
        }
        Button registerBtn = (Button) findViewById(R.id.register_button);
        Button guestBtn = (Button) findViewById(R.id.proceed_as_guest_button);

        // Tying Buttons to Listener
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        guestBtn.setOnClickListener(this);
    }

    /**
     * The method allows launches the StartActivity and adds at registration mode flag to the
     * intent for use by the StartActivity launchFragment method.
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent (this, StartActivity.class);
        switch (view.getId()) {
            case R.id.login_button:
                startActivity(intent);
                break;
            case R.id.register_button:
                intent.putExtra(REG_MODE, true);
                startActivity(intent);
                break;
            case R.id.proceed_as_guest_button:
                Toast.makeText(this,"Proceed as guest pressed not yet implemented",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}