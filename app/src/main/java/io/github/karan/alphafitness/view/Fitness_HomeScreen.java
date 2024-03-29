package io.github.karan.alphafitness.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import io.github.karan.alphafitness.Manifest;
import io.github.karan.alphafitness.R;
import io.github.karan.alphafitness.controller.StepListener;
import io.github.karan.alphafitness.database.UsersDBOperations;
import io.github.karan.alphafitness.model.StepDetector;
import io.github.karan.alphafitness.model.UserData;
import io.github.karan.alphafitness.model.WatchTime;

public class Fitness_HomeScreen extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, StepListener{

    private final int REQUEST_CODE = 0;
    private boolean isFirstLaunch = true;
    private boolean firstClickOfWorkoutButton = true;

    private WatchTime watchTime;
    private long timeInMilliseconds = 0L;
    private Handler mHandler;
    private TextView timeDisplay;

    private TextView distanceTextView;

    private StepDetector simpleStepDetector;
    @Nullable
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int numSteps;

    private UsersDBOperations mUserOps;
    private int numWorkouts = 2;
    private UserData mUserData;

    private final int STEPS_IN_A_MILE = 2000;

    @Nullable
    LocationManager mLocationManager;
    Context mContext;
    private GoogleMap mMap;
    private ArrayList<LatLng> mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__home_screen);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            SupportMapFragment mFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mFrag.getMapAsync(this);
        }

        mContext = this;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListenerGPS);
        mLocationList = new ArrayList<>();
        mUserOps = new UsersDBOperations(this);
        mUserData = new UserData();

        mUserOps.open();

        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, Fitness_DetailsScreen.class);
            startActivity(intent);
            finish();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        timeDisplay = findViewById(R.id.timer_text_view);
        distanceTextView = findViewById(R.id.actual_distance_textView);
        watchTime = new WatchTime();
        mHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUserOps.close();
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        }

        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location l = locManager != null ? locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;

        if (l == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locManager != null ? locManager.getBestProvider(criteria, true) : null;
            assert locManager != null;
            l = locManager.getLastKnownLocation(provider);

            if (isFirstLaunch) {
                isFirstLaunch = false;
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }

        } else {

            if (isFirstLaunch) {
                isFirstLaunch = false;
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }
        }
    }

    public void openProfileScreen(View view) {
        Intent intent = new Intent(this, Fitness_ProfileScreen.class);
        startActivity(intent);
        finish();
    }

    public void startStopWorkoutButtonOperations(View view) {

        Button startStopWorkoutButton = findViewById(R.id.start_workout_button);

        if (firstClickOfWorkoutButton) {
            String STOP_WORKOUT_STRING = "Stop Workout";
            startStopWorkoutButton.setText(STOP_WORKOUT_STRING);
            firstClickOfWorkoutButton = false;

            watchTime.setStartTime(SystemClock.uptimeMillis());
            mHandler.postDelayed(updateTimerRunnable, 20);

            numSteps = 0;
            distanceTextView.setText(R.string.zeroDistance);
            timeDisplay.setText(R.string.zeroTime);
            assert sensorManager != null;
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            mHandler.postDelayed(locationChangedRunnable, 20);

            mHandler.postDelayed(writeToDBRunnable, 300000);

        } else {
            String START_WORKOUT_STRING = "Start Workout";
            startStopWorkoutButton.setText(START_WORKOUT_STRING);
            firstClickOfWorkoutButton = true;

            distanceTextView.setText(R.string.zeroDistance);
            timeDisplay.setText(R.string.zeroTime);

            assert sensorManager != null;
            sensorManager.unregisterListener(this);

            watchTime.addStoredTime(timeInMilliseconds);
            mHandler.removeCallbacks(updateTimerRunnable);
            mHandler.removeCallbacks(locationChangedRunnable);
            mHandler.removeCallbacks(writeToDBRunnable);
        }
    }

    @NonNull
    private Runnable updateTimerRunnable = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - watchTime.getStartTime();
            watchTime.setTimeUpdate(watchTime.getStoredTime() + timeInMilliseconds);

            int time = (int) (watchTime.getTimeUpdate() / 1000);

            int minutes = time / 60;
            int seconds = time % 60;
            int milliseconds = (int) (watchTime.getTimeUpdate() % 1000);

            String timerString = String.format(java.util.Locale.US, "%02d", minutes) + ":"
                    + String.format(java.util.Locale.US, "%02d", seconds) + ":"
                    + String.format(java.util.Locale.US, "%02d", milliseconds);

            timeDisplay.setText(timerString);

            mHandler.postDelayed(this, 0);
        }
    };

    private Runnable writeToDBRunnable = new Runnable() {
        public void run() {

            final int CALORIES_BURNED_PER_2000_STEPS = 120;
            Float distanceRan = (float) numSteps / STEPS_IN_A_MILE;
            Float workoutTime = 5f;
            numWorkouts++;
            Float workoutCalories = (distanceRan / CALORIES_BURNED_PER_2000_STEPS) * 10000;

            mUserData.setmDistance_ran_in_a_week(distanceRan);
            mUserData.setmTime_ran_in_a_week(workoutTime);
            mUserData.setmWorkouts_done_in_a_week(numWorkouts);
            mUserData.setmCalories_burned_in_a_week(workoutCalories);

            mUserOps.addUserData(mUserData);

            mHandler.postDelayed(this, 300000);
        }

    };

    @NonNull
    @SuppressWarnings("unused")
    private Runnable locationChangedRunnable = new Runnable() {
        public void run() {
            LocationListener locationListenerGPS = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull android.location.Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    mLocationList.add(new LatLng(latitude, longitude));

                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    for (int z = 0; z < mLocationList.size(); z++) {
                        LatLng point = mLocationList.get(z);
                        options.add(point);
                    }
                    mMap.addPolyline(options);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            mHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(@NonNull SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]
            );
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        String stringToSet = String.valueOf( (float) numSteps / STEPS_IN_A_MILE);
        distanceTextView.setText(stringToSet);
    }

    @NonNull
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            mLocationList.add(new LatLng(latitude, longitude));

            PolylineOptions options = new PolylineOptions().width(5).color(R.color.polyline_blue).geodesic(true);
            for (int z = 0; z < mLocationList.size(); z++) {
                LatLng point = mLocationList.get(z);
                options.add(point);
            }
            mMap.addPolyline(options);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
