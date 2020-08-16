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

import java.util.regex.Pattern;

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

    /** Constant - email validification pattern */
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /** Constant to validate password. */
    private final static int PASSWORD_LENGTH = 5;


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

                // Check email, password and username
                if (!isValidEmail(email)) {
                    Toast.makeText(v.getContext(), "Enter valid email address", Toast.LENGTH_LONG)
                            .show();
                    mEmailText.requestFocus();
                } else if (!isValidPassword(pwd) && !isValidPassword(pwd2)) {
                    Toast.makeText(v.getContext(), "Enter valid password (at least 5 characters" +
                                    "and containing at least one symbol and at least one number)",
                            Toast.LENGTH_LONG)
                            .show();
                    mPasswordText.requestFocus();
                    mPasswordConfirmText.requestFocus();
                } else if (TextUtils.isEmpty(name) || name.length() < 5 || name.length() > 36) {
                    Toast.makeText(v.getContext(), "User name not valid (Name must be " +
                            "between 5 and 36 characters.", Toast.LENGTH_LONG).show();
                    mUsernameText.requestFocus();
                }else if (pwd.compareTo(pwd2) != 0) {
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
                } else {
                    mRegistrationFragmentListener.register(first, last, email, name, pwd);
                }
            }
        });
        return view;
    }


    /**
     * Validates if the given input is a valid email address.
     * @param theEmail a String holding the email to validate.
     * @return a boolean. {@code true} if input is valid email {@code false} if not.
     */
    public static boolean isValidEmail(String theEmail) {
        return theEmail != null && EMAIL_PATTERN.matcher(theEmail).matches();
    }

    /**
     * Validates if the given password is valid. Passwords must be at least 6 characters
     * long with at least one digit and one symbol.
     * @param pwd a String representing the password to be validated.
     * @return a boolean {@code true} if the password meets the requirements
     * {@code false} if it does not.
     */
    public static boolean isValidPassword(String pwd) {
        boolean foundDigit = false;
        boolean foundSymbol = false;

        if ((pwd == null) || (pwd.length() < PASSWORD_LENGTH)) {
            return false;
        }
        for (int i = 0; i < pwd.length(); i++) {
            if (Character.isDigit(pwd.charAt(i)))
                foundDigit = true;
            if (!Character.isLetterOrDigit(pwd.charAt(i)))
                foundSymbol = true;
        }
        return foundDigit && foundSymbol;
    }


}


