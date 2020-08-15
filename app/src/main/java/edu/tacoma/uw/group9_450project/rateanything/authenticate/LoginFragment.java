package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.tacoma.uw.group9_450project.rateanything.R;

/**
 * Fragment to help SignInActivity
 * @author Code supplied by Android Studio and UWT 450 Instructor. Modified by Rich W.
 * @version July 2020
 */
public class LoginFragment extends Fragment {

    /** Member variable for the Listener interface */
    private LoginFragmentListener mloginFragmentListener;
   // private EditText mEmailText;
    private EditText mPasswordText;
   // private EditText mUsernameText;
    private EditText mUser;

    /** Constants */
    private static final String MEMBER_ID = "member_id";
    private static final String USERNAME = "username";


    /**
     * Interface to make the sign in work.
     */
    public interface LoginFragmentListener {
        public void login(String email, String pwd);
    }

    /** Required empty constructor */
    public LoginFragment() {}

    /**
     * Mandatory onCreate method.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The overridden method creates collects the login data from the user and prevents
     * the user from progressing unless the proper fields are filled out with the
     * proper types of data. Email must contain an @ or username must be at least 5 characters.
     * Also, the password must be at least 5 characters.
     * @author Rich W.
     * @version August 2020
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle("Sign In");
        mloginFragmentListener = (LoginFragmentListener) getActivity();
        //mEmailText = view.findViewById(R.id.emailAddress_id);
        mPasswordText = view.findViewById(R.id.password_id);
        mUser = view.findViewById(R.id.username_id);
        Button loginButton = view.findViewById(R.id.sign_in_btn_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String email = mEmailText.getText().toString();
                String pwd = mPasswordText.getText().toString();
                String name = mUser.getText().toString();

                if ((TextUtils.isEmpty(name))) {
                    Toast.makeText(v.getContext(),"Enter username or email address", Toast.LENGTH_LONG)
                            .show();
                    mUser.requestFocus();
                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 5) {
                //(!RegisterFragment.isValidPassword(pwd)) { consider using this when testing done
                    Toast.makeText(v.getContext(), "Enter valid password",
                            Toast.LENGTH_LONG)
                            .show();
                    mPasswordText.requestFocus();
                } else {
                    mloginFragmentListener.login(name, pwd);
                }

//                if ((TextUtils.isEmpty(email) || !email.contains("@")) && TextUtils.isEmpty(name)) {
//                    Toast.makeText(v.getContext(),"Enter valid email address", Toast.LENGTH_SHORT)
//                            .show();
//                    mEmailText.requestFocus();
//                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 5) {
//                    Toast.makeText(v.getContext(), "Enter valid password (at least 5 characters",
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    mPasswordText.requestFocus();
//                } else if (TextUtils.isEmpty(email) && name.length() < 5) {
//                    Toast.makeText(v.getContext(), "Username must be at least 5 characters",
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    mUsernameText.requestFocus();
//                } else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name)) {
//                    Toast.makeText(v.getContext(), "Fill either the email or username, NOT both",
//                            Toast.LENGTH_SHORT)
//                            .show();
//                    mUsernameText.requestFocus();
//                    mEmailText.requestFocus();
//                }


//                if ((!TextUtils.isEmpty(email) && email.contains("@")
//                        || (!TextUtils.isEmpty(name) && name.length() > 4))
//                        && !TextUtils.isEmpty(pwd) && pwd.length() > 4) {
//                    if (TextUtils.isEmpty(email)) {
//                        mloginFragmentListener.login(name, pwd);
//                    } else {
//                        mloginFragmentListener.login(email, pwd);
//                    }
//                }
            }
        });
        return view;
    }

}