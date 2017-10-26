package io.github.karan.alphafitness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Fitness_HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__home_screen);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);

        checkSomething();
    }

    public void checkSomething() {
        Button submitButton = findViewById(R.id.start_workout_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
