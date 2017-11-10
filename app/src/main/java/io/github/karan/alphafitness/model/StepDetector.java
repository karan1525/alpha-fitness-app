package io.github.karan.alphafitness.model;

import io.github.karan.alphafitness.controller.StepListener;

/**
 * A class to calculate the steps
 * using the algorithms defined in the model package
 *
 * CODE CITATION: http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.WgYI0rbMyEs
 */

public class StepDetector {

    private static final int ACCEL_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;

    //lower threshold if needed
    private static final float STEP_THRESHOLD = 50f;
    private static final int STEP_DELAY_NS = 250000000;

    private int mAccelRingCounter = 0;
    private float[] mAccelRingX = new float[ACCEL_RING_SIZE];
    private float[] mAccelRingY = new float[ACCEL_RING_SIZE];
    private float[] mAccelRingZ = new float[ACCEL_RING_SIZE];

    private int mVelRingCounter = 0;
    private float[] mVelRing = new float[VEL_RING_SIZE];
    private long mLastStepTimeNs = 0;
    private float mOldVelocityEstimate = 0;

    private StepListener mStepListener;

    public void registerListener(StepListener listener) {
        mStepListener = listener;
    }

    public void updateAccel(long timeNs, float x, float y, float z) {

        float[] currentAccel = new float[3];
        currentAccel[0] = x;
        currentAccel[1] = y;
        currentAccel[2] = z;

        //Update our guess of where the global z vector is
        mAccelRingCounter++;
        mAccelRingX[mAccelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
        mAccelRingY[mAccelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
        mAccelRingZ[mAccelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

        float[] worldZ = new float[3];

        worldZ[0] = SensorFilter.sum(mAccelRingX) / Math.min(mAccelRingCounter, ACCEL_RING_SIZE);
        worldZ[1] = SensorFilter.sum(mAccelRingY) / Math.min(mAccelRingCounter, ACCEL_RING_SIZE);
        worldZ[2] = SensorFilter.sum(mAccelRingZ) / Math.min(mAccelRingCounter, ACCEL_RING_SIZE);

        float normalization_factor = SensorFilter.norm(worldZ);

        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;

        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        mVelRingCounter++;
        mVelRing[mVelRingCounter % VEL_RING_SIZE] = currentZ;

        float velocityEstimate = SensorFilter.sum(mVelRing);

        if (velocityEstimate > STEP_THRESHOLD && mOldVelocityEstimate <= STEP_THRESHOLD &&
                (timeNs - mLastStepTimeNs > STEP_DELAY_NS)) {

            mStepListener.step(timeNs);
            mLastStepTimeNs = timeNs;
        }

        mOldVelocityEstimate = velocityEstimate;
    }

}
