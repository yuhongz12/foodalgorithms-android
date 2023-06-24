package com.example.foodalgorithms.ui.combo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<ResultFoodItem> {

    private Context context;
    private List<ResultFoodItem> resultList;

    private int resourceLayout;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<ResultFoodItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resultList = objects;
        this.resourceLayout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(android.R.layout.simple_list_item_1, null);
        }

        ResultFoodItem p = getItem(position);

//        if (p != null) {
//            TextView tt1 =  v.findViewById(R.id.text1);
//
//            if (tt1 != null) {
//                tt1.setText(p.getStrMeal());
//            }
//
//        }

        return v;
    }
}
