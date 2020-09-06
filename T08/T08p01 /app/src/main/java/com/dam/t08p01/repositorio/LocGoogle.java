package com.dam.t08p01.repositorio;


import android.Manifest;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.dam.t08p01.R;
import com.dam.t08p01.vista.fragmentos.MtoIncsFragment;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;


public class LocGoogle {

    private Listener mLocalizacion;
    private MtoIncsFragment mContext;
    private LocationCallback locationCallback;
    private Location localizacion;
    private FusedLocationProviderClient fusedLocationClient;
    private SharedPreferences pref;
    private int intervalo;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 666;


    public interface Listener {
        void onLocationChange(double latitud, double longitud);
    }

    public LocGoogle(MtoIncsFragment mtoIncsFragment) {
        this.mLocalizacion = mtoIncsFragment;
        this.mContext = mtoIncsFragment;
        locationCallback = obtenerDatos();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mtoIncsFragment.getContext());
        pref = PreferenceManager.getDefaultSharedPreferences(mtoIncsFragment.getContext());
        Map<String, ?> preferencias = pref.getAll();
        for (Map.Entry<String, ?> preferencia : preferencias.entrySet()) {
            Log.i("t08p01", preferencia.getKey() + ": " +
                    preferencia.getValue().toString());
            if (preferencia.getKey().equals("Loc_desplazamiento_key"))
                intervalo=Integer.parseInt(preferencia.getValue().toString());

        }

    }


    public void getCoor() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(intervalo);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        if (ContextCompat.checkSelfPermission(mContext.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext.requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(mContext.requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }



        SettingsClient client = LocationServices.getSettingsClient(mContext.requireActivity()); // Pasar activity
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener( mContext.requireActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locationCallback = obtenerDatos();
                fusedLocationClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        null);
            }
        });


        task.addOnFailureListener(mContext.requireActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mContext.requireActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    public void stopCoor() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    private LocationCallback obtenerDatos() {

        LocationCallback tmpLocationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                localizacion = locationResult.getLastLocation();
                if (localizacion != null) {
                    mLocalizacion.onLocationChange(localizacion.getLatitude(), localizacion.getLongitude());
                    Log.i("t08p01","Coordenadas:"+localizacion.getLatitude()+localizacion.getLongitude());
                }
                super.onLocationResult(locationResult);
            }
        };

        return tmpLocationCallBack;
    }
}