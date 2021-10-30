package com.vijay.wifiindoorpositioning.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

import com.vijay.wifiindoorpositioning.R;
import com.vijay.wifiindoorpositioning.model.AccessPoint;
import com.vijay.wifiindoorpositioning.model.AccessPointCustomModel;
import com.vijay.wifiindoorpositioning.model.MatchedResultModel;
import com.vijay.wifiindoorpositioning.model.WayFindingSample;


public class LocateMeActivity extends AppCompatActivity {

    private List<WayFindingSample> wayFindingSamples = new ArrayList<>();
    private List<AccessPoint> accessPointsLists = new ArrayList<>();
    private List<AccessPointCustomModel> accessPointCustomModels =  new ArrayList<>();
    private TextView wpid,coordinates;
    private ArrayList<MatchedResultModel> matchedResultModels = new ArrayList<>();

    private Double finalResult = Double.MAX_VALUE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_me);


        wpid = findViewById(R.id.loc_me_wpid);
        coordinates = findViewById(R.id.loc_me_cor);

        convertingCSVTOArray();
        retriveingDataFromRealm();


        findingResult();
    }

    private void findingResult() {
        for(AccessPoint accessPoint : accessPointsLists){
            AccessPointCustomModel accessPointCustomModel = new AccessPointCustomModel();
            accessPointCustomModel.setBSSID(accessPoint.getSsid());
            accessPointCustomModel.setFrq(accessPoint.getFrq());
            accessPointCustomModel.setLevel(accessPoint.getLevel());

            accessPointCustomModels.add(accessPointCustomModel);
        }


        for(AccessPointCustomModel accessPointCustomModel : accessPointCustomModels){
            for(WayFindingSample wayFindingSample : wayFindingSamples){

                if(accessPointCustomModel.getBSSID().equals(wayFindingSample.getBSSID())){

                    Double accessPointLevel = accessPointCustomModel.getLevel();
                    Double wayFindingLevel = wayFindingSample.getLevel();
                    Double accessPointFreq = accessPointCustomModel.getFrq();
                    Double wayFindingFreq = wayFindingSample.getFrq();

                    Double resLevel = accessPointLevel - wayFindingLevel;
                    resLevel *= resLevel;

                    Double resFreq = accessPointFreq - wayFindingFreq;
                    resFreq *= resFreq;

                    Double tempResult = Math.sqrt(resLevel+resFreq);
                    matchedResultModels.add(new MatchedResultModel(
                            wayFindingSample.getBSSID(),
                            wayFindingSample.getWPID()+"",
                            wayFindingSample.getLat(),
                            wayFindingSample.getLon(),
                            tempResult));
                }
            }
        }

        Collections.sort(matchedResultModels);

        int arr[] = new int[101];

        for(int i=0;i<accessPointsLists.size();i++){
            arr[Integer.parseInt(matchedResultModels.get(i).getWpid())]++;
        }

        int max = 0,index=0;

        for(int i=0;i<accessPointsLists.size();i++){
            int x = Integer.parseInt(matchedResultModels.get(i).getWpid());
            Log.v("x",x+"");
            if(max<arr[x]){
                max = arr[x];
                index = x;
            }
        }

        MatchedResultModel ansResult = new MatchedResultModel();
        for(int i=0;i<matchedResultModels.size();i++){
            if(matchedResultModels.get(i).getWpid().equals(index+"")){
                ansResult.setCalResult(matchedResultModels.get(i).getCalResult());
                ansResult.setBssid(matchedResultModels.get(i).getBssid());
                ansResult.setWpid(matchedResultModels.get(i).getWpid());
                ansResult.setLon(matchedResultModels.get(i).getLon());
                ansResult.setLat(matchedResultModels.get(i).getLat());

                break;
            }
        }
        wpid.setText(ansResult.getWpid());
        coordinates.setText("("+ansResult.getLat()+","+ansResult.getLon()+")");

        for(int i=0;i<matchedResultModels.size();i++){
            Log.v("res",i+" "+matchedResultModels.get(i).getCalResult()+" "+
                    " "+matchedResultModels.get(i).getLat()+" "+
                    " "+matchedResultModels.get(i).getLon()+" "+
                    " "+matchedResultModels.get(i).getBssid()+" "+
                    " "+matchedResultModels.get(i).getWpid());
        }
    }

    private void retriveingDataFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        accessPointsLists = realm.where(AccessPoint.class).findAll();

    }

    private void convertingCSVTOArray() {
        wayFindingSamples = new ArrayList<>();

        InputStream is = getResources().openRawResource(R.raw.way_finding);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";

        try {
            reader.readLine();
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
}
