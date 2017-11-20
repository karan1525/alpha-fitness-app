package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.sdsmdg.tastytoast.TastyToast;

import io.github.karan.alphafitness.R;
import io.github.karan.alphafitness.database.UsersDBOperations;
import io.github.karan.alphafitness.model.User;

public class Fitness_ProfileScreen extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "io.github.karan.alphafitness.userID";
    private static final String EXTRA_ADD_UPDATE = "io.github.karan.alphafitness.add_update";

    private EditText nameEditText;
    private Spinner genderSpinner;
    private EditText weightEditText;
    private User newUser;
    private long userId;
    private UsersDBOperations mUserOps;
    private static boolean mIsFirstRun = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__profile_screen);

        newUser = new User();
        nameEditText = findViewById(R.id.user_name_textEdit);
        genderSpinner = findViewById(R.id.gender_spinner);
        weightEditText = findViewById(R.id.weight_edit_text);
        mUserOps = new UsersDBOperations(this);
        mUserOps.open();

        //hardcoded.. Stop recording and then uncomment this
        if (!mIsFirstRun) {
            checkIfUserExists();
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
//        nameEditText.setText(R.string.user_name);
//        String gender = getString(R.string.user_gender);
//        if (gender.equalsIgnoreCase("Male")) {
//            genderSpinner.setSelection(0);
//        } else {
//            genderSpinner.setSelection(1);
//        }
//        weightEditText.setText(R.string.user_weight);

        User myCurrentUser = mUserOps.getUser(newUser.getmId());
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
        newUser = mUserOps.addUser(newUser);

        if (!nameEditText.getText().toString().isEmpty()) {
            TastyToast.makeText(this, "User " + newUser.getmName() + " has been added successfully",
                    TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        } else {
            TastyToast.makeText(this, "User " + newUser.getmName() + " was not added. Try again",
                    TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
        }

            TastyToast.makeText(this, "User adding failed!", TastyToast.LENGTH_LONG,
                    TastyToast.ERROR);


    }
}
