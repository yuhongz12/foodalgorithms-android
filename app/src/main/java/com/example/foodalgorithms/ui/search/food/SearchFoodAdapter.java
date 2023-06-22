package com.example.foodalgorithms.ui.search.food;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;

import java.io.InputStream;
import java.util.List;

public class SearchFoodAdapter extends RecyclerView.Adapter<SearchFoodAdapter.FoodViewHolder>{

    Context context;
    List<ResultFoodItem> foodItemList;

    public SearchFoodAdapter (Context context, List<ResultFoodItem> foodItemList) {
        this.context = context;
        this.foodItemList = foodItemList;
    }


    @NonNull
    @Override
    public SearchFoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_recipe_item, parent, false);
        FoodViewHolder myViewHolder = new FoodViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFoodAdapter.FoodViewHolder holder, int position) {
        new DownloadImageTask(holder.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodItemList.get(position).strMealThumb);
        holder.nameTV.setText(foodItemList.get(position).strMeal);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewComboDetails = new Intent(context, FoodDetailFragment.class);
                AppCompatActivity activity = (AppCompatActivity) context;
                FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                Bundle arguments = new Bundle();
                arguments.putString("idMeal", String.valueOf(foodItemList.get(holder.getAdapterPosition()).getIdMeal()));
                foodDetailFragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView nameTV;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.ComboFoodCard);
            imageView = itemView.findViewById(R.id.RandomFoodImage);
            nameTV = itemView.findViewById(R.id.ComboFoodName);

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
