package org.fireking.uugame.uis.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.fireking.uugame.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreOnLineGameFragment extends Fragment {


    public PreOnLineGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pre_on_line_game, container, false);
    }

}
