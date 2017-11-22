package io.github.karan.alphafitness.model;

/**
 * A class representing the algorithm to
 * estimate the steps
 *
 * CODE CITATION: http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.WgYI0rbMyEs
 */

@SuppressWarnings("unused")
public class SensorFilter {

    private SensorFilter() {
    }

    static float sum(float[] array) {
        float retValue = 0;

        for (float item: array) retValue += item;

        return retValue;
    }

    public static float[] cross(float[] arrayA, float[] arrayB) {

        float[] returnArray = new float[3];

        returnArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1];
        returnArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2];
        returnArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0];

        return returnArray;
    }

    static float norm(float[] array) {
        float returnValue = 0;

        for (float item: array) {
            returnValue += item * item;
        }

        return (float) Math.sqrt(returnValue);
    }

    static float dot (float[] arrayA, float[] arrayB) {
        return (arrayA[0] * arrayB[0]) + (arrayA[1] * arrayB[1]) + (arrayA[2] * arrayB[2]);
    }

    public static float[] normalize(float[] arrayA) {
        float[] returnArray = new float[arrayA.length];
        float normalized = norm(arrayA);

        for (int i = 0; i < arrayA.length; i++) {
            returnArray[i] = arrayA[i] / normalized;
        }

        return returnArray;
    }


}
