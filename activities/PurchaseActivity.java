package com.teamproject.plastikproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.fragments.PurchaseEditFragmentbaru;
import com.teamproject.plastikproject.fragments.PurchaseManageFragmentoribaru;
import com.teamproject.plastikproject.helpers.ActivityHelper;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.helpers.SharedPrefHelper;


/**
 * Created by rage on 08.02.15.
 */
public class PurchaseActivity extends BaseActivity implements PurchaseManageFragmentoribaru.OnPurchaseListMainFragmentListener, FragmentManager.OnBackStackChangedListener {
    private static final String TAG = PurchaseActivity.class.getSimpleName();
    private PurchaseEditFragmentbaru purchaseListEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.getInstance().setIsMainActivity(true);
        initDrawer();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(this);
            showPurchaseLists();
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            long listDbId = intent.getExtras().getLong(AppConstants.NOTIFICATION_LIST_ARGS, -1);
            if (listDbId >= 0) {
                showList(listDbId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityHelper.getInstance().setIsMainActivity(true);
        superOnResume();
        SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();
        if (!sharedPrefHelper.isLogin()) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (activityHelper.getGlobalId() != activityHelper.getPurchaseMenuId()) {
            if (activityHelper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_LIST
                    || activityHelper.getGlobalId() == AppConstants.MENU_SHOW_PURCHASE_ARCHIVE) {
                menuShowPurchaseLists(activityHelper.getGlobalId());
            } else {
                activityHelper.setGlobalId(activityHelper.getPurchaseMenuId());
            }
        }
    }

    @Override
    public void onPurchaseListMainFragmentClickListener() {
        purchaseListEditFragment = new PurchaseEditFragmentbaru();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, purchaseListEditFragment)
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onPurchaseListMainFragmentClickListener(long id) {
        showList(id);
    }

    private void showPurchaseLists(){
        activityHelper.setGlobalId(activityHelper.getPurchaseMenuId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PurchaseManageFragmentoribaru())
                .commit();
    }

    private void showList(long id) {
        if (purchaseListEditFragment != null) {
            purchaseListEditFragment.onBackPressed();
            getSupportFragmentManager().popBackStack();
            purchaseListEditFragment = null;
        }
        purchaseListEditFragment = PurchaseEditFragmentbaru.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, purchaseListEditFragment)
                .addToBackStack(AppConstants.BACK_STACK_PURCHASE)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackEntryCount <= 0){
            purchaseListEditFragment = null;
        }
    }

    @Override
    public void menuOnClick(int menuId) {
        super.menuOnClick(menuId);
        if (menuId != AppConstants.MENU_LOGOUT && purchaseListEditFragment != null) {
            purchaseListEditFragment.onBackPressed();
            purchaseListEditFragment = null;
        }
    }

    @Override
    public void menuShowPurchaseLists(int menuId) {
        activityHelper.setPurchaseMenuId(menuId);
        showPurchaseLists();
    }

    @Override
    public void menuLogout() {
        super.menuLogout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
