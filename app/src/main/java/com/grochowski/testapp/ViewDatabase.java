package com.grochowski.testapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDatabase extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference dbReference;
    private String pointID;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_database_layout);

        mListView = (ListView) findViewById(R.id.listview);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    private String name;
    private String statut;
    private String imageUrl;
    private String indice1;
    private String indice2;
    private String indice3;
    private String latitude;
    private String longitude;
    */

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            PointInformation pInfo = new PointInformation();
            pInfo.setName(ds.child(pointID).getValue(PointInformation.class).getName());
            pInfo.setStatut(ds.child(pointID).getValue(PointInformation.class).getStatut());
            pInfo.setImageUrl(ds.child(pointID).getValue(PointInformation.class).getImageUrl());
            pInfo.setIndice1(ds.child(pointID).getValue(PointInformation.class).getIndice1());
            pInfo.setIndice2(ds.child(pointID).getValue(PointInformation.class).getIndice2());
            pInfo.setIndice3(ds.child(pointID).getValue(PointInformation.class).getIndice3());
            pInfo.setLatitude(ds.child(pointID).getValue(PointInformation.class).getLatitude());
            pInfo.setLongitude(ds.child(pointID).getValue(PointInformation.class).getLongitude());
            Log.d(TAG, "showData name : " + pInfo.getName());
            Log.d(TAG, "showData name : " + pInfo.getStatut());
            Log.d(TAG, "showData name : " + pInfo.getImageUrl());
            Log.d(TAG, "showData name : " + pInfo.getIndice1());
            Log.d(TAG, "showData name : " + pInfo.getIndice2());
            Log.d(TAG, "showData name : " + pInfo.getIndice3());
            Log.d(TAG, "showData name : " + pInfo.getLatitude());
            Log.d(TAG, "showData name : " + pInfo.getLongitude());
            ArrayList<String> array = new ArrayList<>();
            array.add(pInfo.getName());
            array.add(pInfo.getStatut());
            array.add(pInfo.getImageUrl());
            array.add(pInfo.getIndice1());
            array.add(pInfo.getIndice2());
            array.add(pInfo.getIndice3());
            array.add(pInfo.getLatitude());
            array.add(pInfo.getLongitude());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListView.setAdapter(adapter);

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
