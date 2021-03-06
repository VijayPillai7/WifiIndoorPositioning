package com.vijay.wifiindoorpositioning.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.realm.Realm;

import com.vijay.wifiindoorpositioning.adapter.holder.AccessPointSection;
import com.vijay.wifiindoorpositioning.R;
import com.vijay.wifiindoorpositioning.utils.RecyclerItemClickListener;
import com.vijay.wifiindoorpositioning.model.AccessPoint;
import com.vijay.wifiindoorpositioning.model.IndoorProject;


public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemClickListener.OnItemClickListener {

    private RecyclerView pointRV;
    private Button btnAddAp, btnLocateMe;
    private IndoorProject project = new IndoorProject();
    private SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
//    private ReferencePointSection rpSec;
    private AccessPointSection apSec;
    private LinearLayoutManager layoutManager;
    private String projectId;
    private int PERM_REQ_CODE_RP_ACCESS_COARSE_LOCATION = 198;
    private int PERM_REQ_CODE_LM_ACCESS_COARSE_LOCATION = 197;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        projectId = getIntent().getStringExtra("id");
        if (projectId == null) {
            Toast.makeText(getApplicationContext(), "Project Not Found", Toast.LENGTH_LONG).show();
            this.finish();
        }
        Log.i("ProjectDetailActivity", "id>"+projectId);

        Realm realm = Realm.getDefaultInstance();

        project = realm.where(IndoorProject.class).equalTo("id", projectId).findFirst();
//        Log.i("ProjectDetailActivity", "name>"+project.getName());

        initUI();
    }

    private void initUI() {
        pointRV = findViewById(R.id.rv_points);
        btnAddAp = findViewById(R.id.btn_add_ap);
        btnAddAp.setOnClickListener(this);

        btnLocateMe = findViewById(R.id.btn_locate_me);
        btnLocateMe.setOnClickListener(this);
        setCounts();

        SectionParameters sp = new SectionParameters.Builder(R.layout.item_point_details)
                .headerResourceId(R.layout.item_section_details)
                .build();
        apSec = new AccessPointSection(sp);
//        rpSec = new ReferencePointSection(sp);
        apSec.setAccessPoints(project.getAps());
//        rpSec.setReferencePoints(project.getRps());
        sectionAdapter.addSection(apSec);
//        sectionAdapter.addSection(rpSec);
        layoutManager = new LinearLayoutManager(this);
        pointRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        pointRV.setLayoutManager(layoutManager);
        pointRV.setAdapter(sectionAdapter);
        pointRV.addOnItemTouchListener(new RecyclerItemClickListener(this,pointRV, this));
    }

    private void setCounts() {
        String name = project.getName();
        int apCount = project.getAps().size();
//        int rpCount = project.getRps().size();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }
        if (apCount > 0) {
            ((TextView)findViewById(R.id.tv_ap_count)).setText("Access Points:"+String.valueOf(apCount));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sectionAdapter.notifyDataSetChanged();
        setCounts();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnAddAp.getId()) {
            startAddAPActivity("");
        }else if (view.getId() == btnLocateMe.getId()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERM_REQ_CODE_LM_ACCESS_COARSE_LOCATION);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else{
                startAddLocateMeActivity();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PERM_REQ_CODE_LM_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startAddLocateMeActivity();
        }
    }

    private void startAddAPActivity(String apId) {
        Intent intent = new Intent(this, AddOrEditAccessPointActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("apID", apId);
        startActivity(intent);
    }

    private void startAddLocateMeActivity() {
        Intent intent = new Intent(this, LocateMeActivity.class);
        intent.putExtra("projectId", projectId);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this,"onItemClick pos>"+position, Toast.LENGTH_SHORT).show();
        int apsCount = 0;
        if (project.getAps() != null) {
            apsCount = project.getAps().size();
        }
        if (position <= apsCount && position != 0) {//AP section event
            AccessPoint accessPoint = project.getAps().get(position - 1);
            startAddAPActivity(accessPoint.getId());
        }
    }

    @Override
    public void onLongClick(View view, int position) {
//        Toast.makeText(this,"onLongClick pos>"+position, Toast.LENGTH_SHORT).show();
        int apsCount = 0;
        if (project.getAps() != null) {
            apsCount = project.getAps().size();
        }
        if (position <= apsCount && position != 0) {//AP section event
            AccessPoint accessPoint = project.getAps().get(position - 1);
            showDeleteDialog(accessPoint);
        }
    }

    private void showDeleteDialog(final AccessPoint accessPoint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog);
        if (accessPoint != null) {
            builder.setTitle("Delete this Access Point");
            builder.setMessage("Delete "+ accessPoint.getSsid());
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                if (accessPoint != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            accessPoint.deleteFromRealm();
                            refreshList();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void refreshList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sectionAdapter.notifyDataSetChanged();
            }
        });
    }
}