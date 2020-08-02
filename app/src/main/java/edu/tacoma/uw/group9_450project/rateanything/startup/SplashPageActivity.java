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
 * This is the first page of the Rate Anything App. Initial plans allow the user to
 * proceed to the categories and search options regardless of login state. There will be
 * an option here to login and register as a new user.
 * @author Rich W.
 * @version July 2020
 */
public class SplashPageActivity extends AppCompatActivity implements View.OnClickListener {

    /** Member variable to hold user login until data is cleared. */
    private SharedPreferences mSharedPreferences;

    /** Member variable flags. */
    private boolean mLoggedIn;
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
        setContentView(R.layout.activity_splash_page);
        mToRegister = false;

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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent (this, StartActivity.class);
        switch (view.getId()) {
            case R.id.login_button:
                startActivity(intent);
                finish();
                break;
            case R.id.register_button:
                mToRegister = true;
                startActivity(intent);
                finish();
                break;
            case R.id.proceed_as_guest_button:
                Toast.makeText(this,"Proceed as guest pressed", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}