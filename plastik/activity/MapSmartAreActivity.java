package com.teamproject.plastikproject.plastik.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.modeldatalokasi.ResponseDataLokasi;
import com.teamproject.plastikproject.plastik.helper.GPSTracker;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapSmartAreActivity extends AppCompatActivity implements OnMapReadyCallback {


    @BindView(R.id.edtawal)
    EditText edtawal;
    @BindView(R.id.edtakhir)
    EditText edtakhir;
    @BindView(R.id.edtsearch)
    EditText edtsearch;
    private GoogleMap mMap;
    CameraUpdate cu;
    //init marker , untuk fit zoom all marker
    private List<Marker> markersList = new ArrayList<Marker>();
    Marker mapMarker;
    String catid = null;
    String id = null;
    String name = null;
    String key = "sdf";

    ArrayList<String> placeList = new ArrayList<String>();
    ArrayList<String> idList = new ArrayList<String>();
    private GPSTracker gpStrack;
    private double lat;
    private double lon;
    private String name_location;
    private LatLng lokasiku;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sub_poiwisata);
        ButterKnife.bind(this);

        //back button
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.icon_bar);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        catid = intent.getStringExtra("catid");
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");

        //set Text
      //  namaObjek.setText(name);

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

    private String convertname(Double lat, Double lon) {
        name_location = null;
        Geocoder geocoder = new Geocoder(MapSmartAreActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat, lon,1);
            if (list != null && list.size() > 0) {
                name_location = list.get(0).getAddressLine(0) + "" + list.get(0).getFeatureName();

                //fetch data from addresses
            } else {
                Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name_location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpStrack = new GPSTracker(MapSmartAreActivity.this);
        if (gpStrack.canGetLocation() && mMap != null) {
            lat = gpStrack.getLatitude();
            lon = gpStrack.getLongitude();
            mMap.clear();
            name_location = convertname(lat, lon);
            Toast.makeText(MapSmartAreActivity.this, "lat " + lat + "\nlon " + lon, Toast.LENGTH_SHORT).show();
            lokasiku = new LatLng(lat, lon);
            //add marker
            mMap.addMarker(new MarkerOptions().position(lokasiku).title(name_location)).setIcon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_iamhere)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiku, 16));


            edtawal.setText(name_location);

        }
//        //Enable/Show Traffic
//        if (sesi.getTraffic().equals("Yes")){
//            mMap.setTrafficEnabled(true);
//        }

        //iktifkan settingan zoom contol
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //init progess dialog
        getdatalokasi();


    }

    private void getdatalokasi() {
        final ProgressDialog progress = new ProgressDialog(this);
        //tambahkan pesan
        progress.setMessage("Loading...");
        progress.show();

        RestApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ResponseDataLokasi> dataLokasiCall = api.getalllokasi();
        dataLokasiCall.enqueue(new Callback<ResponseDataLokasi>() {
            @Override
            public void onResponse(Call<ResponseDataLokasi> call, Response<ResponseDataLokasi> response) {
                progress.dismiss();
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getResponse() != null) {
                            for (int i = 0; i < response.body().getResponse().size(); i++) {
                                Double lat = Double.valueOf(response.body().getResponse().get(i).getLat());
                                Double lon = Double.valueOf(response.body().getResponse().get(i).getLong());
                                String placeName = response.body().getResponse().get(i).getDescription();
                                LatLng koordinat = new LatLng(lat, lon);
                                String id = response.body().getResponse().get(i).getId();
                                 mapMarker = mMap.addMarker(new MarkerOptions()
                                        .position(koordinat)
                                        .title(placeName)
                                );

                                //add to list, untuk membuat fix bounds, sehingga semua marker terlihat
                                markersList.add(i, mapMarker);

                                placeList.add(placeName);
                                idList.add(id);
                            }
                        } else {
                            Toast.makeText(MapSmartAreActivity.this, "tidak ada data", Toast.LENGTH_SHORT).show();
//                            HeroHelper.pesan(ctx,"Tidak ada data wisata");
                        }

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }

                } else {
                    //         Toast.makeText(getActivity(), "tidak ada data", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapSmartAreActivity.this, "tidak ada data", Toast.LENGTH_SHORT).show();
//
                }

                //kalau ada marker
                //fit zoom all markers
                if (markersList.size() > 0) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker m : markersList) {
                        builder.include(m.getPosition());
                    }
                    /**initialize the padding for map boundary*/
                    int padding = 90;
                    /**create the bounds from latlngBuilder to set into map camera*/
                    LatLngBounds bounds = builder.build();
                    /**create the camera with bounds and padding to set into map*/
                    cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    /**call the map call back to know map is loaded or not*/
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            /**set animated zoom camera into map*/
                            mMap.animateCamera(cu);

                        }
                    });
                }


                //Event Klik infoWindows
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        LatLng latLon = marker.getPosition();

                        //Cycle through places array
                        for (Marker place : markersList) {
                            if (latLon.equals(place.getPosition())) {
                                // Send single item click data to SingleItemView Class
//                                Intent intent = new Intent(MapSmartAreActivity.this, DetailPOIActivity.class);
//                                // Pass all data population
//                                // Pass all data population
//                                int indexArrayList = placeList.indexOf(place.getTitle());
//                                intent.putExtra("id",idList.get(indexArrayList));
//                                startActivity(intent);
                                edtakhir.setText(place.getTitle());
                            }

                        }
                    }
                });


            }

            @Override
            public void onFailure(Call<ResponseDataLokasi> call, Throwable t) {
                Toast.makeText(MapSmartAreActivity.this, "gagal" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();
                ;
            }
        });
    }

    @OnClick({R.id.edtawal, R.id.edtsearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.edtawal:

                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry("ID") // set filter negara
                        .build();

                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                            .build(MapSmartAreActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.edtsearch:
                AutocompleteFilter typeFilter1 = new AutocompleteFilter.Builder()
                        .setCountry("ID") // set filter negara
                        .build();

                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter1).build(MapSmartAreActivity.this);
                    startActivityForResult(intent, 2);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Place place = PlaceAutocomplete.getPlace(this, data);
        //getlat dan get lon
        if (requestCode == 1 && resultCode == RESULT_OK) {
            lat = place.getLatLng().latitude;
            lon = place.getLatLng().longitude;
            name_location = place.getName().toString();
            edtawal.setText(name_location);
            mMap.clear();
            addmarker(lat, lon);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            lat = place.getLatLng().latitude;
            lon = place.getLatLng().longitude;
            name_location = place.getName().toString();
            edtakhir.setText(name_location);
           // mMap.clear();
            addmarker(lat, lon);
          //  aksesrute(strmode);
            LatLng latLon = mapMarker.getPosition();

            //Cycle through places array
            for (Marker place1 : markersList) {
                if (latLon.equals(place1.getPosition())) {
                    // Send single item click data to SingleItemView Class
//                                Intent intent = new Intent(MapSmartAreActivity.this, DetailPOIActivity.class);
//                                // Pass all data population
//                                // Pass all data population
//                                int indexArrayList = placeList.indexOf(place.getTitle());
//                                intent.putExtra("id",idList.get(indexArrayList));
//                                startActivity(intent);
               //     edtakhir.setText(place.getTitle());
                }

            }
        }

    }
    private void addmarker(Double lat, Double lon) {
        lokasiku = new LatLng(lat, lon);
        name_location = convertname(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiku, 15));
        mMap.addMarker(new MarkerOptions().position(lokasiku).title(name_location));

    }

//    @OnClick({R.id.buttonBerita, R.id.buttonMap})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.buttonBerita:
//                Intent in = new Intent(ctx, BeritaAreaActivity.class);
//                in.putExtra("id",id);
//                in.putExtra("name",name);
//                startActivity(in);
//                break;
//            case R.id.buttonMap:
//                //HeroHelper.pesan(ctx,catid+"-"+id);
//                Intent intent = new Intent(ctx, SubPOIWisataActivity.class);
//                // Pass all data population
//                intent.putExtra("id",id);
//                intent.putExtra("catid",catid);
//                intent.putExtra("name",name);
//                startActivity(intent);
//                break;
//        }
//    }
}
