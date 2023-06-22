package com.example.foodalgorithms.ui.food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.foodalgorithms.R;

import java.util.Arrays;

public class FoodDetailFragment extends Fragment {

    Food food = new Food(52859, "Key Lime Pie", "American", "Dessert",
            Arrays.asList("Digestive Biscuits", "Butter", "Condensed Milk", "Egg Yolks", "Lime", "Double Cream", "Icing Sugar", "Lime"),
            Arrays.asList("300g","150g", "400g", "3", "4", "300ml ", "1 tbls", "to serve"),
            "Heat the oven to 160C/fan 140C/gas 3. Whizz the biscuits to crumbs in a food processor (or put in a strong plastic bag and bash with a rolling pin). Mix with the melted butter and press into the base and up the sides of a 22cm loose-based tart tin. Bake in the oven for 10 minutes. Remove and cool.\r\nPut the egg yolks in a large bowl and whisk for a minute with electric beaters. Add the condensed milk and whisk for 3 minutes then add the zest and juice and whisk again for 3 minutes. Pour the filling into the cooled base then put back in the oven for 15 minutes. Cool then chill for at least 3 hours or overnight if you like.\r\nWhen you are ready to serve, carefully remove the pie from the tin and put on a serving plate. To decorate, softly whip together the cream and icing sugar. Dollop or pipe the cream onto the top of the pie and finish with extra lime zest.",
            "https://www.themealdb.com/images/media/meals/qpqtuu1511386216.jpg",
            "https://www.youtube.com/watch?v=q4Rz7tUkX9A");

    TextView foodDetailsName;
    TextView foodDetailsCategory;
    TextView foodDetailsCountry;
    TextView foodDetailsIngredientsText;
    TextView foodDetailsDirectionsText;
    ImageView foodDetailsImage;
    WebView foodDetailsVideo;

    String comboId;

    public static FoodDetailFragment newInstance() {
        return new FoodDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            comboId = bundle.getString("idMeal");
//
//        }
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        foodDetailsName = view.findViewById(R.id.FoodDetailsName);
        foodDetailsCategory = view.findViewById(R.id.FoodDetailCategory);
        foodDetailsCountry = view.findViewById(R.id.FoodDetailsCountry);
        foodDetailsIngredientsText = view.findViewById(R.id.FoodDetailsIngredientsText);
        foodDetailsDirectionsText = view.findViewById(R.id.FoodDetailsInstructionText);
        foodDetailsImage = view.findViewById(R.id.FoodDetailsImage);
        foodDetailsVideo = view.findViewById(R.id.FoodDetailsVideoView);

        foodDetailsName.setText(food.getName());
        foodDetailsCountry.setText(food.getCountry());
        foodDetailsCategory.setText(food.getCategory());
        foodDetailsDirectionsText.setText(food.getDirection());

      //  foodDetailsVideo.loadUrl("file:///android_asset/dinner_menu.png");
        foodDetailsVideo.setWebViewClient(new WebViewClient());
        foodDetailsVideo.loadUrl(food.getVideoURL());

        return view;
    }


}