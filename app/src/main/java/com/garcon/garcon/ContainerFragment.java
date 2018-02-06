package com.garcon.garcon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Container Fragment to display map and restaurant list view.
 */
public class ContainerFragment extends Fragment {

    public ContainerFragment() {
        // Required empty public constructor
    }

    public static ContainerFragment newInstance() {
        ContainerFragment fragment = new ContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_container, new PrimaryFragment());

        transaction.commit();

        return view;
    }

    public void switchFragments(boolean isPrimary){
        if (isPrimary){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new PrimaryFragment());
            transaction.commit();
        }else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new SecondFragment());
            transaction.commit();
        }
    }
}