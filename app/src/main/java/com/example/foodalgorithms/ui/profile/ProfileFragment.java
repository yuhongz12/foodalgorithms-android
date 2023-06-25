package com.example.foodalgorithms.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.combo.Combo;
import com.example.foodalgorithms.ui.combo.ComboAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView userNameTV;
    TextView userIdTV;
    TextView userComboCount;
    String currentUser;
    RecyclerView profileComboList;

    List<Combo> comboList;
    ComboAdapter comboAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTV = view.findViewById(R.id.ProfileWelcomeName);
        userIdTV = view.findViewById(R.id.profileUserId);
        profileComboList = view.findViewById(R.id.profileComboList);
        userComboCount = view.findViewById(R.id.ProfileComboCount);

        comboList = new ArrayList<>();
        comboAdapter = new ComboAdapter(getContext(), comboList);
        profileComboList.setAdapter(comboAdapter);
        profileComboList.setLayoutManager(new LinearLayoutManager(getContext()));
        profileComboList.setNestedScrollingEnabled(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            currentUser = user.getUid();
            userNameTV.setText("Welcome, " + user.getEmail() + "!");
            userIdTV.setText("User id: " + currentUser);
        } else {
            userNameTV.setText("not logged in");
        }
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
                    if (currentUser != null && combo != null && currentUser.equals(combo.getUserId())) {
                        comboList.add(combo);
                        comboAdapter.notifyItemInserted(comboList.size());
                        userComboCount.setText("Recipe created: " + comboList.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}