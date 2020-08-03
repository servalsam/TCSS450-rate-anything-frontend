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
import edu.tacoma.uw.group9_450project.rateanything.startup.SplashPageActivity;

/**
 * Class that signs the user into the app.
 * @author Code supplied by UWT 450 Instructor, modified by Rich W.
 * @version July 2020
 */
public class StartActivity extends AppCompatActivity implements
        LoginFragment.LoginFragmentListener,
        RegisterFragment.RegistrationFragmentListener {

    /** Member variables used for login and/or registration. */
    private SharedPreferences mSharedPreferences;
    private JSONObject mAuthJSON;

    /** Member variable flags. */
    private boolean mToLogin;


    /** Constants */
    private static final String NOT_LOGGED_IN = "Login as an existing Rate Anything User";
    private static final String LOGGED_IN = "Logged-in. Proceed to list of categories";
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

        launchFragments();
    }

    /**
     * A helper method that launches the appropriate fragment (either login or registration)
     * based upon the boolean value sent from SplashPageActivity. If the user has saved
     * preferences, neither fragment is launched; the CategoryListActivity is launched.
     * @author Rich W. with support from TCSS 450 Instructor (Menaka Abraham)
     * @version August 2020
     */
    private void launchFragments() {
        boolean mode = getIntent().getBooleanExtra(SplashPageActivity.REG_MODE, false);
        if (mode) {
            RegisterFragment rf = new RegisterFragment();
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.sign_in_fragment_container, rf);
            t.commit();
        } else if (!mSharedPreferences.getBoolean("loggedin", false)) {
            mToLogin = true;
            LoginFragment lf = new LoginFragment();
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.sign_in_fragment_container, lf);
            t.commit();
        } else {
            Intent intent = new Intent (this, CategoryListActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Method is called from LoginFragment when user has filled in the requested information
     * to login. The shared preferences are saved here if the checkbox was set to save the
     * login information. The method creates a JSON file to be used by the AsyncTask class to
     * verify login information through the webservice. The AsyncTask is launched upon
     * successful creation of the JSON file. Some of the code was supplied by the TCSS 450 class.
     * @author Rich W.
     * @version August 2020
     * @param loginInChoice a string that holds either a email or a username.
     * @param pwd a string that holds the user's password
     */
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

    /**
     * Method is called from RegisterFragment when user has filled in the requested information
     * to register. The method creates a JSON file to be used by the AsyncTask class to verify
     * registration information through the webservice. The AsyncTask is launched upon successful
     * creation of the JSON file. Some of the code was supplied by the TCSS 450 class.
     * @author Rich W.
     * @version August 2020
     * @param first string containing first name of user
     * @param last string containing last name of user
     * @param email string containing email of user
     * @param name string containing selected username by user
     * @param pwd string containing selected password by user
     */
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

    /**
     * The AsyncTask class that sends the JSON file to the webservice. In the OnPostExecute,
     * if AuthAsyncTask was called from login, the CategoryListActivity. If the AuthAsyncTask
     * was called from register, then LoginFragment is started. Code was based off examples
     * supplied through the TCSS 450 class.
     * @author Rich W.
     * @version August 2020
     */
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
                        i.setClass(getApplicationContext(), CategoryListActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        mToLogin = true;
                        Toast.makeText(getApplicationContext(), "Successful registration",
                                Toast.LENGTH_LONG).show();
                        LoginFragment lf = new LoginFragment();
                        FragmentTransaction transaction =
                                getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.sign_in_fragment_container, lf);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
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
