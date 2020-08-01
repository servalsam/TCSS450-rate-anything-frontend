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

    /**
     * Interface to make the sign in work.
     */
    public interface LoginFragmentListener {
        public void login(String email, String pwd);
    }

    /** Required empty constructor */
    public LoginFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle("Sign In");
        mloginFragmentListener = (LoginFragmentListener) getActivity();
        final EditText emailText = view.findViewById(R.id.emailAddress_id);
        final EditText pwdText = view.findViewById(R.id.password_id);
        Button loginButton = view.findViewById(R.id.sign_in_btn_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String pwd = pwdText.getText().toString();

                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(v.getContext(),"Enter valid email address", Toast.LENGTH_SHORT)
                            .show();
                    emailText.requestFocus();
                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter valid password (at least 6 characters",
                            Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                }

                if (!TextUtils.isEmpty(email) && email.contains("@") && !TextUtils.isEmpty(pwd) && pwd.length() > 5) {
                    mloginFragmentListener.login(email, pwd);
                }
            }
        });
        return view;
    }
}