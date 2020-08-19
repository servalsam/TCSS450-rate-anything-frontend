package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.tacoma.uw.group9_450project.rateanything.R;

/**
 * Fragment to help SignInActivity
 * @author Code supplied by Android Studio and UWT 450 Instructor. Modified by Rich W.
 * @version July 2020
 */
public class LoginFragment extends Fragment {

    /** Member variable for the Listener interface */
    private LoginFragmentListener mloginFragmentListener;
    private EditText mPasswordText;
    private EditText mUser;

    /** Constants */
    private static final String REGISTER_SUCCESSFUL = "Registration was successful." +
            " Now login with your new account";
    private static final String FROM_REGISTER = "Registered";

    /**
     * Interface to make the sign in work.
     */
    public interface LoginFragmentListener {
        void login(String email, String pwd);
    }

    /** Required empty constructor */
    public LoginFragment() {}

    /**
     * onCreate method override.
     * @param savedInstanceState a Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(FROM_REGISTER)) {
                Toast.makeText(getContext(), REGISTER_SUCCESSFUL, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * The overridden method creates collects the login data from the user and prevents
     * the user from progressing unless the proper fields are filled out with the
     * proper types of data. Email must contain an @ or username must be at least 5 characters.
     * Also, the password must be at least 5 characters.
     * @author Rich W. - August 2020
     * @param inflater a LayoutInflater
     * @param container a ViewGroup
     * @param savedInstanceState a Bundle
     * @return a View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle("Sign In");
        mloginFragmentListener = (LoginFragmentListener) getActivity();
        mPasswordText = view.findViewById(R.id.password_id);
        mUser = view.findViewById(R.id.username_id);
        Button loginButton = view.findViewById(R.id.sign_in_btn_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = mPasswordText.getText().toString();
                String name = mUser.getText().toString();

                if ((TextUtils.isEmpty(name))) {
                    Toast.makeText(v.getContext(),"Enter username or email address", Toast.LENGTH_LONG)
                            .show();
                    mUser.requestFocus();
                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 5) {
                //(!RegisterFragment.isValidPassword(pwd)) { consider using this after test accounts removed done
                    Toast.makeText(v.getContext(), "Enter valid password",
                            Toast.LENGTH_LONG)
                            .show();
                    mPasswordText.requestFocus();
                } else {
                    mloginFragmentListener.login(name, pwd);
                }
            }
        });
        return view;
    }

}