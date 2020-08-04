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
 * A subclass to register a new user of Rate Anything.
 * @author Rich W. With base code supplied by Andoid Studio and UWT 450 Instructor.
 */
public class  RegisterFragment extends Fragment {

    /** Member variable for the Listener interface */
    private RegistrationFragmentListener mRegistrationFragmentListener;

    /** Member variables */
    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mUsernameText;
    private EditText mFirstNameText;
    private EditText mLastNameText;
    private EditText mPasswordConfirmText;

    /**
     * Interface to make the registration work.
     */
    public interface RegistrationFragmentListener {
        void register(String first, String last, String email, String name, String pwd);
    }

    /** Required empty public constructor */
    public RegisterFragment() {}

    /**
     * Required onCreate method.
     * @param savedInstanceState a Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The overridden method creates collects the registration data from the user and prevents
     * the user from progressing unless the proper fields are filled out with the
     * proper types of data. Email must contain an @ or username must be at least 5 characters.
     * The password must be at least 5 characters and both the password and the confirmation
     * password must match.
     * @author Rich W.
     * @param inflater a LayoutInflator
     * @param container a ViewGroup
     * @param savedInstanceState a Bundle
     * @return a View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle("Register");
        mRegistrationFragmentListener = (RegistrationFragmentListener) getActivity();
        mFirstNameText = view.findViewById(R.id.first_name_registration);
        mLastNameText = view.findViewById(R.id.last_name_registration);
        mEmailText = view.findViewById(R.id.emailAddress_registration);
        mPasswordText = view.findViewById(R.id.password_registration);
        mPasswordConfirmText = view.findViewById(R.id.password_confirm_registration);
        mUsernameText = view.findViewById(R.id.user_name_registration);
        Button registerButton = view.findViewById(R.id.register_btn);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString();
                String pwd = mPasswordText.getText().toString();
                String name = mUsernameText.getText().toString();
                String pwd2 = mPasswordConfirmText.getText().toString();
                String first = mFirstNameText.getText().toString();
                String last = mLastNameText.getText().toString();

                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter valid email address", Toast.LENGTH_SHORT)
                            .show();
                    mEmailText.requestFocus();
                } else if (TextUtils.isEmpty(pwd) || (pwd.length() < 5) || (pwd.length() > 32)) {
                    Toast.makeText(v.getContext(), "Enter valid password (at least 5 characters",
                            Toast.LENGTH_SHORT)
                            .show();
                    mPasswordText.requestFocus();
                } else if (TextUtils.isEmpty(pwd2) || pwd2.length() < 5) {
                    Toast.makeText(v.getContext(), "Enter valid password confirm (at least 5 characters",
                            Toast.LENGTH_SHORT)
                            .show();
                    mPasswordConfirmText.requestFocus();
                } else if (pwd.compareTo(pwd2) != 0) {
                    Toast.makeText(v.getContext(), "Passwords don't match",
                            Toast.LENGTH_SHORT).show();
                    mPasswordText.requestFocus();
                    mPasswordConfirmText.requestFocus();
                } else if (TextUtils.isEmpty(first)) {
                    Toast.makeText(v.getContext(), "Enter a first name",
                            Toast.LENGTH_SHORT)
                            .show();
                    mFirstNameText.requestFocus();
                } else if (!TextUtils.isEmpty(last)) {
                    Toast.makeText(v.getContext(), "Enter a last name",
                            Toast.LENGTH_SHORT)
                            .show();
                    mLastNameText.requestFocus();
                }

                if ((!TextUtils.isEmpty(email) && email.contains("@"))
                        && ((!TextUtils.isEmpty(name) && name.length() > 4))
                        && ((!TextUtils.isEmpty(pwd) && pwd.length() > 4))
                        && ((!TextUtils.isEmpty(pwd2) && pwd2.length() > 4))
                        && (!TextUtils.isEmpty(first))
                        && (!TextUtils.isEmpty(last))
                        && (pwd.compareTo(pwd2) == 0)) {
                    mRegistrationFragmentListener.register(first, last, email, name, pwd);
                }

            }
        });
        return view;
    }
}


