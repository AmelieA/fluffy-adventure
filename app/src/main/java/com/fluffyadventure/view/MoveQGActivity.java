package com.fluffyadventure.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Spawn;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoveQGActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView move_QG_desc;
    private Button map_button_go;
    private GoogleMap currentMap;
    private String homeType;
    private boolean firstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_qg);

        Animal animal1 = Controller.getAnimal1();
        homeType = "";

        if (animal1.getType()== Creature.RABBIT) {
            homeType = "le terrier";
        } else if (animal1.getType()== Creature.SQUIRREL) {
            homeType = "le nid";
        } else if (animal1.getType()== Creature.SHEEP) {
            homeType = "la bergerie";
        }

        map_button_go = (Button) findViewById(R.id.map_button_go);

        //homeBtn.setTypeface(font);
        map_button_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoveQGActivity.this, Status.class);
                startActivity(intent);
                finish();
            }
        });

        move_QG_desc = (TextView) findViewById(R.id.move_QG_desc);

        if (Controller.getQGLocation() == null) {
            firstTime = true;
            map_button_go.setText("Choisis un emplacement");
            move_QG_desc.setText("Choisis un emplacement pour " + homeType + " de " + animal1.getName());
        } else {
            firstTime = false;
            map_button_go.setText("Valider la position");
            move_QG_desc.setText("Choisis un nouvel emplacement pour " + homeType + " de " + animal1.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        Log.i("FA", "---------------  MAP READY -----------------");

        currentMap = map;

        configureMapOptions(map);
        placeQGOnMap();

    }

    /**
     * Init map
     */
    private void configureMapOptions(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (newPlaceFarEnough(point) || firstTime) {
                    Controller.setQGLocation(point.latitude,point.longitude);
                    placeQGOnMap();
                    map_button_go.setEnabled(true);
                    map_button_go.setText("Valider la position");
                    Animal animal1 = Controller.getAnimal1();
                    move_QG_desc.setText("Choisis un nouvel emplacement pour " + homeType + " de " + animal1.getName());
                } else {
                    showTooFarDialog();
                }

            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
    }


    /**
     * Place the actual QG on the map
     */
    private void placeQGOnMap() {
        if (Controller.getQGLocation() != null) {
            currentMap.clear();
            Resources resources = getResources();

            Animal animal = Controller.getAnimal1();

            int iconId = resources.getIdentifier(animal.getQGImage(), "drawable", getPackageName());
            Bitmap icon = BitmapFactory.decodeResource(resources, iconId);

            Marker marker = currentMap.addMarker(new MarkerOptions()
                    .position(Controller.getQGLocation())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon)));
        }
    }

    private boolean newPlaceFarEnough(LatLng newPlace) {
        if (Controller.getQGLocation() == null) {
            return true;
        } else {
            float[] results = new float[3];
            Location.distanceBetween(Controller.getQGLocation().latitude,Controller.getQGLocation().longitude,newPlace.latitude,newPlace.longitude,results);
            return results[0] > 999;
        }
    }

    private void showTooFarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Le nouvel emplacement est trop près de l'ancien (moins d'1km).")
                .setTitle("Déménagement impossible");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
