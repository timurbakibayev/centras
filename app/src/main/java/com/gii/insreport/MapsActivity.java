package com.gii.insreport;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    static String TAG = "MapsActivity";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button)view).setText("Зарузка карты...");
                if (mMap != null) {
                    captureAndFinish();
                }
            }
        });
    }

    Marker place;
    private void captureAndFinish() {
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        if (place != null)
            place.remove();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    public void onSnapshotReady(Bitmap bitmap) {
                        // Write image to disk
                        try {
                            if (bitmap != null) {
                                FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                InsReport.tempBitmap = bitmap;
                                setResult(RESULT_OK);
                                finish();
                                return;
                            }
                            setResult(RESULT_CANCELED);
                            finish();
                            return;
                        } catch (Exception e) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    }
                });
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }

        LatLng almaty = new LatLng(43.236, 76.913);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(almaty,13));

        place = null;
        if (InsReport.currentForm != null &&
                InsReport.currentForm.input.get("LAT") != null &&
                InsReport.currentForm.input.get("LON") != null) {
            try {
                double lat = Double.parseDouble(InsReport.currentForm.input.get("LAT"));
                double lon = Double.parseDouble(InsReport.currentForm.input.get("LON"));
                place = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Место аварии"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),19));
                Log.w(TAG, "onMapReady: going to " + lat + "," + lon);
            } catch (Exception e) {

            }
        }

        String address = "";
        if (place == null) {
            if (InsReport.currentForm.input.get("EVENT_PLACE") != null) {
                address = InsReport.currentForm.input.get("EVENT_PLACE");
            }
            String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
            String[] location = address.split(" ");
            boolean coordinates = false;

            for (int i = 0; i < location.length; i++) {
                if (location[i].matches(regex)) {
                    coordinates = true;
                }
            }

            if (coordinates && location.length > 1) {
                try {
                    double lat = Double.parseDouble(location[0]);
                    double lon = Double.parseDouble(location[1]);
                    place = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Место аварии"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),19));
                    Log.w(TAG, "onMapReady: going to " + lat + "," + lon);
                } catch (Exception e) {

                }
            }
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        /*
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cp = mMap.getCameraPosition();
                Log.w(TAG, "onCameraMove: " + cp.target.latitude + "," + cp.target.longitude + ", zoom " + cp.zoom);
            }
        });*/
    }
}
