package com.grochowski.testapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grochowski.testapp.databaseClass.interestPoint;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LatLngBounds mMapBoundary;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private static final long MIN_TIME = 40;
    private static final float MIN_DISTANCE = 100;

    private Button btnStart;
    private Button btnVerif;
    private ConstraintLayout indiceLayout;
    private TextView mIndice1;
    private TextView mIndice2;
    private TextView mIndice3;
    private int countIndice = 1;

    protected DatabaseReference databaseApp;

    private static final int REQUEST_LOCATION = 1;

    public static class Chasse {

        public String id;
        public String indice1;
        public String indice2;
        public String indice3;
        public Double latitude;
        public Double longitude;
        public String name;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        btnStart = findViewById(R.id.startBtn);
        btnVerif = findViewById(R.id.verifBtn);

        indiceLayout = findViewById(R.id.layoutIndice);
        mIndice1 = findViewById(R.id.textViewIndice1);
        mIndice2 = findViewById(R.id.textViewIndice2);
        mIndice3 = findViewById(R.id.textViewIndice3);

        databaseApp = FirebaseDatabase.getInstance().getReference().child("interestPoint");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                    databaseApp.child("-LalkRIDbrlSpEgSMnlJ").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Chasse chasseactive = dataSnapshot.getValue(Chasse.class);
                            String firstindice = "Indice 1 : " + chasseactive.indice1;
                            //String secondindice = "Indice 2 : " + chasseactive.indice2;
                            String thirdindice = "Indice 3 : " + chasseactive.indice3;

                            //Toast.makeText(MapsActivity.this, firstindice + secondindice + thirdindice, Toast.LENGTH_SHORT).show();



                            LatLng actualLocat = getLocation();
                            Double lat = actualLocat.latitude;
                            Double longi = actualLocat.longitude;

                            if (countIndice == 1) {

                                mIndice1.setText(firstindice);
                                indiceLayout.setVisibility(v.VISIBLE);
                                btnVerif.setVisibility(v.VISIBLE);
                                countIndice += 1;
                                btnStart.setVisibility(v.INVISIBLE);
                                //btnStart.setText("Vérifier ma position");
                            }
                            else if (countIndice == 2) {
                                if (chasseactive.latitude == lat + 0.000001 && chasseactive.latitude >= lat - 0.000001) {
                                    if (chasseactive.longitude == longi + 0.000001 && chasseactive.longitude >= longi - 0.000001) {
                                        Toast.makeText(MapsActivity.this, "Vous avez trouvé le bon point, bravo", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String secondindice = "Indice 2 : " + chasseactive.indice2;
                                        mIndice2.setText(secondindice);
                                        mIndice2.setVisibility(v.VISIBLE);
                                        countIndice += 1;
                                    }
                                } else {

                                    mIndice2.setVisibility(v.VISIBLE);
                                    countIndice += 1;

                                }
                            }


                            //System.out.println(profile.getUsername());
                        }





                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }

                    });


                }


        });

        btnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                databaseApp.child("-LalkRIDbrlSpEgSMnlJ").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Chasse chasseactive = dataSnapshot.getValue(Chasse.class);
                        String secondindice = "Indice 2 : " + chasseactive.indice2;
                        String thirdindice = "Indice 3 : " + chasseactive.indice3;
                        Double latiacti = chasseactive.latitude;
                        Double longiacti = chasseactive.longitude;




                        LatLng actualLocat = getLocation();
                        Double lat = actualLocat.latitude;
                        Double longi = actualLocat.longitude;

                        String msg = latiacti.toString() +" " + longiacti.toString()+ " " + lat.toString()+ " " + longi.toString();

                        Toast.makeText(MapsActivity.this, msg , Toast.LENGTH_SHORT).show();

                        if (countIndice == 2) {
                            if (latiacti <= lat + 0.000001 && latiacti >= lat - 0.000001) {
                                if (longiacti <= longi + 0.000001 && longiacti >= longi - 0.000001) {
                                    Toast.makeText(MapsActivity.this, "Vous avez trouvé le bon point, bravo", Toast.LENGTH_SHORT).show();
                                } else {

                                    mIndice2.setText(secondindice);
                                    mIndice2.setVisibility(v.VISIBLE);
                                    countIndice += 1;
                                }
                                //mIndice2.setText(secondindice);
                                //mIndice2.setVisibility(v.VISIBLE);
                                //indiceLayout.setVisibility(v.VISIBLE);
                                //btnVerif.setVisibility(v.VISIBLE);
                                //countIndice += 1;
                                //btnStart.setVisibility(v.INVISIBLE);
                                //btnStart.setText("Vérifier ma position");
                            }
                            else{
                                mIndice2.setText(secondindice);
                                mIndice2.setVisibility(v.VISIBLE);
                                countIndice += 1;
                            }

                        }
                        else if (countIndice == 3) {

                            mIndice3.setText(thirdindice);
                            mIndice3.setVisibility(v.VISIBLE);
                            //indiceLayout.setVisibility(v.VISIBLE);
                            //btnVerif.setVisibility(v.VISIBLE);
                            countIndice += 1;
                            //btnStart.setVisibility(v.INVISIBLE);
                            //btnStart.setText("Vérifier ma position");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }



    private LatLng getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnowLocation: called");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    Log.d(TAG, "CnComplete : latitude: " + location.getLatitude());
                    Log.d(TAG, "CnComplete : longitude: " + location.getLongitude());

                    //LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());

                }
            }

        });

    }


    /*private void setCameraView() {

        getLastKnownLocation()
        LatLng actualPosition = new LatLng(location.getLatitude(), location.getLongitude());

        double bottomBoundary = location.getLatitude() - 0.1;
        double leftBoundary = location.getLongitude() - 0.1;
        double topBoundary = location.getLatitude() + 0.1;
        double rightBoundary = location.getLongitude() +0.1;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary,leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary,0));

    }*/



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap = googleMap;
    }

    public void distanceFromActualPosition(Location location){

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,18);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
