package io.github.karan.alphafitness.model;

/**
 * A class representing all the fields
 * associated with a user in the app
 */

public class User {

    private long mId;
    private String mName;
    private String mGender;
    private float mWeight;

    private String mCurrent_time;
    private float mDistance_ran_in_a_week;
    private float mTime_ran_in_a_week;
    private float mWorkouts_done_in_a_week;
    private float mCalories_burned_in_a_week;

    public User(long id, String name, String gender, float weight) {
        mId = id;
        mName = name;
        mGender = gender;
        mWeight = weight;
    }

    public User(String currentTime, float distance, float time, float workouts, float calories) {
        mCurrent_time = currentTime;
        mDistance_ran_in_a_week = distance;
        mTime_ran_in_a_week = time;
        mWorkouts_done_in_a_week = workouts;
        mCalories_burned_in_a_week = calories;

    }

    public User() {

    }

    public long getmId() {
        return mId;
    }

    public void setmId(long Id) {
        this.mId = Id;
    }

    public String getmName() {
        return mName;
    }

    public String getmGender() {
        return mGender;
    }

    public float getmWeight() {
        return mWeight;
    }

    public void setmName(String name) {
        mName = name;
    }

    public void setmGender(String gender) {
        mGender = gender;
    }

    public void setmWeight(float weight) {
        mWeight = weight;
    }

    public String getmCurrent_time() {
        return mCurrent_time;
    }

    public void setmCurrent_time(String Current_time) {
        this.mCurrent_time = Current_time;
    }

    public float getmDistance_ran_in_a_week() {
        return mDistance_ran_in_a_week;
    }

    public void setmDistance_ran_in_a_week(float Distance_ran_in_a_week) {
        this.mDistance_ran_in_a_week = Distance_ran_in_a_week;
    }

    public float getmTime_ran_in_a_week() {
        return mTime_ran_in_a_week;
    }

    public void setmTime_ran_in_a_week(float Time_ran_in_a_week) {
        this.mTime_ran_in_a_week = Time_ran_in_a_week;
    }

    public float getmWorkouts_done_in_a_week() {
        return mWorkouts_done_in_a_week;
    }

    public void setmWorkouts_done_in_a_week(float Workouts_done_in_a_week) {
        this.mWorkouts_done_in_a_week = Workouts_done_in_a_week;
    }

    public float getmCalories_burned_in_a_week() {
        return mCalories_burned_in_a_week;
    }

    public void setmCalories_burned_in_a_week(float Calories_burned_in_a_week) {
        this.mCalories_burned_in_a_week = Calories_burned_in_a_week;
    }

    public String toString() {
        return "Username: " + getmName() + "\n" +
                "Gender: " + getmGender() + "\n" +
                "Weight: " + getmWeight();
    }

}
