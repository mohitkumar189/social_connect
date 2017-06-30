package in.squarei.socialconnect.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import in.squarei.socialconnect.R;
import in.squarei.socialconnect.modals.LatLngModel;
import in.squarei.socialconnect.services.LocationService;
import in.squarei.socialconnect.utils.Logger;

import static in.squarei.socialconnect.R.id.map;

public class LandmarkCityActivity extends SocialConnectBaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long LOCATION_REFRESH_TIME = 10000l;
    private static final float LOCATION_REFRESH_DISTANCE = 500f;
    private SupportMapFragment supportMapFragment = null;
    private GoogleMap googleMap;
    private LocationManager mLocationManager;
    private static final String TAG = "LandmarkCityActivity";
    IntentFilter filter = new IntentFilter();
    public static final String BROADCAST_ACTION = "in.squarei.socialconnect.services";
    private Button button;
    private static final double LAT = 25.200484;
    private static final double LONG = 75.831548;
    private CardView card;
    boolean isVisible = true;
    private Button btn1, btn2, btn3, btn4, btn5;

    private ArrayList<LatLngModel> cafeList = new ArrayList<>();
    private ArrayList<LatLngModel> playground = new ArrayList<>();
    private ArrayList<LatLngModel> parks = new ArrayList<>();
    private ArrayList<LatLngModel> pools = new ArrayList<>();
    private ArrayList<LatLngModel> schools = new ArrayList<>();

    LatLng currentLoc = null;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_city);
        initMap();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(receiver, filter);
        createGoogleApi();
/*        if (getNavigationBarHeight(context, Configuration.ORIENTATION_PORTRAIT) > 0) {
            float dp = convertPixelsToDp(getNavigationBarHeight(context, Configuration.ORIENTATION_PORTRAIT), context);
            CardView.LayoutParams l = new CardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            l.setMargins(0, 0, 0, (int) dp);
            card.setLayoutParams(l);
        }*/
        Log.e(TAG, "" + getNavigationBarHeight(context, Configuration.ORIENTATION_PORTRAIT));
    }

    private int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "======received intent=====");
            currentLoc = new LatLng(intent.getDoubleExtra("Latitude", 0), intent.getDoubleExtra("Longitude", 0));
            // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(clocation.getLatitude(), clocation.getLongitude()), 13));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 13));

            googleMap.addMarker(new MarkerOptions()
                    .title("You are here")
                    .snippet("")
                    .position(currentLoc));

        }
    };

    @Override
    protected void initViews() {
        setTitle("Landmark City");
        card = (CardView) findViewById(R.id.card);
        button = (Button) findViewById(R.id.button);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);

        //  SlideToDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void initContext() {
        context = this;
        currentActivity = this;
    }

    @Override
    protected void initListners() {
        button.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected boolean isNavigationView() {
        return false;
    }

    @Override
    protected boolean isTabs() {
        return false;
    }

    @Override
    protected boolean isFab() {
        return false;
    }

    @Override
    protected boolean isDrawerListener() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        hideHome();
        switch (v.getId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;

        }
    }

    private void initMap() {
        try {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
            supportMapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        startService(new Intent(context, LocationService.class));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        this.googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LAT, LONG), 20));

        googleMap.addMarker(new MarkerOptions()
                .title("You are here")
                .snippet("")
                .position(new LatLng(LAT, LONG)));
    }

    Location clocation;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Logger.info(TAG, "new location detected: " + location);
            clocation = location;
            //your code here
            createGeofence(new LatLng(clocation.getLatitude(), clocation.getLongitude()), 500);
            //  currentLocation = new LatLng(clocation.getLatitude(), clocation.getLongitude());
            if (location != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(clocation.getLatitude(), clocation.getLongitude()), 13));

                googleMap.addMarker(new MarkerOptions()
                        .title("")
                        .snippet("DL 1CR ")
                        .position(new LatLng(clocation.getLatitude(), clocation.getLongitude())));
            }

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
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.normal:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


    }

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    public void SlideToAbove() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.bottomtop);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card.startAnimation(anim);
        // image_show.startAnimation(anim);

    }

    public void SlideToDown() {
        Animation anim;
        anim = AnimationUtils.loadAnimation(context, R.anim.topbottom);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card.startAnimation(anim);
        // image_show.startAnimation(anim);

    }

    private void hideHome() {
        if (isVisible) {
            // scrollview.setVisibility(View.GONE);
            SlideToDown();
            isVisible = false;
        } else {
            //  scrollview.setVisibility(View.VISIBLE);
            SlideToAbove();
            isVisible = true;
        }
    }
}
