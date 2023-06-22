package com.example.foodalgorithms.ui.combo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodalgorithms.R;

public class ComboDetails extends Fragment {

    String comboId;



    public ComboDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            comboId = bundle.getString("comboId");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combo_details, container, false);
        TextView tv = view.findViewById(R.id.ComboDetailName);
       // tv.setText(comboId);
        return view;
    }
}