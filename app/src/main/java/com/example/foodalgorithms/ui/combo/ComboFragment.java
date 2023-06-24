package com.example.foodalgorithms.ui.combo;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.foodalgorithms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComboFragment extends Fragment {

    FloatingActionButton comboFAB;

    private ComboViewModel mViewModel;
    RecyclerView comboRecyclerView;

    ComboAdapter comboAdapter;

    List<Combo> comboList;

    public static ComboFragment newInstance() {
        return new ComboFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combo, container, false);
        comboList = new ArrayList<>();

        comboRecyclerView = view.findViewById(R.id.ComboRecyclerView);
        comboAdapter = new ComboAdapter(getContext(), comboList);
        comboRecyclerView.setAdapter(comboAdapter);
        comboRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        comboFAB = view.findViewById(R.id.ComboFAB);
        comboFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateCombo = new Intent(getActivity(), CreateComboActivity.class);
                startActivity(toCreateCombo);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        comboList.clear();
        comboAdapter.notifyDataSetChanged();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("combos");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Combo combo = ds.getValue(Combo.class);
                    comboList.add(combo);
                    comboAdapter.notifyItemInserted(comboList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}