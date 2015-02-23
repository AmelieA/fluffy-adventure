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
    private final Map<String, Spawn> spawnMarkers = new HashMap<>();
    private Spawn selectedSpawn = null;
    private Button button_go;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        button_go = (Button) findViewById(R.id.map_button_go);

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
                Animal animal = Controller.getAnimal();
                ViewGroup parent = (ViewGroup) findViewById(R.id.map);
                View v = getLayoutInflater().inflate(R.layout.spawn_tooltip, parent, false);
                /*
                Spawn spawn = spawnMarkers.get(marker.getId());

                ((TextView) v.findViewById(R.id.tooltip_name)).setText(spawn.getName());
                ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                ((TextView) v.findViewById(R.id.tooltip_level)).setText(spawn.getLevel());*/
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

        int fightIcon = resources.getIdentifier("spawn_icon", "drawable", getPackageName());
        int treasureIcon = resources.getIdentifier("treasure_icon", "drawable", getPackageName());

        Bitmap icon1 = BitmapFactory.decodeResource(resources, fightIcon);
        Bitmap icon2 = BitmapFactory.decodeResource(resources, treasureIcon);

       map.addMarker(new MarkerOptions()
                .position(new LatLng(45.781745, 4.872931))
                .title("FIGHT !")
                .snippet("Bat le méchant zombie mangeur de carottes !")
                .icon(BitmapDescriptorFactory.fromBitmap(icon1)));


       map.addMarker(new MarkerOptions()
                .position(new LatLng(45.782347, 4.877629 ))
                .title("Trésor, much gold")
                .snippet("Such loot, very intense, wow")
                .icon(BitmapDescriptorFactory.fromBitmap(icon2)));

        List<Spawn> spawns = new ArrayList<>();
       // spawns.addAll(Spawn.listAll(Encounter.class));
       // spawns.addAll(Spawn.listAll(Dunjeon.class));

        /*for (Spawn spawn : spawns) {
            addSpawnToMap(map, resources, spawn);
        }*/
    }

    /**
     * Place a spawn marker
     */
    private void addSpawnToMap(GoogleMap map, Resources resources, Spawn spawn) {

        /*int iconeId = resources.getIdentifier(spawn.getIcone(), "drawable", getPackageName());
        Animal animal = Controller.getAnimal();

        if (spawn.getStatut(animal).equals(Spawn.Statut.REUSSIE) ||
                spawn.getStatut(animal).equals(Spawn.Statut.PREREQUIS_INSATISFAIT)) {
            return;
        }

        Bitmap icone = BitmapFactory.decodeResource(resources, iconeId);
        if (spawn.getStatut(animal).equals(Spawn.Statut.COMPETENCES_INSUFFISANTES)) {
            icone = convertToGrayscale(icone);
        }

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(spawn.latitude, spawn.longitude))
                .title(spawn.getStatut(animal) + " - " + spawn.titre)
                .snippet(spawn.getText())
                .icon(BitmapDescriptorFactory.fromBitmap(icone)));
        spawnMarkers.put(marker.getId(), spawn);*/
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
    private void selectSpawn(Spawn spawn) {
      /*  Animal animal = Controller.getAnimal();
        selectedSpawn = spawn;
        if (quete.getStatut(animal).equals(Spawn.Statut.DISPONIBLE)) {
            button_go.setText("Commencer la quête !");
            button_go.setEnabled(true);
        } else if (quete.getStatut(animal).equals(Spawn.Statut.COMPETENCES_INSUFFISANTES)) {
            button_go.setText("Compétences insuffisantes");
            button_go.setEnabled(false);
        }*/


    }

    /**
     * Start the selected fight
     */
    @SuppressWarnings("UnusedParameters")
    public void startSelectedQuest(View view) {
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
    private boolean isPlayerTooFar(Spawn spawn) {
        float distance = getDistanceBetweenSpawnAndPlayer(spawn);
        return distance >= MAX_DISTANCE_BETWEEN_QUEST_AND_PLAYER;
    }

    /**
     * Get the distance between the player and a spawn
     */
    private float getDistanceBetweenSpawnAndPlayer(Spawn spawn) {
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
