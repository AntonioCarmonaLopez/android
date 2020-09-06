package com.dam.t08p01.vista.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.vistamodelo.IncsViewModel;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapIncsFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Button btAceptar;
    private OnFragmentMapListener mListener;
    private SupportMapFragment mapFragment;
    private GoogleMap mapa;
    private String datosobtenidos;
    private IncsViewModel incsVM;
    private SharedPreferences pref;


    public interface OnFragmentMapListener {

        void onClickListener(Marker marker);

        void onAceptarListener();
    }

    public MapIncsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inits
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        incsVM = new ViewModelProvider(requireActivity()).get(IncsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_incs, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        // FindViewByIds
        btAceptar = v.findViewById(R.id.btAceptar);

        //listeners
        btAceptar.setOnClickListener(aceptar_click_listener);

        return v;
    }

    private View.OnClickListener aceptar_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.onAceptarListener();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMapListener) {
            mListener = (OnFragmentMapListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        LocationRequest locationRequest = null;
        Map<String, ?> preferencias = pref.getAll();
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.setBuildingsEnabled(true);

            for (Map.Entry<String, ?> preferencia : preferencias.entrySet()) {
                Log.i("t08p01", preferencia.getKey() + ": " +
                        preferencia.getValue().toString());

                if (preferencia.getKey().equals("Loc_interval_key")) {
                    locationRequest = new LocationRequest();
                    locationRequest.setInterval(Long.parseLong(preferencia.getValue().toString()));
                }

                if (preferencia.getKey().equals("Map_tipo_key"))
                    if (preferencia.getValue().toString().equals("Normal"))
                        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    else if (preferencia.getValue().toString().equals("Híbrido"))
                        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    else if (preferencia.getValue().toString().equals("Satelite"))
                        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    else
                        mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }

            /*tIncs = IncsViewModel.getIncs();
            for (Incidencia inc : tIncs) {
                if (inc.getLatitud().isEmpty() || inc.getLatitud().isEmpty())
                    continue;

                String datosobtenidos = inc.getLatitud() + "," + inc.getLongitud();
                String[] parts = datosobtenidos.split(",");
                double lat = Double.parseDouble(parts[0]);
                double lon = Double.parseDouble(parts[1]);
                LatLng latLng = new LatLng(lat, lon);
                mapa.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(inc.getId())
                        .icon(BitmapDescriptorFactory.fromResource(inc.getIcono(inc))));

                mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }*/

            incsVM.getIncsToMarker().observe(this, new Observer<List<Incidencia>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(List<Incidencia> incs) {
                    if(incs.size()==0) {
                        Snackbar.make(getView(), R.string.buscar_ko, Snackbar.LENGTH_SHORT).show();
                    } else{
                        //mapa.clear();
                        incs.forEach(i->{
                            datosobtenidos = i.getLatitud() + "," + i.getLongitud();
                            String[] parts = datosobtenidos.split(",");
                            Log.i("t08p01",datosobtenidos+"-"+i.getEstado());
                            double lat = Double.parseDouble(parts[0]);
                            double lon = Double.parseDouble(parts[1]);
                            LatLng latLng = new LatLng(lat, lon);
                            if(i.getEstado()==0)
                            mapa.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(i.getId())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                    //.icon(BitmapDescriptorFactory.fromResource(i.getIcono(i))));
                            else
                                mapa.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(i.getId())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        });

                    }
                }
            });
        }
        mapa.setOnMarkerClickListener(MapIncsFragment.this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mListener.onClickListener(marker);
        return false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
