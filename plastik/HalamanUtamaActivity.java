package com.teamproject.plastikproject.plastik;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.fragment.HelpFragment;
import com.teamproject.plastikproject.plastik.fragment.HomeFragment;
import com.teamproject.plastikproject.plastik.fragment.MyAccountFragment;
import com.teamproject.plastikproject.plastik.fragment.NotifikasiFragment;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.plastik.imageslider.application.AppController;
import com.teamproject.plastikproject.plastik.model.ModelUser;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.imagesliderwithswipeslibrary.Animations.DescriptionAnimation;
import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;
import retrofit2.Call;
import retrofit2.Callback;


public class HalamanUtamaActivity extends SessionManager implements BaseSliderView.OnSliderClickListener {

        private SliderLayout mDemoSlider;

        // Billionaires json url
        private static final String getURL = "http://35.231.18.55:3000/api/smart_advertisings";
        HashMap<String, String> url_maps;


    @BindView(R.id.bottom_view)
    BottomNavigationView mBottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            mDemoSlider = (SliderLayout) findViewById(R.id.slider);
            ButterKnife.bind(this);
//            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            android.support.v4.app.Fragment fragCategory = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragCategory);
            transaction.commit();


            // Creating volley request obj
            JsonArrayRequest billionaireReq = new JsonArrayRequest(getURL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            url_maps = new HashMap<String, String>();
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    url_maps.put(obj.getString("company") + " - " + obj.getString("publish_time"), obj.getString("image_name"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            for (String name : url_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(HalamanUtamaActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .descriptionSize(20)
                                        .image("http://35.231.18.55:3000/api/attachments/advertising/download/"+url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                        .setOnSliderClickListener(HalamanUtamaActivity.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "network issue: please enable wifi/mobile data"+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(billionaireReq);

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);

            mDemoSlider.setPresetTransformer("Stack");
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment fragCategory = null;
                    // init corresponding fragment
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            fragCategory = new HomeFragment();
                            break;
                        case R.id.menu_categories:
                            fragCategory = new NotifikasiFragment();
                            break;
                        case R.id.menu_services:
                            fragCategory = new HelpFragment();
                            break;
                        case R.id.menu_account:
                            fragCategory = new MyAccountFragment();
                            break;
                    }
                    //Set bottom menu selected item text in toolbar
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(item.getTitle());
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragCategory);
                    transaction.commit();
                    return true;
                }
            });
        }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

        @Override
        public void onStop() {
            // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
            mDemoSlider.stopAutoCycle();
            super.onStop();
        }

        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        }
//        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//                = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()) {
//                    case R.id.navigation_shop:
//                      //  toolbar.setTitle("Shop");
//                        return true;
//                    case R.id.navigation_gifts:
//                      //  toolbar.setTitle("My Gifts");
//                        return true;
//                    case R.id.navigation_cart:
//                      //  toolbar.setTitle("Cart");
//                        return true;
//                    case R.id.navigation_profile:
//                      //  toolbar.setTitle("Profile");
//                        return true;
//                }
//                return false;
//            }
//        };

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.mn_logout) {
                showProgressDialog("proses login user");
                RestApi api = MyRetrofitClient.getInstaceRetrofit();
                String token =sessionManager.getToken();
                Log.d("testaja1",token);
                Call<ModelUser> modelUserCall =api.logout(token);
                modelUserCall.enqueue(new Callback<ModelUser>() {
                    @Override
                    public void onResponse(Call<ModelUser> call, retrofit2.Response<ModelUser> response) {
                        sessionManager.logout();
                        Intent intent = new Intent(HalamanUtamaActivity.this, LoginActivity.class);
                        startActivity(intent);

                        myToast("berhasil logout");
hideProgressDialog();
                        //                        if (response.isSuccessful()){
//                            myToast("berhasil logout");
//                        }else{
//                            myToast("gagal ");
//                        }
                    }

                    @Override
                    public void onFailure(Call<ModelUser> call, Throwable t) {
myToast("no internet"+t.getMessage());
hideProgressDialog();
                    }
                });
            }
            return super.onOptionsItemSelected(item);

        }

    }