package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.karan.alphafitness.R;

public class Fitness_ProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__profile_screen);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Fitness_HomeScreen.class);
        startActivity(intent);
        finish();
    }
}
