package com.vijay.wifiindoorpositioning.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

import com.vijay.wifiindoorpositioning.R;
import com.vijay.wifiindoorpositioning.model.AccessPoint;
import com.vijay.wifiindoorpositioning.model.IndoorProject;


public class AddOrEditAccessPointActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addAp, btnScanAP;
    private EditText etName, etDesc, etFrq,etLevel;
    public String projectId="", apID="";
    private boolean isEdit = false;
    private AccessPoint apToBeEdited;
    private int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 199;
    private static final int REQ_CODE = 1212;//this is always positive

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_access_point);

        projectId = getIntent().getStringExtra("projectId");
        if(projectId == null) {
            Toast.makeText(this, "Access point not found", Toast.LENGTH_LONG).show();
            this.finish();
        }

        apID = getIntent().getStringExtra("apID");
        initUI();
        if (apID.equals("")) {
            isEdit = false;
        } else {
            isEdit = true;
            addAp.setText("Save");
        }

        if (isEdit)
        setUpEditMode();
    }

    private void setUpEditMode() {
        Realm realm = Realm.getDefaultInstance();
        apToBeEdited = realm.where(AccessPoint.class).equalTo("id", apID).findFirst();
        setValuesToFields(apToBeEdited);
    }

    private void setValuesToFields(AccessPoint accessPoint) {
        etName.setText(accessPoint.getSsid());
        etFrq.setText(String.valueOf(accessPoint.getFrq()));
        etLevel.setText(accessPoint.getLevel()+"");
    }

    private void initUI() {
        etName = findViewById(R.id.et_ap_name);
        etDesc = findViewById(R.id.et_ap_desc);
        etFrq = findViewById(R.id.et_frq);
        etLevel = findViewById(R.id.et_level);
        addAp = findViewById(R.id.bn_ap_create);
        addAp.setOnClickListener(this);
        btnScanAP = findViewById(R.id.bn_ap_scan);
        btnScanAP.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addAp.getId()) {
            final String text = etName.getText().toString().trim();
            final String x = etFrq.getText().toString().trim();
            final String mac = etLevel.getText().toString().trim();
            final boolean isEditMode = isEdit;

            if (text.isEmpty()) {
                Snackbar.make(addAp, "Provide Access Point Name", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                // Obtain a Realm instance
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                IndoorProject project = realm.where(IndoorProject.class).equalTo("id", projectId).findFirst();

                Log.v("AddOr",projectId);


                if (isEditMode) {
                    apToBeEdited.setSsid(text);
                    apToBeEdited.setFrq(Double.valueOf(x));
                    apToBeEdited.setLevel(Double.valueOf(mac));
                } else {
                    AccessPoint accessPoint = realm.createObject(AccessPoint.class, UUID.randomUUID().toString());
                    accessPoint.setBssid(mac);
                    accessPoint.setCreatedAt(new Date());
                    accessPoint.setFrq(Double.valueOf(x));
                    accessPoint.setSsid(text);
                    accessPoint.setLevel(Double.valueOf(mac));
                    project.getAps().add(accessPoint);
                }
                realm.commitTransaction();
                this.finish();
            }
        } else if (view.getId() == btnScanAP.getId()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

            } else{
                startSearchWifiActivity();
            }
        }
    }

    private void startSearchWifiActivity() {
        Intent intent = new Intent(this, SearchWifiAccessPointActivity.class);
        intent.putExtra("projectId",projectId);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startSearchWifiActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            AccessPoint accessPoint = (AccessPoint) data.getParcelableExtra("accessPoint");
            setValuesToFields(accessPoint);
        }
    }
}
