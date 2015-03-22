package com.fluffyadventure.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.AsyncTask;
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
import com.fluffyadventure.view.Status;
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
    private LatLng initialPosition;


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
                CreateAllTask task = new CreateAllTask(MoveQGActivity.this);
                task.execute();
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
        initialPosition = Controller.getQGLocation();

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

        LatLng location = getCurrentLocation();

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.latitude, location.longitude)));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    public LatLng getCurrentLocation()
    {
        try
        {
            LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String locProvider = locMgr.getBestProvider(criteria, false);
            Location location = locMgr.getLastKnownLocation(locProvider);

            // getting GPS status
            boolean isGPSEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNWEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNWEnabled)
            {
                // no network provider is enabled
                Toast.makeText(MoveQGActivity.this, "Activer la localisation GPS", Toast.LENGTH_LONG).show();
                return new LatLng(45.7814205,4.8729611);
            }
            else
            {
                // First get location from Network Provider
                if (isNWEnabled)
                    if (locMgr != null)
                        location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                    if (location == null)
                        if (locMgr != null)
                            location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (NullPointerException ne)
        {
            Toast.makeText(MoveQGActivity.this, "Position GPS introuvable", Toast.LENGTH_LONG).show();
            return new LatLng(45.7814205,4.8729611);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new LatLng(45.7814205,4.8729611);
        }
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
        if (initialPosition == null) {
            return true;
        } else {
            float[] results = new float[3];
            Location.distanceBetween(initialPosition.latitude,initialPosition.longitude,newPlace.latitude,newPlace.longitude,results);
            return results[0] > 999;
        }
    }

    private void showTooFarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Le nouvel emplacement est trop près de l'ancien (moins d'1km).")
                .setTitle("Déménagement impossible")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });;

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onBackPressed() {
        Intent intent;
        if (firstTime){
            Controller.flush();
            intent = new Intent(MoveQGActivity.this, MainActivity.class);

        }
        else {
            intent = new Intent(MoveQGActivity.this, com.fluffyadventure.view.Status.class);
        }

        startActivity(intent);
        finish();
    }


    private class CreateAllTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context ctx;
        int ret;

        public CreateAllTask(Context ctx) {
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            this.ret = 0;
            //this.dialog.setCancelable(true);
        }

        protected void onPreExecute(){
            this.dialog.setTitle("Création...");
            this.dialog.show();

        }

        protected Boolean doInBackground(Void... params){
            Boolean result;
            Log.d("FirstTime",Boolean.toString(firstTime));
            if (!firstTime){
                Log.d("FIRSTTIME","NO");
                result = Controller.moveHQ2();
            }
            else {
                this.ret = Controller.createUserAnimalHQ();
                if (this.ret == 0){
                    result = true;
                }
                else {
                    result = false;
                }
            }

            //Boolean result = true;

            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            Intent intent;

            if (!login){
                switch (this.ret){
                    case -1:
                        Toast.makeText(MoveQGActivity.this, "Echec de la création de l'utilisateur", Toast.LENGTH_LONG).show();
                        break;
                    case -2:
                        Toast.makeText(MoveQGActivity.this, "Echec de la création de l'animal", Toast.LENGTH_LONG).show();
                        break;
                    case -3:
                        Toast.makeText(MoveQGActivity.this, "Echec de la création du QG", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(MoveQGActivity.this, "Echec de la création", Toast.LENGTH_LONG).show();
                        break;
                }
                intent = new Intent(MoveQGActivity.this, MainActivity.class);

            }
            else
            {
                Toast.makeText(MoveQGActivity.this, "Création réussie", Toast.LENGTH_LONG).show();
                intent = new Intent(MoveQGActivity.this, com.fluffyadventure.view.Status.class);
            }
            startActivity(intent);
            finish();



        }



    }
}
