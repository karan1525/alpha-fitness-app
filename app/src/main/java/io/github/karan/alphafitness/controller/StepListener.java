package io.github.karan.alphafitness.controller;

/**
 * An interface for the step counting algorithm
 *
 * CODE CITATION: http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.WgYI0rbMyEs
 */

public interface StepListener {

    void step(long timeNs);

}
