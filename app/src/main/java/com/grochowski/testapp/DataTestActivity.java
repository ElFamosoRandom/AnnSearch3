package com.grochowski.testapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grochowski.testapp.databaseClass.interestPoint;

public class DataTestActivity extends AppCompatActivity {

    private EditText mIndice1;
    private EditText mIndice2;
    private EditText mIndice3;
    private Button btnAdd;


    private static final int REQUEST_LOCATION = 1;


    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    private static final String TAG = "DataTestActivity";

    protected DatabaseReference databaseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        databaseApp = FirebaseDatabase.getInstance().getReference();

        mIndice1 =  findViewById(R.id.editTextIndice1);
        mIndice2 =  findViewById(R.id.editTextIndice2);
        mIndice3 =  findViewById(R.id.editTextIndice3);

        btnAdd =  findViewById(R.id.addButton);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDbInfos();
            }
        });


    }


    private LatLng getLocation() {
        if (ActivityCompat.checkSelfPermission(DataTestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DataTestActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DataTestActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return null;

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            if (location != null) {
                double lati = location.getLatitude();
                double longi = location.getLongitude();

                LatLng actuallocation = new LatLng(lati, longi);
                return actuallocation;

            } else {

                LatLng noloc = new LatLng(0,0);
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                return noloc;


            }
        }
    }




        private void addDbInfos() {

            LatLng loc = getLocation();
            String indice1 = mIndice1.getText().toString().trim();
            String indice2 = mIndice2.getText().toString().trim();
            String indice3 = mIndice3.getText().toString().trim();
            Double latitude = loc.latitude;
            Double longitude = loc.longitude;


            if (!TextUtils.isEmpty(indice1) || !TextUtils.isEmpty(indice2) || !TextUtils.isEmpty(indice3)) {
                String id = databaseApp.push().getKey();

                interestPoint point = new interestPoint(id, indice1, indice2, indice3, latitude, longitude);
                databaseApp.child(id).setValue(point);

                Toast.makeText(this, "Ajout réussi", Toast.LENGTH_LONG).show();

                startActivity(new Intent(DataTestActivity.this, MainActivity.class));

            } else {
                Toast.makeText(this, "Vous devez remplir tout les champs pour ajouter un point", Toast.LENGTH_LONG).show();
            }
        }



}
