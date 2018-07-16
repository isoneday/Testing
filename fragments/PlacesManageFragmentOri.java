package com.teamproject.plastikproject.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.activities.MapActivity;
import com.teamproject.plastikproject.activities.MapActivityOri;
import com.teamproject.plastikproject.adapters.PlaceAdapter;
import com.teamproject.plastikproject.adapters.PlaceAdapter2;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.helpers.ContentHelper;
import com.teamproject.plastikproject.helpers.ShoppingContentProvider;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.model.PlacesModel;
import com.teamproject.plastikproject.modeldatalokasi.ResponseDataLokasi;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesManageFragmentOri extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PlacesManageFragmentOri.class.getSimpleName();
    private static final String ARG_MENU_ID = "argMenuId";
    private static final String STATE_LIST = "PlaceListState";
    public static final String SHARED_PREFERENCES_FILE_USER_INFO_LIST = "userInfoList";
    public static final String SHARED_PREFERENCES_KEY_USER_INFO_LIST = "User_Info_List";
    private ListView placeListView;
    private View floatPlus;
    //private List<PlacesModel> placesList;
    private List<PlacesModel> placesList;
    private PlaceAdapter adapter;
    private PlaceAdapter2 adapter1;
    private int menuItemId = -1;
    private Parcelable placeListViewState;
    private boolean showFabPlus;

    public PlacesManageFragmentOri() {
    }

    public static PlacesManageFragmentOri newInstance(int menuItemId) {
        PlacesManageFragmentOri fragment = new PlacesManageFragmentOri();
        Bundle args = new Bundle();
        args.putInt(ARG_MENU_ID, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            placeListViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_manage, container, false);
        addToolbar(view);
        placeListView = (ListView) view.findViewById(R.id.shop_list);
        floatPlus = view.findViewById(R.id.plus_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            menuItemId = getArguments().getInt(ARG_MENU_ID);
        }

        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        getLoaderManager().initLoader(menuItemId, null, this);

        adapter = new PlaceAdapter(getContext(), R.layout.item_place, placesList);
        placeListView.setAdapter(adapter);
        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MapActivityOri.class)
                        .putExtra(AppConstants.EXTRA_MENU_ITEM, menuItemId)
                        .putExtra(
                                AppConstants.EXTRA_PLACE_ID,
                                adapter.getItem(position).getDbId()
                        );
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        floatPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivityOri.class)
                        .putExtra(AppConstants.EXTRA_MENU_ITEM, menuItemId);
                startActivityForResult(intent, AppConstants.SHOP_RESULT_CODE);
            }
        });

        placeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > visibleItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        showFabPlus(false);
                    } else {
                        showFabPlus(true);
                    }
                }
            }
        });
    }

    private void showFabPlus(boolean show) {
        if (show != showFabPlus) {
            showFabPlus = show;

            int time = getResources().getInteger(R.integer.fab_button_hide_time);
            int distance = getResources().getDimensionPixelSize(R.dimen.fab_button_hide_distance);

            if (!show) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    floatPlus.setVisibility(View.GONE);
                } else {
                    ObjectAnimator
                            .ofFloat(floatPlus, "translationY", 0, distance)
                            .setDuration(time).start();
                }
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    floatPlus.setVisibility(View.VISIBLE);
                } else {
                    ObjectAnimator
                            .ofFloat(floatPlus, "translationY", distance, 0)
                            .setDuration(time).start();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLoaderManager().restartLoader(menuItemId, null, this);
    }

    private void refreshShopsList(Cursor cursor) {
        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        if (placesList.size() > 0) {
            placesList.clear();
        }
        //data lokal
        placesList.addAll(ContentHelper.getPlaceList(cursor));
        //lokal server
        getdatalokasi();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (placeListViewState != null) {
            placeListView.onRestoreInstanceState(placeListViewState);
        }
        placeListViewState = null;
    }
    private void getdatalokasi() {
        RestApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ResponseDataLokasi> dataLokasiCall =api.getalllokasi();
        dataLokasiCall.enqueue(new Callback<ResponseDataLokasi>() {
            @Override
            public void onResponse(Call<ResponseDataLokasi> call, retrofit2.Response<ResponseDataLokasi> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "ada data", Toast.LENGTH_SHORT).show();
                    //versi webser
                    if (placesList == null) {
                        placesList = new ArrayList<>();

                    }
                    //versiwebser
                   // placesList = response.body().getResponse();
                    adapter1 = new PlaceAdapter2(getContext(), R.layout.item_place, placesList);
                    placeListView.setAdapter(adapter1);

                    //simpan di session
                    // Create Gson object.
                    Gson gson = new Gson();

                    // Get java object list json format string.
                    String userInfoListJsonString = gson.toJson(placesList);

                    // Create SharedPreferences object.
                    Context ctx = getContext();
                    SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCES_FILE_USER_INFO_LIST, MODE_PRIVATE);

                    // Put the json format string to SharedPreferences object.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SHARED_PREFERENCES_KEY_USER_INFO_LIST, userInfoListJsonString);
                    editor.commit();

                    // Popup a toast message in screen bottom.
//                    Toast.makeText(ctx, "User info DTO list has been saved in sharedpreferences file.", Toast.LENGTH_SHORT).show();

//                    SessionManager manager = new SessionManager(getContext());
//                    manager.setDataPlace(placesList);
                    placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), MapActivity.class);

                            intent.putExtra(AppConstants.EXTRA_MENU_ITEM, menuItemId);
                            intent.putExtra("bool",true);
//                            intent.putExtra(AppConstants.EXTRA_PLACE_ID,placesList.get(position).getId());
//                            intent.putExtra(AppConstants.EXTRA_PLACE_LAT,placesList.get(position).getLat());
//                            intent.putExtra(AppConstants.EXTRA_PLACE_LONG,placesList.get(position).getLong());
//                            intent.putExtra(AppConstants.EXTRA_PLACE_DESC,placesList.get(position).getDescription());
//                      AppConstants.EXTRA_PLACE_ID, placesList2.get(position).getId()
                            // );
                            startActivity(intent);
                        }
                    });

                }else{
                    Toast.makeText(getActivity(), "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataLokasi> call, Throwable t) {
                Toast.makeText(getActivity(), "gagal"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        placeListViewState = placeListView.onSaveInstanceState();
        outState.putParcelable(STATE_LIST, placeListViewState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(menuItemId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case AppConstants.MENU_SHOW_SHOPS: {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_DESCRIPTION,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                        };
                String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getContext(),
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        SqlDbHelper.COLUMN_ID + "=?",
                        new String[]{Long.toString(AppConstants.PLACES_SHOP)},
                        orderBy
                );
            }
            case AppConstants.MENU_SHOW_PLACES: {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PLACES_COLUMN_PLACES_ID,
                        SqlDbHelper.PLACES_COLUMN_CATEGORY,
                        SqlDbHelper.PLACES_COLUMN_NAME,
                        SqlDbHelper.PLACES_COLUMN_DESCRIPTION,
                        SqlDbHelper.PLACES_COLUMN_LATITUDE,
                        SqlDbHelper.PLACES_COLUMN_LONGITUDE,
                        SqlDbHelper.PLACES_COLUMN_IS_DELETE,
                        SqlDbHelper.PLACES_COLUMN_TIMESTAMP,
                };
                String orderBy = SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getContext(),
                        ShoppingContentProvider.PLACE_CONTENT_URI,
                        projection,
                        SqlDbHelper.PLACES_COLUMN_CATEGORY + "=?",
                        new String[]{Long.toString(AppConstants.PLACES_USER)},
                        orderBy
                );
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        refreshShopsList(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
