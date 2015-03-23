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
import com.fluffyadventure.model.Treasure;
import com.fluffyadventure.model.WanderingSpawn;
import com.fluffyadventure.model.WantedSpawn;
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


public class MapComponent extends FragmentActivity implements OnMapReadyCallback {
    public final static String SPAWN_ID = "com.fluffyadventure.SPAWN_ID";
    //TODO : changer pour la release
    private final static float MAX_DISTANCE_BETWEEN_QUEST_AND_PLAYER = 30;
    private final Map<String, AbstractSpawn> spawnMarkers = new HashMap<>();
    private Button button_go;
    private Button homeBtn;
    private Animal animal1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        animal1 = Controller.getAnimal1();

        button_go = (Button) findViewById(R.id.map_button_go);

        homeBtn = (Button)findViewById(R.id.homeBtn);

        if (animal1.getType()== Creature.RABBIT) {
            homeBtn.setText("Terrier");
        } else if (animal1.getType()== Creature.SQUIRREL) {
            homeBtn.setText("Nid");
        } else if (animal1.getType()== Creature.SHEEP) {
            homeBtn.setText("Bergerie");
        }

        //homeBtn.setTypeface(font);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapComponent.this, Status.class);
                startActivity(intent);
                finish();
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
        placeObjectivesOnMap(map);

    }

    /**
     * Init map
     */
    private void configureMapOptions(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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

                if (spawn instanceof Dungeon) {
                    ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                    ((TextView) v.findViewById(R.id.tooltip_level)).setText("Finis ce donjon pour pouvoir progresser dans l'aventure !");
                } else if (spawn instanceof Spawn) {
                    if (spawn.getSpawnId() != -1)  {
                        ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                        ((TextView) v.findViewById(R.id.tooltip_level)).setText(String.valueOf("Difficulté : " + spawn.getLevel()));
                    } else {
                        ((TextView) v.findViewById(R.id.tooltip_text)).setText("Home sweet home");
                        ((TextView) v.findViewById(R.id.tooltip_level)).setVisibility(View.GONE);
                    }
                } else  if (spawn instanceof Treasure) {
                    ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                    ((TextView) v.findViewById(R.id.tooltip_level)).setVisibility(View.GONE);
                }else if (spawn instanceof WanderingSpawn) {
                    ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                    ((TextView) v.findViewById(R.id.tooltip_level)).setVisibility(View.GONE);
                }else if (spawn instanceof WantedSpawn) {
                    ((TextView) v.findViewById(R.id.tooltip_text)).setText(spawn.getText());
                    ((TextView) v.findViewById(R.id.tooltip_level)).setVisibility(View.GONE);
                }



                return v;
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
                Toast.makeText(MapComponent.this, "Activer la localisation GPS", Toast.LENGTH_LONG).show();
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
            Toast.makeText(MapComponent.this, "Position GPS introuvable", Toast.LENGTH_LONG).show();
            return new LatLng(45.7814205,4.8729611);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new LatLng(45.7814205,4.8729611);
        }
    }

    /**
     * Place all the markers on the map
     */
    private void placeObjectivesOnMap(GoogleMap map) {

        map.clear();
        Resources resources = getResources();

        List<AbstractSpawn> spawns = Controller.getObjectives();
        Controller.moveWanderingSpawns();

        for (AbstractSpawn spawn : spawns) {
            if(!Controller.hasSucceeded(spawn.getSpawnId()))
                addSpawnToMap(map, resources, spawn);
        }

        if (Controller.getQGLocation() != null) {

            int iconId = resources.getIdentifier(animal1.getQGImage(), "drawable", getPackageName());
            Bitmap icon = BitmapFactory.decodeResource(resources, iconId);

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(Controller.getQGLocation())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon)));
            spawnMarkers.put(marker.getId(), new Spawn(animal1.getName(), animal1.getType()));
        }
    }

    /**
     * Place a spawn marker
     */
    private void addSpawnToMap(GoogleMap map, Resources resources, AbstractSpawn spawn) {

        int iconId = resources.getIdentifier(spawn.getIcon(), "drawable", getPackageName());
        Animal animal = Controller.getAnimal1();

        if (spawn.getStatus().equals(AbstractSpawn.SpawnStatus.DONE) ||
                spawn.getStatus().equals(AbstractSpawn.SpawnStatus.REQUIREMENT_NOT_MET)) {
            return;
        }

        Bitmap icon = BitmapFactory.decodeResource(resources, iconId);

        if (!spawn.isSoloFight() && Controller.getAnimal(2) == null) {
            icon = convertToGrayscale(icon);
        }
        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(spawn.latitude, spawn.longitude))
                .title(spawn.getStatus() + " - " + spawn.getName())
                .snippet(spawn.getText())
                .icon(BitmapDescriptorFactory.fromBitmap(icon)));
        spawnMarkers.put(marker.getId(), spawn);
    }

    /**
     * Convert a spawn icon to greyscale
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
     * Select a spawn when the player clicks on a marker
     */
    private void selectSpawn(AbstractSpawn spawn) {
        if (spawn instanceof Treasure) {
            Controller.setCurrentObjective(spawn);
            button_go.setText("Récupérer le trésor !");
            button_go.setEnabled(true);
        } else if (spawn instanceof Spawn) {
            if (spawn.getSpawnId() != -1) {
                Controller.setCurrentObjective(spawn);
                if (!spawn.isSoloFight() && Controller.getAnimal(2) == null) {
                    button_go.setText("Ce combat nécessite 2 compagnons !");
                    button_go.setEnabled(false);
                } else {
                    button_go.setText("Engager le combat !");
                    button_go.setEnabled(true);
                }
            } else {
                Controller.setCurrentObjective(null);
                button_go.setText("Choisis un objectif sur la carte !");
                button_go.setEnabled(false);
            }
        } else if (spawn instanceof Dungeon) {
            Controller.setCurrentObjective(spawn);
            button_go.setText("Commencer le donjon !");
            button_go.setEnabled(true);
        }else if (spawn instanceof WanderingSpawn) {
            Controller.setCurrentObjective(spawn);
            button_go.setText("Commencer le combat !");
            button_go.setEnabled(true);
        }else if (spawn instanceof WantedSpawn) {
            Controller.setCurrentObjective(spawn);
            button_go.setText("Commencer le combat !");
            button_go.setEnabled(true);


        }
    }

    /**
     * Start the selected fight
     */
    @SuppressWarnings("UnusedParameters")
    public void startSelectedObjective(View view) {

        Class targetActivity;
        if (Controller.getCurrentObjective() instanceof Dungeon) {
            targetActivity = SoloCombat.class;
        } else if (Controller.getCurrentObjective() instanceof Spawn || Controller.getCurrentObjective() instanceof WanderingSpawn) {
            targetActivity = SoloCombat.class;

        }else if(Controller.getCurrentObjective() instanceof WantedSpawn) {
            targetActivity = SoloCombat.class;

        } else {
            return;
        }

        Intent intent = new Intent(this, targetActivity);
        intent.putExtra(SPAWN_ID, Controller.getCurrentObjective().getSpawnId());
        startActivity(intent);
        finish();

        /*if (selectedSpawn == null) {
            return;
        }
        if (isPlayerTooFar(selectedSpawn)) {
            showTooFarDialog();
        } else {

            Intent intent = new Intent(this, targetActivity);
            intent.putExtra(QUETE_ID, selectedSpawn.getId());
            startActivity(intent);
        }*/
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

    public void onBackPressed() {
        Intent intent = new Intent(MapComponent.this, Status.class);
        startActivity(intent);
        finish();

    }


}
