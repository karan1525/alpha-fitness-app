package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.github.karan.alphafitness.R;

public class Fitness_DetailsScreen extends FragmentActivity {

    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    private double latitude = 37.484532;
    private double longitude = -122.147448;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness__details_screen);

        Configuration configuration = getResources().getConfiguration();

        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, Fitness_HomeScreen.class);
            startActivity(intent);
            finish();
        }

        LineChart chart = findViewById(R.id.chart);
        //test data
        latLngs.add(new LatLng(latitude, longitude));
        latLngs.add(new LatLng(latitude + 1, longitude + 1));
        latLngs.add(new LatLng(latitude + 2, longitude + 2));
        latLngs.add(new LatLng(latitude + 3, longitude + 3));
        latLngs.add(new LatLng(latitude + 4, longitude + 4));
        latLngs.add(new LatLng(latitude + 5, longitude + 5));
        latLngs.add(new LatLng(latitude + 6, longitude + 6));
        latLngs.add(new LatLng(latitude + 7, longitude + 7));
        latLngs.add(new LatLng(latitude + 8, longitude + 8));

        List<Entry> entries = new ArrayList<Entry>();

        for (LatLng data : latLngs) {
            // turn your data into Entry objects
            entries.add(new Entry( (float) data.latitude, (float) data.longitude));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.rgb(255, 150, 101));
        dataSet.setValueTextColor(Color.rgb(255,255,255));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}
