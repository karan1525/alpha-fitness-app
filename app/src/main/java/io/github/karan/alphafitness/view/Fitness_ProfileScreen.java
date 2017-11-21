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

    private EditText nameEditText;
    private Spinner genderSpinner;
    private EditText weightEditText;
    private User newUser;
    private User mSavedUser;
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

        mSavedUser = mUserOps.getUser(1);

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
}
