package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
        implements View.OnClickListener,
        LoginFragment.LoginFragmentListener,
        RegisterFragment.RegistrationFragmentListener {

    /** Member variables used for login and/or registration. */
    private SharedPreferences mSharedPreferences;
    private JSONObject mAuthJSON;
//    private Fragment mLoginFragment;
//    private Fragment mRegisterFragment;

    /** Member variable flags. */
    private boolean mToLogin;
//    private boolean mToSaveLogon;
//    private static boolean mToRegister;
//    private boolean mOkToProceedToCategoriesActivity;
//    private boolean mOkToProceedToLogin;


    /** Constants */
    private static final String LOGIN = "Login as an existing Rate Anything User";
    private static final String LOGGED_IN = "Proceed to list of categories";
    private static final String AUTHENTICATE = "Authenticate";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PWD = "password";
    private static final String FIRST = "first";
    private static final String LAST = "last";
    private static final String LOGIN_ERROR = "Email / Username does not match password";
    private static final String REGISTRATION_ERROR = "Email / Username has been taken";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mAuthJSON = new JSONObject();
//        mOkToProceedToCategoriesActivity = false;
//        mOkToProceedToLogin = false;

        // Button Creation
        Button loginBtn = (Button) findViewById(R.id.login_button);
        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), true)) {
            loginBtn.setText(LOGGED_IN);
//            mToSaveLogon = true;
        } else {
            loginBtn.setText(LOGIN);
//            mToSaveLogon = false;
        }
        Button registerBtn = (Button) findViewById(R.id.register_button);
        Button guestBtn = (Button) findViewById(R.id.proceed_as_guest_button);

        // Tying Buttons to Listener
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        guestBtn.setOnClickListener(this);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mOkToProceedToCategoriesActivity) {
//            Intent i = new Intent(this, CategoryListActivity.class);
//            startActivity(i);
//            finish();
//        }
//        if (mOkToProceedToLogin) {
//            mToLogin = true;
//            mToRegister = false;
//            Fragment mLoginFragment = new LoginFragment();
//            FragmentTransaction transaction =
//                    getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.sign_in_fragment_container, mLoginFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//    }

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
                    mToLogin = true;
//                    mToRegister = false;
                    LoginFragment lf = new LoginFragment();
//                    mLoginFragment = new LoginFragment();
                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.sign_in_fragment_container, lf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Intent intent = new Intent(this, CategoryListActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.register_button:
//                mToRegister = true;
                mToLogin = false;
                RegisterFragment rf = new RegisterFragment();
                FragmentTransaction transaction =
                        getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.sign_in_fragment_container, rf);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.proceed_as_guest_button:
                Toast.makeText(this,"Proceed as guest pressed",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void login(String loginInChoice, String pwd) {
        boolean isChecked = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
        if (isChecked) {
            mSharedPreferences
                    .edit()
                    .putBoolean(getString(R.string.LOGGEDIN), true)
                    .apply();
        }
        StringBuilder url = new StringBuilder();
        if (mAuthJSON.length() > 0) {
            mAuthJSON = new JSONObject();
        }
        try{
            if (loginInChoice.contains("@")) {
                url.append(getString(R.string.login_email));
                mAuthJSON.put(EMAIL, loginInChoice);
            } else {
                url.append(getString(R.string.login_username));
                mAuthJSON.put(USERNAME, loginInChoice);
            }
            mAuthJSON.put(PWD, pwd);
            new AuthAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this,"Error with JSON creation on login: "
                    + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void register(String first, String last, String email, String name, String pwd) {
        StringBuilder url = new StringBuilder();
        if (mAuthJSON.length() > 0) {
            mAuthJSON = new JSONObject();
        }
        url.append(getString(R.string.register));
        try{
            mAuthJSON.put(FIRST, first);
            mAuthJSON.put(LAST, last);
            mAuthJSON.put(EMAIL, email);
            mAuthJSON.put(USERNAME, name);
            mAuthJSON.put(PWD, pwd);
            new AuthAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this,"Error with JSON creation on login: "
                    + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class AuthAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                    Log.i(AUTHENTICATE, mAuthJSON.toString());
                    wr.write(mAuthJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable authenticate, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.i(AUTHENTICATE,response.toString());
            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("OnPostExecute", s.toString());
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Intent i = new Intent();
                    if (mToLogin) {
                        //mOkToProceedToCategoriesActivity = true;
                        i.setClass(getApplicationContext(), CategoryListActivity.class);

                    } else {
                        //mOkToProceedToLogin = true;
                        Toast.makeText(getApplicationContext(), "Successful registration",
                                Toast.LENGTH_LONG).show();
                       i.setClass(getApplicationContext(), StartActivity.class);
                    }
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Authentication not possible: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e(AUTHENTICATE, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                if (mToLogin) {
                    Toast.makeText(getApplicationContext(), "Unable to authenticate, " +
                            LOGIN_ERROR + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to authenticate, " +
                            REGISTRATION_ERROR + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e(AUTHENTICATE, e.getMessage());
            }
        }
    }
}
