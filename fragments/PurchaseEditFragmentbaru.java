package com.teamproject.plastikproject.fragments;

import android.animation.ObjectAnimator;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.activities.BaseActivity;
import com.teamproject.plastikproject.activities.PlacesActivity;
import com.teamproject.plastikproject.adapters.PurchaseItemAdapter;
import com.teamproject.plastikproject.adapters.SettingsSpinnerAdapterbar;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.helpers.ContentHelper;
import com.teamproject.plastikproject.helpers.ShoppingContentProvider;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.model.PlacesModel;
import com.teamproject.plastikproject.model.PurchaseItemModel;
import com.teamproject.plastikproject.model.PurchaseListModelbar;
import com.teamproject.plastikproject.modelupdateskedule.ModelUpdateSkedule;
import com.teamproject.plastikproject.modelupdateskedule.Responseupdateskdle;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;
import com.teamproject.plastikproject.WritePurchaseListService;
import com.teamproject.plastikproject.utils.AlarmUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PurchaseEditFragmentbaru extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PurchaseEditFragmentbaru.class.getSimpleName();
    private static final String ARG_LIST_ID = "PurchaseList_param";
    private static final String STATE_LIST = "PurchaseListState";
    private static final String STATE_ID = "ListIdState";
    private static final String STATE_GOODS = "GoodsState";
    private static final String STATE_TITLE = "TitleState";
    private static final String STATE_NEW_LIST = "newListState";
    private static final int REQUEST_SHOP = 10101;
    private static final int REQUEST_PLACES = 10102;
    private static final int LOADER_ITEM_ID = 0;
    private static final int LOADER_PLACE_ID = 1;
    private static final int LOADER_SHOP_ID = 2;
    private static final int LOADER_LIST = 3;
    public static final String SHARED_PQREFERENCES_KEY_USER_INFO_LIST = "info";
    public static final String SHARED_PREFERENCES_FILE_USER_INFO_LIST1 = "dataaa";
    private PurchaseItemAdapter adapter;
    private ListView purchaseListView;
    private View header, progressBar, addItemButton, toolbarBottom, createButton, clearTimeButton, moreButton, createView, hidePanel;
    private Button doneButton;
    private EditText listNameEdit;
    private EditText goodsLabelEdit;
    private Spinner shopsSpinner, placeSpinner;
    private TextView timeText, dateText;
    private SettingsSpinnerAdapterbar shopSpinnerAdapter, placeSpinnerAdapter;
    private List<PlacesModel> shopsList, placesList;
    private PurchaseListModelbar purchaseList;
    private Parcelable purchaseListViewState;
    private boolean isShowMore = false, isDateSelect = false, isTimeSelect = false, isNewList = false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String titleState;
    private String desc;
    private Context mContext;
    private String waktu;
    Responseupdateskdle dataskdle ;
    private int hasil;
    private SessionManager sesi;

    public static PurchaseEditFragmentbaru newInstance(long purchaseListId) {
        PurchaseEditFragmentbaru fragment = new PurchaseEditFragmentbaru();
        Bundle args = new Bundle();
        args.putLong(ARG_LIST_ID, purchaseListId);
        fragment.setArguments(args);
        return fragment;
    }

    public PurchaseEditFragmentbaru() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        sesi = new SessionManager(getContext());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            purchaseListViewState = savedInstanceState.getParcelable(STATE_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_edit, container, false);

        //setListenerToRootView();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        purchaseListView = (ListView) view.findViewById(R.id.purchase_item_list);
        header = inflater.inflate(R.layout.purchase_edit_header, purchaseListView, false);
        goodsLabelEdit = (EditText) header.findViewById(R.id.edit_goods_label);
        addItemButton = header.findViewById(R.id.button_goods_add);

        listNameEdit = (EditText) view.findViewById(R.id.edit_list_name);

        progressBar = view.findViewById(R.id.progress_bar);

        toolbarBottom = view.findViewById(R.id.toolbar_bottom);
        moreButton = view.findViewById(R.id.more_button);
        doneButton = (Button) view.findViewById(R.id.done_button);
        createView = view.findViewById(R.id.create_panel);
        createButton = view.findViewById(R.id.create_list_button);
        hidePanel = view.findViewById(R.id.hide_panel);
        clearTimeButton = view.findViewById(R.id.clear_time_button);
        shopsSpinner = (Spinner) view.findViewById(R.id.shops_spinner);
        placeSpinner = (Spinner) view.findViewById(R.id.place_spinner);
        timeText = (TextView) view.findViewById(R.id.time_time);
        dateText = (TextView) view.findViewById(R.id.time_date);

        toolbarBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nothing
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long id = -1;
        String goodsState = null;
        boolean isRestoreSaved = false;

        if (savedInstanceState != null) {
            id = savedInstanceState.getLong(STATE_ID);
            goodsState = savedInstanceState.getString(STATE_GOODS);
            titleState = savedInstanceState.getString(STATE_TITLE);
            isNewList = savedInstanceState.getBoolean(STATE_NEW_LIST);
            isRestoreSaved = true;
        }
        if (id < 0 && getArguments() != null) {
            id = getArguments().getLong(ARG_LIST_ID);
        }

        if (id > 0) {
            Bundle args = new Bundle();
            args.putLong(ARG_LIST_ID, id);
            getLoaderManager().initLoader(LOADER_LIST, args, this);
        } else {
            if (!isRestoreSaved) {
                isNewList = true;
            }
            purchaseList = new PurchaseListModelbar();
            purchaseList.setTimeCreate(System.currentTimeMillis());
            List<PurchaseItemModel> purchaseItems = new ArrayList<>();
            purchaseList.setPurchasesItems(purchaseItems);

            initScreen();
        }

        if (goodsState != null) {
            goodsLabelEdit.setText(goodsState);
        }

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToolbarBottomPanel(!isShowMore);
            }
        });
    }

    private void showToolbarBottomPanel(boolean show) {
        if (show != isShowMore) {

            final int heightHidePanel = getActivity().getResources().getDimensionPixelSize(R.dimen.purchase_item_hide_panel_height);
            final int panelTime = getResources().getInteger(R.integer.purchase_item_panel_time);

            if (show) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) toolbarBottom.getLayoutParams();
                    params.bottomMargin = 0;
                    toolbarBottom.setLayoutParams(params);
                } else {
                    ObjectAnimator
                            .ofFloat(toolbarBottom, "translationY", 0, -heightHidePanel)
                            .setDuration(panelTime).start();
                }
                isShowMore = true;
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) toolbarBottom.getLayoutParams();
                    params.bottomMargin = (-heightHidePanel);
                    toolbarBottom.setLayoutParams(params);
                } else {
                    ObjectAnimator
                            .ofFloat(toolbarBottom, "translationY", -heightHidePanel, 0)
                            .setDuration(panelTime).start();
                }
                isShowMore = false;
            }
        }
    }

    private void initScreen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            adapter = new PurchaseItemAdapter(getContext(), null, R.layout.item_purchase_edit);
        } else {
            adapter = new PurchaseItemAdapter(getContext(), null, 0, R.layout.item_purchase_edit);
        }
        adapter.setOnItemInteractionListener(new PurchaseItemAdapter.OnItemInteractionListener() {
            @Override
            public void onCheckBoxClick(long dbId, boolean checked) {
                changeBought(dbId, checked);
            }
        });
        purchaseListView.addHeaderView(header);
        purchaseListView.setAdapter(adapter);
        purchaseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!purchaseList.isDone()) {
                    showDialog((Cursor) purchaseListView.getItemAtPosition(position));
                }
            }
        });

        if (purchaseList.getIdUser() != 0) {
            getLoaderManager().initLoader(LOADER_ITEM_ID, null, this);
            progressBar.setVisibility(View.VISIBLE);
        }
        if (isNewList) {
            createView.setVisibility(View.VISIBLE);
            moreButton.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
        } else {
            hidePanel.setVisibility(View.VISIBLE);
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchaseList.getIdUser() == 0
                        && TextUtils.isEmpty(listNameEdit.getText().toString())) {

                    /*Toast.makeText(
                            getActivity(),
                            R.string.purchase_edit_create_empty_toast,
                            Toast.LENGTH_LONG
                    ).show();*/

                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.purchase_edit_create_empty_title))
                            .setMessage(getString(R.string.purchase_edit_create_empty_message))
                            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //nothing
                                }
                            })
                            .show();
                } else {
                    if (TextUtils.isEmpty(listNameEdit.getText())) {
                        listNameEdit.setText(R.string.purchase_edit_new_list_default);
                    }
                    purchaseList.setDay(listNameEdit.getText().toString());
                    final String name = goodsLabelEdit.getText().toString();
                    final Handler handler = new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (purchaseList.getIdUser() == 0) {
                                Uri uri = ContentHelper.insertPurchaseList(getActivity(), purchaseList);
                             //   purchaseList.setIdUser(Integer.parseInt(uri.getLastPathSegment()));
                                purchaseList.setIdUser(Integer.parseInt(uri.getLastPathSegment()));
                            } else {
                                ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                            }
                            if (!TextUtils.isEmpty(name)) {
                                PurchaseItemModel item = new PurchaseItemModel(
                                        0,
                                        0,
                                        0,
                                        false,
                                        false,
                                        0,
                                        name,
                                        0,
                                        "",
                                        0
                                );
                                ContentHelper.insertPurchaseItem(getActivity(), item,purchaseList.getIdUser());

                                if (adapter.getCursor() == null) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getLoaderManager().initLoader(LOADER_ITEM_ID, null, PurchaseEditFragmentbaru.this);
                                        }
                                    });
                                }
                            }
                        }
                    }).start();

                    goodsLabelEdit.setText(null);

                    createView.setVisibility(View.GONE);
                    moreButton.setVisibility(View.VISIBLE);
                    doneButton.setVisibility(View.VISIBLE);

                    hidePanel.setVisibility(View.VISIBLE);

                    isNewList = false;
                }
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseListModelbar data1 =purchaseList;
                //   PurchaseListModelbar model = new PurchaseListModelbar();
                long data =data1.getTime();
                //creating Date from millisecond
                Date currentDate = new Date(data);

                //printing value of Date

                System.out.println("current Date: " + currentDate);

                DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                //formatted value of current Date
                System.out.println("Milliseconds to Date: " + df.format(currentDate));

                //Converting milliseconds to Date using Calendar
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(data);
                System.out.println("Milliseconds to Date using Calendar:"
                        + df.format(cal.getTime()));

                Log.d("currentdate:", String.valueOf(currentDate));
                Log.d("currentdateMillise", df.format(currentDate));
                Log.d("currentdateMilclnd",df.format(cal.getTime()));
                Timestamp sq = new Timestamp(cal.getTime().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Log.d("currentdatetimestampe",sdf.format(sq)); //this will print wit
                desc =listNameEdit.getText().toString();
                RestApi api = MyRetrofitClient.getInstaceRetrofit();
                String id = "5b48b8a16fed35468804aad2";
                String waktu =sdf.format(sq);
                String sttus ="true";
                Call<PurchaseListModelbar> modelCall =api.insertskedule(
                        id,desc,waktu,sttus
                );
                modelCall.enqueue(new Callback<PurchaseListModelbar>() {
                    @Override
                    public void onResponse(Call<PurchaseListModelbar> call, Response<PurchaseListModelbar> response) {
                        if (response.isSuccessful()){
                            showToast("berhasil");
                            long id =response.body().getIdUser();
                            SessionManager manager = new SessionManager(getContext());
                            String hari =listNameEdit.getText().toString();
                           // String time = String.valueOf(response.body().getTime());
                         //   String mili = String.valueOf(response.body().getTimeAlarm());
                            manager.setDay(hari);
                          //  manager.setTime(time);
                           // manager.setMili(mili);
                            addItem(id);
                        }else{
                            showToast("gagal");
                        }
                    }

                    @Override
                    public void onFailure(Call<PurchaseListModelbar> call, Throwable t) {
                        showToast("check your connection and try again "+t.getMessage());
                    }
                });


            }
        });

//        goodsLabelEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                    addItem(actionId);
//                    handled = true;
//                }
//                return handled;
//            }
//        });

        getLoaderManager().initLoader(LOADER_SHOP_ID, null, this);

        getLoaderManager().initLoader(LOADER_PLACE_ID, null, this);

        if (purchaseList.getTime() > 0) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(purchaseList.getTime());
//            mYear = c.get(Calendar.YEAR);
//            mMonth = c.get(Calendar.MONTH);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            isDateSelect = true;
            isTimeSelect = true;

            showTime();
        }

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSpinner();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDateSpinner();
            }
        });

        clearTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchaseList.getTime() > 0) {
//                    mYear = 0;
//                    mMonth = 0;
//                    mDay = 0;
                    mHour = 0;
                    mMinute = 0;
                    isDateSelect = false;
                    isTimeSelect = false;

                    purchaseList.setTime(0);
                    AlarmUtils alarmUtils = new AlarmUtils(getContext());
                    alarmUtils.cancelListAlarm(purchaseList);

                    dateText.setText(R.string.purchase_edit_bottom_set_date);
                    timeText.setText(R.string.purchase_edit_bottom_set_time);

                    updateList();

                    clearTimeButton.setVisibility(View.GONE);
                }
            }
        });

        setEnableInterface(!purchaseList.isDone());
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder titleBuilder = new StringBuilder();
                StringBuilder massageBuilder = new StringBuilder();
                if (purchaseList.isDone()) {
                    titleBuilder.append(getString(R.string.purchase_edit_alert_continue_title));
                    massageBuilder.append(getString(R.string.purchase_edit_alert_continue_message));
                } else {
                    titleBuilder.append(getString(R.string.purchase_edit_alert_done_title));
                    massageBuilder.append(getString(R.string.purchase_edit_alert_done_message));
                }
                new AlertDialog.Builder(getContext())
                        .setTitle(titleBuilder.toString())
                        .setMessage(massageBuilder.toString())
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                purchaseList.setIsDone(!purchaseList.isDone());
                                updateList();
                                setEnableInterface(!purchaseList.isDone());
                                if (purchaseList.isDone()) {
                                    onBackPressed();
                                }
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void setEnableInterface(boolean enable){
        purchaseListView.setClickable(enable);
        adapter.setItemEnable(enable);
        adapter.notifyDataSetChanged();
        listNameEdit.setEnabled(enable);
        goodsLabelEdit.setEnabled(enable);
        addItemButton.setEnabled(enable);
        dateText.setEnabled(enable);
        timeText.setEnabled(enable);
        clearTimeButton.setEnabled(enable);
        shopsSpinner.setEnabled(enable);
        placeSpinner.setEnabled(enable);
        if (enable) {
            doneButton.setText(R.string.purchase_edit_button_done);
            setDoneIcon();
        } else {
            doneButton.setText(R.string.purchase_edit_button_continue);
        }
    }

    private void showDialog(Cursor cursor) {
        int indexId = cursor.getColumnIndex(SqlDbHelper.COLUMN_ID);
        int indexServerId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID);
        int indexIsBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);
        int indexIsCancel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL);
        int indexGoodsId = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID);
        int indexLabel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
        int indexQuantity = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY);
        int indexDescription = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION);
        int indexTimestamp = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP);
        final PurchaseItemModel item = new PurchaseItemModel(
                cursor.getLong(indexId),
                cursor.getLong(indexServerId),
                purchaseList.getUserId(),
                cursor.getInt(indexIsBought)>0,
                cursor.getInt(indexIsCancel)>0,
                cursor.getInt(indexGoodsId),
                cursor.getString(indexLabel),
                cursor.getFloat(indexQuantity),
                cursor.getString(indexDescription),
                cursor.getLong(indexTimestamp)
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.purchase_edit_item_title);
        View view = inflater.inflate(R.layout.dialog_purchase_item, null);
        final EditText name = (EditText) view.findViewById(R.id.edit_item_name);
        final CheckBox bought = (CheckBox) view.findViewById(R.id.check_bought);
        builder.setView(view);
        if (!item.isCancel()) {
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!TextUtils.isEmpty(name.getText().toString())) {
                        item.setGoodsLabel(name.getText().toString());
                    }
                    item.setBought(bought.isChecked());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseItem(getContext(), item);
                        }
                    }).start();
                }
            });
        }
        builder.setNegativeButton(R.string.button_cancel, null);
        String deleteTitle;
        if (item.isCancel()) {
            deleteTitle = getString(R.string.purchase_edit_item_button_restore);
        } else {
            deleteTitle = getString(R.string.purchase_edit_item_button_delete);
        }
        builder.setNeutralButton(deleteTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (item.getServerId() == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.deletePurchaseItem(getContext(), item.getDbId());
                        }
                    }).start();
                } else {
                    if (!TextUtils.isEmpty(name.getText().toString())) {
                        item.setGoodsLabel(name.getText().toString());
                    }
                    item.setBought(bought.isChecked());

                    item.setCancel(!item.isCancel());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseItem(getContext(), item);
                        }
                    }).start();
                }
            }
        });
        AlertDialog dialog = builder.create();

        name.setText(item.getGoodsLabel());
        name.setEnabled(!item.isCancel());
        bought.setChecked(item.isBought());
        bought.setEnabled(!item.isCancel());

        dialog.show();
    }

    private void initShopSpinner() {
        shopSpinnerAdapter = new SettingsSpinnerAdapterbar(
                getContext(),
                R.layout.item_settings_spinner,
                shopsList,
                R.string.shop_edit_spinner_default_entry
        );
        shopSpinnerAdapter.setSettingsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopsSpinner.setEnabled(false);
                Intent intent = new Intent(getContext(), PlacesActivity.class);
                intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_SHOPS);
                startActivityForResult(intent, REQUEST_SHOP);
            }
        });
        shopsSpinner.setAdapter(shopSpinnerAdapter);
        if (purchaseList.getShopId() != 0) {
            for (PlacesModel shop : shopsList) {
                if (!purchaseList.isUserShop()) {
                    if (shop.getServerId() == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                } else {
                    if (shop.getDbId() == purchaseList.getShopId()) {
                        shopsSpinner.setSelection(shopSpinnerAdapter.getPosition(shop));
                    }
                }
            }
        }
        shopsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean needUpdate = false;
                if (position != 0) {
                    if (shopSpinnerAdapter.getItem(position).getServerId() == 0
                            && (purchaseList.getShopId() != shopSpinnerAdapter.getItem(position).getDbId()
                            || !purchaseList.isUserShop())) {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getDbId());
                        purchaseList.setIsUserShop(true);
                        needUpdate = true;
                    } else if (shopSpinnerAdapter.getItem(position).getServerId() != 0
                            && (purchaseList.getShopId() != shopSpinnerAdapter.getItem(position).getServerId()
                            || purchaseList.isUserShop())) {
                        purchaseList.setShopId(shopSpinnerAdapter.getItem(position).getServerId());
                        purchaseList.setIsUserShop(false);
                        needUpdate = true;
                    }
                } else if (purchaseList.getShopId() != 0) {
                    purchaseList.setShopId(0);
                    needUpdate = true;
                }
                if (needUpdate &&purchaseList.getIdUser() != 0) {
                    purchaseList.setMaxDistance(0);
                    purchaseList.setIsAlarm(false);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                        }
                    }).start();*/
                    updateList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void initPlaceSpinner() {
        placeSpinnerAdapter = new SettingsSpinnerAdapterbar(
                getContext(),
                R.layout.item_settings_spinner,
                placesList,
                R.string.place_edit_spinner_default_entry
        );
        placeSpinnerAdapter.setSettingsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeSpinner.setEnabled(false);
                Intent intent = new Intent(getContext(), PlacesActivity.class);
                intent.putExtra(AppConstants.EXTRA_MENU_ITEM, AppConstants.MENU_SHOW_PLACES);
                startActivityForResult(intent, REQUEST_PLACES);
            }
        });
        placeSpinner.setAdapter(placeSpinnerAdapter);
        if (purchaseList.getPlaceId() != 0) {
            for (PlacesModel place : placesList) {
                if (!purchaseList.isUserPlace()) {
                    if (place.getServerId() == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                } else {
                    if (place.getDbId() == purchaseList.getPlaceId()) {
                        placeSpinner.setSelection(placeSpinnerAdapter.getPosition(place));
                    }
                }
            }
        }
        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean needUpdate = false;
                if (position != 0) {
                    if (placeSpinnerAdapter.getItem(position).getServerId() == 0
                            && (purchaseList.getPlaceId() != placeSpinnerAdapter.getItem(position).getDbId()
                            || !purchaseList.isUserPlace())) {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getDbId());
                        purchaseList.setIsUserPlace(true);
                        needUpdate = true;
                    } else if (placeSpinnerAdapter.getItem(position).getServerId() != 0
                            && (purchaseList.getPlaceId() != placeSpinnerAdapter.getItem(position).getServerId()
                            || purchaseList.isUserPlace())) {
                        purchaseList.setPlaceId(placeSpinnerAdapter.getItem(position).getServerId());
                        purchaseList.setIsUserPlace(false);
                        needUpdate = true;
                    }
                } else if (purchaseList.getPlaceId() != 0) {
                    purchaseList.setPlaceId(0);
                    purchaseList.setIsUserPlace(false);
                    needUpdate = true;
                }
                if (needUpdate && purchaseList.getIdUser() != 0) {
                    purchaseList.setMaxDistance(0);
                    purchaseList.setIsAlarm(false);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentHelper.updatePurchaseList(getActivity(), purchaseList);
                        }
                    }).start();*/
                    updateList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void showTimeSpinner(){
        int hourOfDay;
        int minute;
        if (!isTimeSelect) {
            final Calendar c = Calendar.getInstance();
            hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        } else {
            hourOfDay = mHour;
            minute = mMinute;
        }

        new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        isTimeSelect = true;
                        saveTime();
                        showTime();
                    }
                },
                hourOfDay,
                minute,
                true
        ).show();
    }

//    private void showDateSpinner(){
//        int year;
//        int month;
//        int day;
//        if (!isDateSelect) {
//            final Calendar c = Calendar.getInstance();
//            year = c.get(Calendar.YEAR);
//            month = c.get(Calendar.MONTH);
//            day = c.get(Calendar.DAY_OF_MONTH);
//        } else {
//            year = mYear;
//            month = mMonth;
//            day = mDay;
//        }
//
//        new DatePickerDialog(
//                getContext(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        mYear = year;
//                        mMonth = monthOfYear;
//                        mDay = dayOfMonth;
//                        isDateSelect = true;
//                        saveTime();
//                        showTime();
//                    }
//                },
//                year,
//                month,
//                day
//        ).show();
//    }

    private void saveTime(){
        if ( isTimeSelect) {
            Calendar c = Calendar.getInstance();
        //    c.set(mHour, mMinute);
            int thisyear =c.get(Calendar.YEAR);
            int thismonth =c.get(Calendar.MONTH);
            int thisdat =c.get(Calendar.DATE);

            c.set(thisyear,thismonth,thisdat,mHour,mMinute);
            purchaseList.setTime(c.getTimeInMillis());
//dsds
            //dsdsds
            if (purchaseList.getIdUser() > 0) {
                updateList();
                AlarmUtils alarmUtils = new AlarmUtils(getContext());
                SessionManager manager = new SessionManager(getContext());
                String day = manager.getDay();
                long jam = purchaseList.getTime();

                Calendar calg = Calendar.getInstance();
                calg.setTimeInMillis(jam);

                Timestamp sqg = new Timestamp(calg.getTime().getTime());
                SimpleDateFormat sdfg = new SimpleDateFormat("HH:mm");
              String  waktujam = sdfg.format(sqg);




                //  int hasil;
                if (day.equals("sunday")) {
                    hasil = 1;
                } else if (day.equals("monday")) {
                    hasil = 2;
                } else if (day.equals("tuesday")) {
                    hasil = 3;
                } else if (day.equals("wednesday")) {
                    hasil = 4;
                } else if (day.equals("thursday")) {
                    hasil = 5;
                } else if (day.equals("friday")) {
                    hasil = 6;
                } else if (day.equals("saturday")) {
                    hasil = 7;
                } else {
                    hasil = 0;
                }
                //miiliscnd waktu sekarang
                Calendar rightNow = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());

                long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                        rightNow.get(Calendar.DST_OFFSET);
                long sinceMid = (rightNow.getTimeInMillis() + offset) %
                        (24 * 60 * 60 * 1000);
                Timestamp sq = new Timestamp(cal.getTime().getTime());

                System.out.println(sinceMid + " milliseconds since midnight");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Log.d("currentdatetimestampe",sdf.format(sq)); //this will print wit
                // desc =listNameEdit.getText().toString();
                String  waktusekarang = sdf.format(sq);

                if (hasil==c.get(Calendar.DAY_OF_WEEK) && waktujam.equals(waktusekarang)){
//             //if (hasil==c.get(Calendar.DAY_OF_WEEK) && 6600000 > sinceMid){
   //           if (hasil ==c.get(Calendar.DAY_OF_WEEK)){
////                73560000
//                  Toast.makeText(getContext(), "sama", Toast.LENGTH_SHORT).show();
                      alarmUtils.setListAlarm(purchaseList);
//
//            }
//                if (purchaseList.getTime() > System.currentTimeMillis()) {
//                  //  alarmUtils.setListAlarm(purchaseList);
//
//                }

                } else {
                    alarmUtils.cancelListAlarm(purchaseList);
                    Toast.makeText(
                            getContext(),
                            R.string.purchase_edit_bottom_fail_time_toast,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }

    private void showTime(){
//        if (isDateSelect) {
//            StringBuilder date = new StringBuilder();
//            date.append(mYear + ".");
//            if (mMonth + 1 > 9) {
//                date.append((mMonth + 1) + ".");
//            } else {
//                date.append("0" + (mMonth + 1) + ".");
//            }
//            if (mDay > 9) {
//                date.append(mDay);
//            } else {
//                date.append("0" + mDay);
//            }
//            dateText.setText(date.toString());
//        }
        if (isTimeSelect) {
            StringBuilder time = new StringBuilder();
            if (mHour > 9) {
                time.append(mHour + ":");
            } else {
                time.append("0" + mHour + ":");
            }
            if (mMinute > 9) {
                time.append(mMinute);
            } else {
                time.append("0" + mMinute);
            }
            timeText.setText(time.toString());
        }

        if (isTimeSelect) {
            clearTimeButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsLabelEdit.requestFocus();
        shopsSpinner.setEnabled(true);
        placeSpinner.setEnabled(true);
    }

    /*
    * Keyboard show/hide listener
    * */
    /*boolean isOpened = false;
    public void setListenerToRootView(){
        final View activityRootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    toolbarBottom.setVisibility(View.GONE);
                    *//*if (!isOpened) {
                        //Do two things, make the view top visible and the editText smaller
                    }*//*
                    isOpened = true;
                } else if (isOpened) {
                    isOpened = false; //Show keyboard
                    toolbarBottom.setVisibility(View.VISIBLE);
                    showToolbarBottomPanel(false);
                }
            }
        });
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        purchaseListViewState = purchaseListView.onSaveInstanceState();
        outState.putParcelable(STATE_LIST, purchaseListViewState);
        if (purchaseList != null) {
            outState.putLong(STATE_ID, purchaseList.getIdUser());
        }
        outState.putString(STATE_GOODS, goodsLabelEdit.getText().toString());
        outState.putString(STATE_TITLE, listNameEdit.getText().toString());
        outState.putBoolean(STATE_NEW_LIST, isNewList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null && isAdded()) {
            getLoaderManager().destroyLoader(LOADER_LIST);
            getLoaderManager().destroyLoader(LOADER_PLACE_ID);
            getLoaderManager().destroyLoader(LOADER_SHOP_ID);
            getLoaderManager().destroyLoader(LOADER_ITEM_ID);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_purchase_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_list) {
            hideSoftKeyboard();
            deleteList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() != null && isAdded()) {
            getLoaderManager().hasRunningLoaders();
            getLoaderManager().destroyLoader(LOADER_LIST);
            getLoaderManager().destroyLoader(LOADER_PLACE_ID);
            getLoaderManager().destroyLoader(LOADER_SHOP_ID);
            getLoaderManager().destroyLoader(LOADER_ITEM_ID);
        }
        int da = Integer.parseInt(String.valueOf(purchaseList.getIdUser()));
        getActivity().startService(new Intent(getContext(), WritePurchaseListService.class)
                .putExtra(WritePurchaseListService.LIST_EXTRA, purchaseList));

        if (getActivity() != null) {
            if (da != 0) {
                if (!TextUtils.equals(listNameEdit.getText().toString(), purchaseList.getDay())) {
                    if ( isTimeSelect) {
                        Calendar c = Calendar.getInstance();
                        c.set(mHour, mMinute, 0);
                        purchaseList.setTime(c.getTimeInMillis());

                    }

                    updateList();
                }
                long data =purchaseList.getTime();
                //creating Date from millisecond
                Date currentDate = new Date(data);

                //printing value of Date

                System.out.println("current Date: " + currentDate);


                DateFormat df = new SimpleDateFormat("HH:mm");

                //formatted value of current Date
                System.out.println("Milliseconds to Date: " + df.format(currentDate));

                //Converting milliseconds to Date using Calendar
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(data);
                System.out.println("Milliseconds to Date using Calendar:"
                        + df.format(cal.getTime()));

                Timestamp sq = new Timestamp(cal.getTime().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Log.d("currentdatetimestampe",sdf.format(sq)); //this will print wit
                desc =listNameEdit.getText().toString();
                waktu = sdf.format(sq);
                String sttus ="true";
                RestApi api = MyRetrofitClient.getInstaceRetrofit();
            showToast("proses");
            String daaaa=String.valueOf(da);
              //  final SessionManager sesi =new SessionManager(getContext());
            String iduser =sesi.getIdUser();
            String hari =listNameEdit.getText().toString();
            sesi.setidincre(String.valueOf(da));
                Call<ModelUpdateSkedule> modelCall = api.updatedata(waktu,iduser,hari,"true", String.valueOf(da));
                modelCall.enqueue(new Callback<ModelUpdateSkedule>() {
                    @Override
                    public void onResponse(Call<ModelUpdateSkedule> call, Response<ModelUpdateSkedule> response) {
            showToast("berhasil update");
            dataskdle = new Responseupdateskdle();
                        dataskdle = response.body().getResponse();
                       sesi.setTime(timeText.getText().toString());
                        // Get java object list json format string.
//                        Intent moveWithObjectIntent = new Intent(getContext(), AlarmBroadcastReceiverUbah.class);
//                        moveWithObjectIntent.putExtra(AlarmBroadcastReceiverUbah.EXTRA_PERSON, (Parcelable) dataskdle);
//                        startActivity(moveWithObjectIntent);
                        AlarmUtils alarmUtils = new AlarmUtils(getContext());
    }

                    @Override
                    public void onFailure(Call<ModelUpdateSkedule> call, Throwable t) {
                        showToast("gagal update");

                    }
                });

            } else {
                if (!TextUtils.isEmpty(listNameEdit.getText().toString())) {
                   // addNewList();
                    Toast.makeText(getContext(), "addnewlist", Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);

                    }
                    desc = listNameEdit.getText().toString();
        purchaseList.setDay(desc);
        if ( isTimeSelect) {
            Calendar c = Calendar.getInstance();
            c.set( mHour, mMinute, 0);
            purchaseList.setTime(c.getTimeInMillis());

        }

        if (getActivity() != null) {



        }
                } else {
                    Toast.makeText(getContext(), R.string.purchase_edit_save_empty_message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "aggg", Toast.LENGTH_SHORT).show();

                }
            }
            hideSoftKeyboard();
            getActivity().getSupportFragmentManager().popBackStack();
        }




///   long millis = data % 1000;
////        long second = (data / 1000) % 60;
////        long minute = (data / (1000 * 60)) % 60;
////        long hour = (data / (1000 * 60 * 60)) % 24;
//       // HeroHelper.
//     //  String sd=HeroHelper.timeConversion2(data1.getTime());
//        // String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
//       // Toast.makeText(getActivity(), "jam:"+time, Toast.LENGTH_LONG).show();
//       // SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
//        try {
//            String dddd=HeroHelper.jamSekarang3(data1.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //    Date ins = df.parse(data1.getTime());
      //  long ts = ins.getTime();
//String ds=HeroHelper.getDurationBreakdown(data1.getTime());
  //      Toast.makeText(getContext(), "ss"+ds, Toast.LENGTH_SHORT).show();

        return false;
    }

    private void addItem(final long id) {
        if (!TextUtils.isEmpty(goodsLabelEdit.getText().toString())) {
            progressBar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(purchaseList.getDay()) && TextUtils.isEmpty(listNameEdit.getText())) {
                purchaseList.setDay(getString(R.string.purchase_edit_new_list_default));
            }
            final String name = goodsLabelEdit.getText().toString();
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (purchaseList.getIdUser() == 0) {
                        Uri uri = ContentHelper.insertPurchaseList(getContext(), purchaseList);
                        purchaseList.setIdUser(id);
                 //       purchaseList.setIdUser(Integer.parseInt(uri.getLastPathSegment()));
                    }
                    PurchaseItemModel item = new PurchaseItemModel(
                            0,
                            0,
                            0,
                            false,
                            false,
                            0,
                            name,
                            0,
                            "",
                            0
                    );
                    ContentHelper.insertPurchaseItem(getContext(), item, purchaseList.getIdUser());

                    if (adapter.getCursor() == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getLoaderManager().initLoader(LOADER_ITEM_ID, null, PurchaseEditFragmentbaru.this);
                            }
                        });
                    }
                }
            }).start();

            goodsLabelEdit.setText(null);
        }
        goodsLabelEdit.requestFocus();
    }
    public void showToast(String msg) {
        if (PurchaseEditFragmentbaru.this.isVisible() && msg != null & mContext != null)
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    private void updateList() {
        if (TextUtils.isEmpty(listNameEdit.getText())) {
            listNameEdit.setText(R.string.purchase_edit_new_list_default);
        }
        purchaseList.setDay(listNameEdit.getText().toString());
        if (getActivity() != null) {
            getActivity().startService(new Intent(getContext(), WritePurchaseListService.class)
                            .putExtra(WritePurchaseListService.LIST_EXTRA, purchaseList)
            );
        }
    }

    private void deleteList() {
        boolean isNeedDelete = false;
        if (purchaseList.getIdUser() != 0) {
            isNeedDelete = true;
        } else {
            if (purchaseList.getPurchasesItems().size() > 0
                    || !TextUtils.isEmpty(listNameEdit.getText().toString())) {
                isNeedDelete = true;
            }
        }
        if (isNeedDelete) {
            String listName = listNameEdit.getText().toString();
            String message;
            if (TextUtils.isEmpty(listName)) {
                message = String.format(
                        getString(R.string.purchase_edit_alert_delete_description),
                        getString(R.string.purchase_edit_this_list_default)
                );
            } else {
                message = String.format(
                        getString(R.string.purchase_edit_alert_delete_description),
                        listName
                );
            }
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.purchase_edit_alert_delete_title)
                    .setMessage(message)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final Context context = getActivity().getApplicationContext();
                           String id = String.valueOf(purchaseList.getIdUser());
                            if (purchaseList.getIdUser() != 0) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ContentHelper.deletePurchaseList(context, purchaseList);
                                    }
                                }).start();
                            }
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void addNewList() {
//        if (TextUtils.isEmpty(listNameEdit.getText())) {
//            listNameEdit.setText(R.string.purchase_edit_new_list_default);
//        }
//        String desc =listNameEdit.getText().toString();
//        purchaseList.setDay(desc);
//        if (isDateSelect && isTimeSelect) {
//            Calendar c = Calendar.getInstance();
//            c.set(mYear, mMonth, mDay, mHour, mMinute, 0);
//            purchaseList.setTime(c.getTimeInMillis());
//            Log.d("jam",String.valueOf(c.getTimeInMillis()));
//            Toast.makeText(getContext(), String.valueOf(c.getTimeInMillis()), Toast.LENGTH_SHORT).show();
//        }
//        RestApi api = MyRetrofitClient.getInstaceRetrofit();
//        String id = "5b4611df0c7d4525caabb017";
//        Call<PurchaseListModelbar> modelCall =api.insertskedule(
//        id,desc,String.valueOf(purchaseList.getTime()),"true"
//        );
//        modelCall.enqueue(new Callback<PurchaseListModelbar>() {
//            @Override
//            public void onResponse(Call<PurchaseListModelbar> call, Response<PurchaseListModelbar> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(getContext(), "sukses", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getContext(), "galgal maap", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PurchaseListModelbar> call, Throwable t) {
//                Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        if (getActivity() != null) {
//            getActivity().startService(new Intent(getContext(), WritePurchaseListService.class)
//                            .putExtra(WritePurchaseListService.LIST_EXTRA, purchaseList)
//            );
//        }
    }

    private void changeBought(final long dbId, final boolean checked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT, checked ? 1 : 0);
                getActivity().getContentResolver().update(
                        ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                        values,
                        SqlDbHelper.COLUMN_ID + "=?",
                        new String[]{Long.toString(dbId)}
                );
            }
        }).start();
    }

    private void refreshShopsList(Cursor cursor) {
        if (shopsList == null) {
            shopsList = new ArrayList<>();
        }
        if (shopsList.size() > 0) {
            shopsList.clear();
        }
        if (!cursor.isClosed()) {
            shopsList.addAll(ContentHelper.getPlaceList(cursor));
        }
        if (shopSpinnerAdapter == null) {
            initShopSpinner();
        } else {
            shopSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void refreshPlacesList(Cursor cursor) {
        if (placesList == null) {
            placesList = new ArrayList<>();
        }
        if (placesList.size() > 0) {
            placesList.clear();
        }
        if (!cursor.isClosed()) {
            placesList.addAll(ContentHelper.getPlaceList(cursor));
        }
        if (placeSpinnerAdapter == null) {
            initPlaceSpinner();
        } else {
            placeSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void refreshPurchaseList(Cursor cursor) {
        if (!cursor.isClosed()) {
            purchaseList = ContentHelper.getPurchaseList(cursor);
            if (!TextUtils.isEmpty(titleState)) {
                listNameEdit.setText(titleState);
                titleState = null;
            } else {
                listNameEdit.setText(purchaseList.getDay());
            }
            initScreen();
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SHOP) {
            getLoaderManager().restartLoader(LOADER_SHOP_ID, null, this);
        }
        if (requestCode == REQUEST_PLACES) {
            getLoaderManager().restartLoader(LOADER_PLACE_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ITEM_ID: {
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_ITEM_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_ID,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_TIMESTAMP,
                };
                String orderBy = SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL + " ASC, "
                        + SqlDbHelper.COLUMN_ID + " DESC";
                return new CursorLoader(
                        getContext(),
                        ShoppingContentProvider.PURCHASE_ITEM_CONTENT_URI,
                        projection,
                        SqlDbHelper.PURCHASE_ITEM_COLUMN_LIST_ID + "=?",
                        new String[]{Long.toString(purchaseList.getIdUser())},
                        orderBy
                );
            }
            case LOADER_SHOP_ID: {
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
                        new String[]{Long.toString(AppConstants.PLACES_SHOP)},
                        orderBy
                );
            }
            case LOADER_PLACE_ID: {
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
            case LOADER_LIST: {
                long dbId = args.getLong(ARG_LIST_ID);
                Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + dbId);
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
                };
                return new CursorLoader(
                        getContext(),
                        uri,
                        projection,
                        null,
                        null,
                        null
                );
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ITEM_ID:
                adapter.changeCursor(data);
                if (purchaseListViewState != null) {
                    purchaseListView.onRestoreInstanceState(purchaseListViewState);
                }
                purchaseListViewState = null;
                progressBar.setVisibility(View.GONE);
                break;
            case LOADER_SHOP_ID:
                refreshShopsList(data);
                break;
            case LOADER_PLACE_ID:
                refreshPlacesList(data);
                break;
            case LOADER_LIST:
                refreshPurchaseList(data);
                break;
        }
        //adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //adapter.swapCursor(null);
        adapter.changeCursor(null);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setDoneIcon() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check_white_24dp);
        }
    }
}
