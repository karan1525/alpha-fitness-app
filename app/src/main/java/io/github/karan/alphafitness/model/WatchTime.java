package io.github.karan.alphafitness.model;

/**
 * A class representing the stopwatch class
 * CITATION fROM: ANDROID CONCEPTS BOOK
 */

public class WatchTime {

    private long mStartTime;
    private long mTimeUpdate;
    private long mStoredTime;

    public WatchTime() {
        mStartTime = 0L;
        mTimeUpdate = 0L;
        mStoredTime = 0L;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setTimeUpdate(long timeUpdate) {
        mTimeUpdate = timeUpdate;
    }

    public long getTimeUpdate() {
        return mTimeUpdate;
    }

    public void addStoredTime(long timeInMilliseconds) {
        mStoredTime += timeInMilliseconds;
    }

    public long getStoredTime() {
        return mStoredTime;
    }
}
