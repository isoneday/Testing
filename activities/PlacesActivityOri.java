package com.teamproject.plastikproject.activities;

import android.content.Intent;
import android.os.Bundle;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.fragments.PlacesManageFragmentOri;
import com.teamproject.plastikproject.helpers.ActivityHelper;
import com.teamproject.plastikproject.helpers.AppConstants;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class PlacesActivityOri extends BaseActivity {
    private static final String TAG = PlacesActivityOri.class.getSimpleName();
    private int menuItemId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.getInstance().setIsMainActivity(false);
        Intent args = getIntent();
        if (args != null) {
            menuItemId = args.getExtras().getInt(AppConstants.EXTRA_MENU_ITEM);
        }
        initDrawer();
        if (savedInstanceState == null && menuItemId >= 0) {
            showFragment(menuItemId);
        }
    }

    private void showFragment(int id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlacesManageFragmentOri.newInstance(id))
                .commit();
    }

    @Override
    public void menuManageShop() {
        showFragment(AppConstants.MENU_SHOW_SHOPS);
    }

    @Override
    public void menuManagePlaces() {
        showFragment(AppConstants.MENU_SHOW_PLACES);
    }
}
