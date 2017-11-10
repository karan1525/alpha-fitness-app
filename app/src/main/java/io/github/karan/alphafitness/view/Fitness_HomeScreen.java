package io.github.karan.alphafitness.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
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

import io.github.karan.alphafitness.Manifest;
import io.github.karan.alphafitness.R;
import io.github.karan.alphafitness.controller.StepListener;
import io.github.karan.alphafitness.model.StepDetector;
import io.github.karan.alphafitness.model.WatchTime;

public class Fitness_HomeScreen extends FragmentActivity implements OnMapReadyCallback, SensorEventListener, StepListener {

    private final int REQUEST_CODE = 0;
    private boolean isFirstLaunch = true;
    private boolean firstClickOfWorkoutButton = true;

    private WatchTime watchTime;
    private long timeInMilliseconds = 0L;
    private Handler mHandler;
    private TextView timeDisplay;

    private TextView distanceTextView;

    final String STOP_WORKOUT_STRING = "Stop Workout";
    final String START_WORKOUT_STRING = "Start Workout";

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final String TEXT_NUM_STEPS = "Number of steps";
    private int numSteps;

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

        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, Fitness_DetailsScreen.class);
            startActivity(intent);
            finish();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        timeDisplay = findViewById(R.id.timer_text_view);
        distanceTextView = findViewById(R.id.actual_distance_textView);
        watchTime = new WatchTime();
        mHandler = new Handler();
    }

    public void onMapReady(GoogleMap googleMap) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap.setMyLocationEnabled(true);

        }

        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location l = locManager != null ? locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;

        if (l == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locManager.getBestProvider(criteria, true);
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
            startStopWorkoutButton.setText(STOP_WORKOUT_STRING);
            firstClickOfWorkoutButton = false;

            watchTime.setStartTime(SystemClock.uptimeMillis());
            mHandler.postDelayed(updateTimerRunnable, 20);

            numSteps = 0;
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        } else {
            startStopWorkoutButton.setText(START_WORKOUT_STRING);
            firstClickOfWorkoutButton = true;

            sensorManager.unregisterListener(this);

            watchTime.addStoredTime(timeInMilliseconds);
            mHandler.removeCallbacks(updateTimerRunnable);
        }
    }

    private Runnable updateTimerRunnable = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - watchTime.getStartTime();
            watchTime.setTimeUpdate(watchTime.getStoredTime() + timeInMilliseconds);

            int time = (int) (watchTime.getTimeUpdate() / 1000);

            int minutes = time / 60;
            int seconds = time % 60;
            int milliseconds = (int) (watchTime.getTimeUpdate() % 1000);

            String timerString = String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%02d", milliseconds);

            timeDisplay.setText(timerString);

            mHandler.postDelayed(this, 0);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]
            );
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        String stringToSet = TEXT_NUM_STEPS + numSteps;
        distanceTextView.setText(stringToSet);
    }
}
