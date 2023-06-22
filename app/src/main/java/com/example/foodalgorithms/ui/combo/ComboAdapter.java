package com.example.foodalgorithms.ui.combo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodalgorithms.R;

import java.util.ArrayList;
import java.util.List;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {
    Context context;
    List<Combo> comboList = new ArrayList<>();

    public ComboAdapter(Context context, List<Combo> comboList) {
        this.comboList = comboList;
        this.context = context;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.combo_item, parent, false);
        ComboViewHolder myViewHolder = new ComboViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        holder.comboItemName.setText(comboList.get(position).getComboName());
        holder.comboItemFoodName.setText(comboList.get(position).getComboFood());
        holder.comboItemCocktailName.setText(comboList.get(position).getComboCocktail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewComboDetails = new Intent(context, ComboDetails.class);
                AppCompatActivity activity = (AppCompatActivity) context;
                ComboDetails comboDetails = new ComboDetails();
                Bundle arguments = new Bundle();
                arguments.putString("comboId", "feafewafwaefawefwe");
                comboDetails.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, comboDetails).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return comboList.size();
    }

    public class ComboViewHolder extends RecyclerView.ViewHolder {

        TextView comboItemName;
        TextView comboItemFoodName;
        TextView comboItemCocktailName;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            comboItemName = itemView.findViewById(R.id.ComboItemName);
            comboItemFoodName = itemView.findViewById(R.id.ComboItemFoodName);
            comboItemCocktailName = itemView.findViewById(R.id.ComboItemCocktailName);
        }
    }
}
