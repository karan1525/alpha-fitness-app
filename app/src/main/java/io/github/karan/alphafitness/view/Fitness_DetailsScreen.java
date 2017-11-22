package io.github.karan.alphafitness.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.github.karan.alphafitness.R;
import io.github.karan.alphafitness.database.UsersDBOperations;
import io.github.karan.alphafitness.model.UserData;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;

public class Fitness_DetailsScreen extends FragmentActivity {

    private UsersDBOperations mUserOps;
    private CombinedChart mChart;

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

        mUserOps = new UsersDBOperations(this);
        mUserOps.open();

        mChart = findViewById(R.id.chart);

        setupChart();

    }

    @Override
    public void onDestroy() {
        mUserOps.close();
        super.onDestroy();
    }

    public void setupChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR,DrawOrder.LINE });

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        CombinedData data = new CombinedData();
        data.setData(getLineData());
        data.setData(getBarData());

        mChart.setData(data);
        mChart.animateXY(2000,2000);
        mChart.invalidate();

    }

    public List<Float> getCalorieData() {

        List<Float> returnList = new ArrayList<>();

        List <UserData> allData;
        allData = mUserOps.getAllData();

        for (UserData data: allData) {
            returnList.add(data.getmCalories_burned_in_a_week());
        }

        return returnList;
    }

    public List<Float> getDistanceData() {

        List<Float> returnList = new ArrayList<>();
        final int STEPS_IN_A_MILE = 2000;

        List <UserData> allData;
        allData = mUserOps.getAllData();

        for (UserData data: allData) {
            //get steps covered every 5 minutes
            returnList.add(data.getmDistance_ran_in_a_week() * STEPS_IN_A_MILE);
        }

        return returnList;

    }

    private LineData getLineData() {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        List<Float> calorieData = getCalorieData();

        for (int index = 0; index < calorieData.size(); index++) {
            //calories offset by 10 as it displays small if no offset
            entries.add(new Entry(index, calorieData.get(index) * 10));
        }

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.BLACK);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);

        d.addDataSet(set);

        return d;
    }

    private BarData getBarData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<Float> distanceData = getDistanceData();

        for (int index = 0; index < distanceData.size(); index++) {
            entries.add(new BarEntry(index, distanceData.get(index)));
        }

        BarDataSet set1 = new BarDataSet(entries, "Bar");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        return new BarData(set1);
    }


}
