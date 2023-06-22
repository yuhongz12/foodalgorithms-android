package com.example.foodalgorithms.ui.search.cocktail;

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
import com.example.foodalgorithms.ui.cocktail.CocktailDetailFragment;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;

import java.io.InputStream;
import java.util.List;

public class SearchCocktailAdapter  extends RecyclerView.Adapter<SearchCocktailAdapter.CocktailViewHolder> {

    Context context;
    List<ResultCocktailItem> cocktailItemList;

    public SearchCocktailAdapter(Context context, List<ResultCocktailItem> cocktailItemList) {
        this.context = context;
        this.cocktailItemList = cocktailItemList;
    }

    @NonNull
    @Override
    public SearchCocktailAdapter.CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_recipe_item, parent, false);
        SearchCocktailAdapter.CocktailViewHolder myViewHolder = new SearchCocktailAdapter.CocktailViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCocktailAdapter.CocktailViewHolder holder, int position) {
        new SearchCocktailAdapter.DownloadImageTask(holder.imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cocktailItemList.get(position).strDrinkThumb);
        holder.nameTV.setText(cocktailItemList.get(position).strDrink);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewComboDetails = new Intent(context, FoodDetailFragment.class);
                AppCompatActivity activity = (AppCompatActivity) context;
                CocktailDetailFragment cocktailDetailFragment = new CocktailDetailFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("idDrink", cocktailItemList.get(holder.getAdapterPosition()).getIdDrink());
                cocktailDetailFragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, cocktailDetailFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cocktailItemList.size();
    }

    public class CocktailViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView nameTV;
        public CocktailViewHolder(@NonNull View itemView) {
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
