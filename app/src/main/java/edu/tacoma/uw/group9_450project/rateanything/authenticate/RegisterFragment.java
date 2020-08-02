package edu.tacoma.uw.group9_450project.rateanything.authenticate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.tacoma.uw.group9_450project.rateanything.R;

/**
 * A simple subclass to register a new user of Rate Anything.
 * @author Rich W. With base code supplied by Andoid Studio and UWT 450 Instructor.
 */
public class  RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}