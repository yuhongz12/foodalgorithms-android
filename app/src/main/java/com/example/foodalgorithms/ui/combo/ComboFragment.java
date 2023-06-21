package com.example.foodalgorithms.ui.combo;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodalgorithms.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComboFragment extends Fragment {

    private ComboViewModel mViewModel;
     RecyclerView comboRecyclerView;

     ComboAdapter comboAdapter;

     List<Combo> comboList = new ArrayList<>( Arrays.asList(new Combo("combo name", "food", "cocktail")));

    public static ComboFragment newInstance() {
        return new ComboFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combo, container, false);

        comboRecyclerView = view.findViewById(R.id.ComboRecyclerView);
        comboAdapter = new ComboAdapter(getContext(), comboList);
        comboRecyclerView.setAdapter(comboAdapter);
        comboRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        comboRecyclerView.addItemDecoration(new DividerItemDecoration(comboRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ComboViewModel.class);
        // TODO: Use the ViewModel
    }

}