package com.teamproject.plastikproject.plastik.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.helper.DirectionMapsV2;
import com.teamproject.plastikproject.plastik.helper.GPSTracker;
import com.teamproject.plastikproject.plastik.helper.MyFuction;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.plastik.model.ModelGeoCoder;
import com.teamproject.plastikproject.plastik.model.ResultsItem;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingLocationActivity extends MyFuction implements OnMapReadyCallback {
    //@BindView(R.id.map)
    //android.widget.fragment map;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.requestorder)
    Button requestorder;
    @BindView(R.id.txtjenissos)
    TextView txtharga;
    @BindView(R.id.tujuan)
    EditText catatan;
    private GoogleMap mMap;
    String jarakkm;
    Double harga;
    String name_location;
    String alamatlengkap;
    LatLng l;
    //deklrasi variable koordinat global
    Double latawal, lonawal, latakhir, lonakkhir;
    String datasos;
    GPSTracker gpStrack;
    DirectionMapsV2 directionMapsV2;
    Double lat, lon;
    LatLng lokasiku;
    //String name_location;
    private GoogleApiClient googleApiClient;
    Intent intent;
    final static int REQUEST_LOCATION = 199;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglocation);
        ButterKnife.bind(this);
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            //     finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        akseslokasiku();

        mapFragment.getMapAsync(this);
        Intent intent =getIntent();
        datasos =intent.getStringExtra("datasos");
//       // requestorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestwonder();
//            }
//        });

    }

    private void requestwonder() {
        final ProgressDialog dialog1 = ProgressDialog.show(ShoppingLocationActivity.this,
                "proses mencari pertolongan", "harap bersabar");
        final String catt =catatan.getText().toString();
        RestApi api = MyRetrofitClient.getInstaceRetrofit();
        final SessionManager sessionManager = new SessionManager(this);

        String iduser = sessionManager.getIdUser();
       // alamatlengkap="jalan hatiku";

//        Call<ModelCariPertolongan> caritolong = api.caripertolongan(iduser, String.valueOf(latawal), String.valueOf(lonawal),catt,name_location,datasos );
//        caritolong.enqueue(new Callback<ModelCariPertolongan>() {
//            @Override
//            public void onResponse(Call<ModelCariPertolongan> call, DataLokasi<ModelCariPertolongan> response) {
//                dialog1.dismiss();
//                boolean result = response.body().isStatus();
//                String r = String.valueOf(result);
//                String msg = response.body().getMsg();
//                if (r.equals("true")) {
//              //      Toast.makeText(ShoppingLocationActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(ShoppingLocationActivity.this,Finddriver.class);
//                    intent.putExtra("catatan",catt);
//                    intent.putExtra("datasos",datasos);
//                startActivity(intent);
//               sessionManager.setIdRescue(String.valueOf(response.body().getData().getId()));
//        //            Toast.makeText(ShoppingLocationActivity.this, "idres"+String.valueOf(response.body().getData().getId()), Toast.LENGTH_LONG).show();
//                finish();
//                }else{
//                    Toast.makeText(ShoppingLocationActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ModelCariPertolongan> call, Throwable t) {
//               myToast("gagal "+t.getMessage());
//                dialog1.dismiss();
//
//            }
//        });
    }

    private void sendnotif() {


    }

    private String posisiku(Double latawal, Double lonawal) {
        name_location = null;
        Geocoder geocoder = new Geocoder(ShoppingLocationActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latawal,lonawal, 1);
            if(list != null&&list.size()>0) {
                name_location = list.get(0).getAddressLine(0) + "" + list.get(0).getCountryName();

                //fetch data from addresses
            }else{
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        110);


            }
            return;
        } else {

            GPSTracker gps = new GPSTracker(ShoppingLocationActivity.this);
            if (gps.canGetLocation()) {

                latawal = gps.getLatitude();
                lonawal = gps.getLongitude();

                //    String name =posisiku(latawal,lonawal);
                 l = new LatLng(latawal, lonawal);
                name_location = posisiku(latawal, lonawal);
                mMap.addMarker(new MarkerOptions().position(l).title(name_location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 18));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,18));

                lokasiawal.setText(name_location);

            } else {
                gps.showSettingGps();
            }


        }
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(6.1925297, -106.8001397);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        // menampilkan control zoom in zoom out
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // menampilkan compas
        mMap.getUiSettings().setCompassEnabled(true);
        // mengatur jenis peta
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    private void getalamatgeocoder(LatLng l) {
        //get init retrofit yang sudah dibkin class inilibrary
        RestApi api = MyRetrofitClient.getInstaceRetrofit3();

        String dataalaamat =l.toString();
        //get request
        Call<ModelGeoCoder> call = api.get_adress(dataalaamat);

        //get response
        call.enqueue(new Callback<ModelGeoCoder>() {
            @Override
            public void onResponse(Call<ModelGeoCoder> call, Response<ModelGeoCoder> response) {

//                String name = "nando";
//                String[] name2 = {"nando","setpain","husn"};

                //response succes
                if (response.isSuccessful()) {
                    //get route from server
                    List<ResultsItem> result = response.body().getResults();
                    //get object overview polyline
                     name_location =result.get(0).getFormattedAddress();
                 //   txtharga.setText("Rp." + HeroHelper.toRupiahFormat2(String.valueOf(harga)) + "(" + jarakkm + ")");


                } else {

                }
            }

            @Override
            public void onFailure(Call<ModelGeoCoder> call, Throwable t) {

            }
        });



    }

    private void akseslokasiku() {
        gpStrack = new GPSTracker(ShoppingLocationActivity.this);
        if (gpStrack.canGetLocation() && mMap != null) {
            lat = gpStrack.getLatitude();
            lon = gpStrack.getLongitude();
            mMap.clear();
            name_location = posisiku(lat, lon);
           // Toast.makeText(ShoppingLocationActivity.this, "lat " + lat + "\nlon " + lon, Toast.LENGTH_SHORT).show();
            lokasiku = new LatLng(lat, lon);
            //add marker
            mMap.addMarker(new MarkerOptions().position(lokasiku).title(name_location)).setIcon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiku, 16));

        }
    }



    @OnClick({R.id.lokasiawal, R.id.lokasitujuan, R.id.requestorder})
    public void onViewClicked(View view) {
       String lA =lokasiawal.getText().toString();
        switch (view.getId()) {
            //
            case R.id.lokasiawal:
                // aksi disini dijalanka
                completeAuto(1);

                break;
            case R.id.lokasitujuan:
                completeAuto(2);
                // disini dijalankan
                break;

            case R.id.requestorder:
                if (TextUtils.isEmpty(lA)){
                    lokasiawal.setError("tidak boleh kosong");
                }else {
                   // requestwonder();
                    //sendnotif();
                }//actionInsertServer();

                break;
        }
    }





    private void completeAuto(int i) {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("ID") // set filter negara
                .build();

        //
        Intent intent = null;
        try {
            intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(ShoppingLocationActivity.this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, i);

    }

    //ini sebuah method untuk ambil data nama lokasi yang di klik oleh pengguna di autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode != 0) {
            //get data
            Place place = PlaceAutocomplete.getPlace(ShoppingLocationActivity.this, data);
            //get coordinat
           name_location=  String.valueOf(place.getAddress());
            latawal = place.getLatLng().latitude;
            lonawal = place.getLatLng().longitude;
            //masukkan k latlang biar bisa di masukkan k maps
            LatLng awal = new LatLng(latawal, lonawal);
            getalamatgeocoder(awal);

            mMap.clear();

            //buat marker berbdasarkan koordinat daptkan atas
            mMap.addMarker(new MarkerOptions()
                    .position(awal).title(place.getAddress().toString()));
            lokasiawal.setMaxLines(1);
            lokasiawal.setText(place.getAddress());


            //set auto zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(awal, 19));


        } else if (requestCode == 2) {
            Place place = PlaceAutocomplete.getPlace(ShoppingLocationActivity.this, data);
            //get coordinat
            latakhir = place.getLatLng().latitude;
            lonakkhir = place.getLatLng().longitude;
            //masukkan k latlang biar bisa di masukkan k maps
            LatLng akhir = new LatLng(latakhir, lonakkhir);

            //buat marker berbdasarkan koordinat daptkan atas
            mMap.addMarker(new MarkerOptions()
                    .position(akhir).title(place.getAddress().toString()));
            lokasitujuan.setMaxLines(1);
            lokasitujuan.setText(place.getAddress());


            //set auto zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(akhir, 16));

            //actionRoute();

        }
    }

//    private void actionRoute() {
//        //get init retrofit yang sudah dibkin class inilibrary
//        RestApi api = MyRetrofitClient.getInstaceRetrofit2();
//
//        String origin = String.valueOf(latawal) + "," + String.valueOf(lonawal);
//        String destination = String.valueOf(latakhir) + "," + String.valueOf(lonakkhir);
//
//        //get request
//        Call<ResponseRoute> call = api.request_route(origin,
//                destination);
//
//        //get response
//        call.enqueue(new Callback<ResponseRoute>() {
//            @Override
//            public void onResponse(Call<ResponseRoute> call, DataLokasi<ResponseRoute> response) {
//
////                String name = "nando";
////                String[] name2 = {"nando","setpain","husn"};
//
//                //response succes
//                if (response.isSuccessful()) {
//                    //get route from server
//                    List<Route> route = response.body().getRoutes();
//                    //get object overview polyline
//                    Route object = route.get(0);
//                    OverviewPolyline overview = object.getOverviewPolyline();
//                    String point = overview.getPoints();
//                    DirectionMapsV2 direction = new DirectionMapsV2(ShoppingLocationActivity.this);
//                    direction.gambarRoute(mMap, point);
//
//                    //ambil jarak
//                    List<Leg> legs = object.getLegs();
//                    Leg object0 = legs.get(0);
//                    //get distance
//                    Distance jarak = object0.getDistance();
//                    jarakkm = jarak.getText();
//                    long jarakmeter = jarak.getValue();
//
//                    //jarak meter kita jadikan km
//                    long km = jarakmeter / 1000;
//
//                    //jadikan string
//                    String km2 = String.valueOf(km);
//
//                    //pembulatan nilainya biar gmpang di kalculasi
//                    //sample : 15640 jadi 16000
//                    Double totaljarak = Math.ceil(Double.parseDouble(km2));
//
//                    //ini sebuah kondisional harga
//                    //kalau seandainya jarak kurang dari 5 km total harga nya 4000
//                    //dan kalau melebihi dari 5 km permeter melebihi 5 di kalikan 2000 + harga normal (4000)
//
//
//                    if (totaljarak >= 5) {
//                        //total harga
//                        harga = (totaljarak - 5) * 2000 + 4000;
//
//                    } else {
//                        harga = 4000.0;
//                    }
//
//
//                    //format rupiah
//                    //sample 10000 kalau pake format 10.000.00
//
//                    txtharga.setText("Rp." + HeroHelper.toRupiahFormat2(String.valueOf(harga)) + "(" + jarakkm + ")");
//
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseRoute> call, Throwable t) {
//
//            }
//        });
//
//
//    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ShoppingLocationActivity.this, REQUEST_LOCATION);

                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }


}
