package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import io.github.karan.alphafitness.R;
import io.github.karan.alphafitness.database.UsersDBOperations;
import io.github.karan.alphafitness.model.User;
import io.github.karan.alphafitness.model.UserData;

public class Fitness_ProfileScreen extends AppCompatActivity {

    private EditText nameEditText;
    private Spinner genderSpinner;
    private EditText weightEditText;
    private User newUser;
    private static User mSavedUser;
    private UsersDBOperations mUserOps;
    private static boolean mIsFirstRun = true;

    private TextView distance_content_textView;
    private TextView time_content_textView;
    private TextView workout_content_textView;
    private TextView calories_burned_content_textView;

    private TextView total_distance_content_textView;
    private TextView total_time_content_textView;
    private TextView total_workout_content_textview;
    private TextView total_calories_burned_content_textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__profile_screen);

        newUser = new User();
        nameEditText = findViewById(R.id.user_name_textEdit);
        genderSpinner = findViewById(R.id.gender_spinner);
        weightEditText = findViewById(R.id.weight_edit_text);

        distance_content_textView = findViewById(R.id.distance_content_textView);
        time_content_textView = findViewById(R.id.time_content_textView);
        workout_content_textView = findViewById(R.id.workout_content_textView);
        calories_burned_content_textView = findViewById(R.id.calories_burned_content_textView);

        total_distance_content_textView = findViewById(R.id._total_distance_content_textView);
        total_time_content_textView = findViewById(R.id.total_time_content_textView);
        total_workout_content_textview = findViewById(R.id.total_workout_content_textView);
        total_calories_burned_content_textView = findViewById(R.id.total_calories_burned_content_textView);

        mUserOps = new UsersDBOperations(this);
        mUserOps.open();

        mSavedUser = mUserOps.getUser(1);

        if (!mIsFirstRun) {
            checkIfUserExists();
            getWeeklyData();
            getAllData();
        }

    }

    @Override
    public void onBackPressed() {

        if (mIsFirstRun) {
            mIsFirstRun = false;
            saveUserInformation();
        }
        Intent intent = new Intent(this, Fitness_HomeScreen.class);
        startActivity(intent);
        finish();
    }

    private void checkIfUserExists() {
        User myCurrentUser = mUserOps.getUser(1);
        nameEditText.setText(myCurrentUser.getmName());
        String gender = myCurrentUser.getmGender();
        if (gender.equalsIgnoreCase("Male")) {
            genderSpinner.setSelection(0);
        } else {
            genderSpinner.setSelection(1);
        }
        weightEditText.setText(String.valueOf(myCurrentUser.getmWeight()));

        mIsFirstRun = false;
    }

    private void saveUserInformation() {
        if (!nameEditText.getText().toString().isEmpty()) {
            newUser.setmName(nameEditText.getText().toString());
        }
        newUser.setmGender(genderSpinner.getSelectedItem().toString());

        if (!weightEditText.getText().toString().isEmpty()) {
            newUser.setmWeight(Float.parseFloat(weightEditText.getText().toString()));
        }

        if((!mSavedUser.getmName().equalsIgnoreCase(newUser.getmName())) && !
                nameEditText.getText().toString().isEmpty()) {
            mSavedUser = mUserOps.addUser(newUser);
            TastyToast.makeText(this, "User " + newUser.getmName() + " has been added successfully",
                    TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        } else if (nameEditText.getText().toString().isEmpty()) {
            TastyToast.makeText(this, "User adding failed! Try again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else {
            TastyToast.makeText(this, "User " + newUser.getmName() + " was already in the DB. Try again",
                    TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
        }

    }

    public void getWeeklyData() {
        UserData userData = mUserOps.getUserData(1);

        String distance = String.valueOf(userData.getmDistance_ran_in_a_week()) + " miles";
        String time = timeConvert((int) userData.getmTime_ran_in_a_week());
        String workout = String.valueOf((int) userData.getmWorkouts_done_in_a_week()) + " times";
        String calories = String.valueOf(userData.getmCalories_burned_in_a_week()) + " calories";

        distance_content_textView.setText(distance);
        time_content_textView.setText(time);
        workout_content_textView.setText(workout);
        calories_burned_content_textView.setText(calories);

    }

    public void getAllData() {

        List<UserData> allData;

        allData = mUserOps.getAllData();
        float totalDistance = 0;
        float totalTime = 0;
        int totalWorkouts = 0;
        float totalCalories = 0;

        for (UserData data: allData) {
            //distance
            totalDistance += data.getmDistance_ran_in_a_week();
            Log.v("TestDistance", String.valueOf(totalDistance));
            //time
            totalTime += data.getmTime_ran_in_a_week();
            Log.v("TestTime", String.valueOf(totalTime));
            //workouts
            totalWorkouts = (int) data.getmWorkouts_done_in_a_week();
            Log.v("TestWorkouts", String.valueOf(totalWorkouts));
            //calories
            totalCalories += data.getmCalories_burned_in_a_week();
            Log.v("TestCalories", String.valueOf(totalCalories));
        }

        String timeInString = timeConvert( (int) totalTime);

        String distance = String.valueOf(totalDistance) + " miles";
        String workouts = String.valueOf(totalWorkouts) + " times";
        String calories = String.valueOf(totalCalories) + " calories";

        total_distance_content_textView.setText(distance);
        total_time_content_textView.setText(timeInString);
        total_workout_content_textview.setText(workouts);
        total_calories_burned_content_textView.setText(calories);

    }

    private String timeConvert(int timeInMinutes) {
        int seconds = timeInMinutes * 60;

        int minutes = (seconds % 3600) / 60;
        int hours = seconds / 3600;
        int days = seconds / 86400;
        seconds = (seconds % 3600) % 60;

        return days + " day " + hours + " hr " + minutes + " min " + seconds + " sec";
    }
}
