package com.fluffyadventure.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
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
import com.fluffyadventure.model.Spawn;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapComponent extends FragmentActivity implements OnMapReadyCallback {
    public final static String QUETE_ID = "com.fluffyadventure.QUETE_ID";
    //TODO : changer pour la release
    private final static float MAX_DISTANCE_BETWEEN_QUEST_AND_PLAYER = 30;
    private final Map<String, AbstractSpawn> spawnMarkers = new HashMap<>();
    private AbstractSpawn selectedSpawn = null;
    private Button button_go;
    private Button homeBtn;
    private Animal animal1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        animal1 = Controller.getAnimal();

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        button_go = (Button) findViewById(R.id.map_button_go);

        homeBtn = (Button)findViewById(R.id.homeBtn);
        homeBtn.setTypeface(font);

        if (animal1.getType().equals("Rabbit")) {
            homeBtn.setText("Terrier");
        } else if (animal1.getType().equals("Squirrel")) {
            homeBtn.setText("Nid");
        } else if (animal1.getType().equals("Sheep")) {
            homeBtn.setText("Bergerie");
        }

        //homeBtn.setTypeface(font);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapComponent.this, Status.class);
                startActivity(intent);
            }
        });

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

        configureMapOptions(map);
        getQuestsFromDatabaseAndShowThemOnMap(map);

    }

    /**
     * Init map
     */
    private void configureMapOptions(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                selectSpawn(spawnMarkers.get(marker.getId()));
                return false;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                ViewGroup parent = (ViewGroup) findViewById(R.id.map);
                View v = getLayoutInflater().inflate(R.layout.spawn_tooltip, parent, false);

                AbstractSpawn spawn = spawnMarkers.get(marker.getId());

                ((TextView) v.findViewById(R.id.tooltip_name)).setText(spawn.getName());
                ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                ((TextView) v.findViewById(R.id.tooltip_level)).setText(spawn.getLevel());
                return v;
            }
        });

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.7791898, 4.8533830)));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * Place all the markers on the map
     */
    private void getQuestsFromDatabaseAndShowThemOnMap(GoogleMap map) {

        map.clear();
        Resources resources = getResources();

        List<AbstractSpawn> spawns = Controller.getObjectives();

        for (AbstractSpawn spawn : spawns) {
            addSpawnToMap(map, resources, spawn);
        }
    }

    /**
     * Place a spawn marker
     */
    private void addSpawnToMap(GoogleMap map, Resources resources, AbstractSpawn spawn) {

        int iconId = resources.getIdentifier(spawn.getIcon(), "drawable", getPackageName());
        Animal animal = Controller.getAnimal();

        if (spawn.getStatus(animal).equals(Spawn.Status.DONE) ||
                spawn.getStatus(animal).equals(Spawn.Status.REQUIREMENT_NOT_MET)) {
            return;
        }

        Bitmap icon = BitmapFactory.decodeResource(resources, iconId);

       /* if (spawn.getStatus(animal).equals(Spawn.Status.COMPETENCES_INSUFFISANTES)) {
            icon = convertToGrayscale(icon);
        }*/

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(spawn.latitude, spawn.longitude))
                .title(spawn.getStatus(animal) + " - " + spawn.getName())
                .snippet(spawn.getText())
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
        spawnMarkers.put(marker.getId(), spawn);
    }

    /**
     * Convert a bitmap to grayscale
     */
    private Bitmap convertToGrayscale(Bitmap icone) {

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();

        cm.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        p.setColorFilter(filter);

        Bitmap iconeNB = Bitmap.createBitmap(icone.getWidth(), icone.getHeight(), icone.getConfig());
        Canvas canvas = new Canvas(iconeNB);

        canvas.drawBitmap(icone, 0f, 0f, p);

        return iconeNB;
    }

    /**
     * Select a spawn when the player cliks on a marker
     */
    private void selectSpawn(AbstractSpawn spawn) {
        Animal animal = Controller.getAnimal();
        selectedSpawn = spawn;
        if (spawn.getStatus(animal).equals(Spawn.Status.AVAILABLE)) {
            button_go.setText("Engager le combat !");
            button_go.setEnabled(true);
        } /*else if (quete.getStatut(animal).equals(Spawn.Statut.COMPETENCES_INSUFFISANTES)) {
            button_go.setText("Compétences insuffisantes");
            button_go.setEnabled(false);
        }*/
    }

    /**
     * Start the selected fight
     */
    @SuppressWarnings("UnusedParameters")
    public void startSelectedObjective(View view) {
        if (selectedSpawn == null) {
            return;
        }
        if (isPlayerTooFar(selectedSpawn)) {
            showTooFarDialog();
        } else {

            Class targetActivity;
          /*  if (selectedSpawn instanceof Dunjeon) {
                targetActivity = Dunjeon_View.class;
            } else if (selectedSpawn instanceof Encounter) {
                targetActivity = Fight_View.class;
            } else {
                return;
            }
            Intent intent = new Intent(this, targetActivity);
            intent.putExtra(QUETE_ID, selectedSpawn.getId());
            startActivity(intent);*/
        }
    }

    /**
     * Check if the player is too far to start a fight
     */
    private boolean isPlayerTooFar(AbstractSpawn spawn) {
        float distance = getDistanceBetweenSpawnAndPlayer(spawn);
        return distance >= MAX_DISTANCE_BETWEEN_QUEST_AND_PLAYER;
    }

    /**
     * Get the distance between the player and a spawn
     */
    private float getDistanceBetweenSpawnAndPlayer(AbstractSpawn spawn) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
        float distance = 10000;
        if (location != null) {
            distance = location.distanceTo(spawn.getLocation());
        } else {
            Toast.makeText(this, "Position GPS introuvable", Toast.LENGTH_LONG).show();
        }
        return distance;
    }

    /**
     * Tell the player he is too far to start a fight
     */
    private void showTooFarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tu es trop loin de l'objectif.")
                .setTitle("Rapproche-toi !");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
