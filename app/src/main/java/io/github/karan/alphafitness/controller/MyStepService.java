package io.github.karan.alphafitness.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A class to calculate steps and run the timer
 * even when the app goes to sleep
 */

public class MyStepService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
