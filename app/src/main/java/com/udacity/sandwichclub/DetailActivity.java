package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private String joinString(List<String> stringsToJoin) {
        // cannot String.join < API 26
        StringBuilder builder = new StringBuilder("");
        for (String string: stringsToJoin) {
            builder.append(string);
            builder.append(", ");
        }

        // shave off trailing comma
        int stringLen = builder.length();
        if (stringLen > 0) {
            builder.delete(stringLen - 2, stringLen);
        }

        return builder.toString();
    }

    private void populateUI(Sandwich sandwich) {
        TextView aka = findViewById(R.id.also_known_tv);
        String akaText = joinString(sandwich.getAlsoKnownAs());
        if (akaText.isEmpty()) {
            aka.setVisibility(View.GONE);
            TextView akaLabel = findViewById(R.id.also_known_tv_label);
            akaLabel.setVisibility(View.GONE);
        } else {
            aka.setText(akaText);
        }

        TextView ingredients = findViewById(R.id.ingredients_tv);
        String ingredientsText = joinString(sandwich.getIngredients());
        if (ingredientsText.isEmpty()) {
            ingredients.setVisibility(View.GONE);
            TextView ingredientsLabel = findViewById(R.id.ingredients_label);
            ingredientsLabel.setVisibility(View.GONE);
        } else {
            ingredients.setText(ingredientsText);
        }

        TextView origin = findViewById(R.id.origin_tv);
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            origin.setVisibility(View.GONE);
            TextView originLabel = findViewById(R.id.place_of_origin_label);
            originLabel.setVisibility(View.GONE);
        } else {
            origin.setText(sandwich.getPlaceOfOrigin());
        }


        TextView description = findViewById(R.id.description_tv);
        if (sandwich.getDescription().isEmpty()) {
            description.setVisibility(View.GONE);
            TextView descriptionLabel = findViewById(R.id.description_tv_label);
            descriptionLabel.setVisibility(View.GONE);
        } else {
            description.setText(sandwich.getDescription());
        }
    }
}
