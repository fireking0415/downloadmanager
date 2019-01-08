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
public class AbroadGameFragment extends Fragment {


    public AbroadGameFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abroad_game, container, false);
    }

}
