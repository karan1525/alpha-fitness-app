package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import io.github.karan.alphafitness.R;

public class Fitness_DetailsScreen extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__details_screen);

        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, Fitness_HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
