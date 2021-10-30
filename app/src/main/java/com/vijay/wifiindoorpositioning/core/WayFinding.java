package com.vijay.wifiindoorpositioning.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.vijay.wifiindoorpositioning.R;
import com.vijay.wifiindoorpositioning.model.WayFindingSample;

public class WayFinding extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readWayFindingData();
    }

    private List<WayFindingSample> wayFindingSamples = new ArrayList<>();
    private void readWayFindingData() {
        InputStream is = getResources().openRawResource(R.raw.way_finding);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            while ((line = reader.readLine()) != null)
            {
                String[] tokens=line.split(",");

                WayFindingSample sample = new WayFindingSample();
                sample.setWPID(Integer.parseInt(tokens[0]));
                sample.setBSSID(tokens[1]);
                sample.setFrq(Double.parseDouble(tokens[2]));
                sample.setLevel(Double.parseDouble(tokens[3]));
                sample.setLat(Double.parseDouble(tokens[4]));
                sample.setLon(Double.parseDouble(tokens[5]));

                wayFindingSamples.add(sample);
            }
            Log.i("size of the csv file", wayFindingSamples.size()+"");

        }  catch (IOException e) {
            Log.v("MyActivity","Error reading data file on line"+line,e);
            e.printStackTrace();
        }
    }

    public List<WayFindingSample> getWayFindingSamples() {
        return wayFindingSamples;
    }
}
