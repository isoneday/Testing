package com.teamproject.plastikproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.model.PlacesModel;

import java.util.List;

/**
 * Created by rage on 3/4/15.
 */
public class PlaceAdapter2 extends BaseAdapter {
    private static final String TAG = PlaceAdapter2.class.getSimpleName();
    private Context context;
    private int resource;
    private List<PlacesModel> shopLists2;
   // private List<Response> shopLists2;
    private LayoutInflater inflater;

    public PlaceAdapter2(Context context, int resource, List<PlacesModel> shopLists) {
        this.context = context;
        this.resource = resource;
        this.shopLists2 = shopLists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shopLists2.size();
    }

    @Override
    public PlacesModel getItem(int position) {
        return shopLists2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public long getItemId(int position) {
//      //  return Long.parseLong(String.valueOf(shopLists2.get(position).getLat()));
//    return shopLists2.get();
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacesModel shop = shopLists2.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.title.setText(shop.getShopDescription());
        return convertView;
    }

    private class Holder {
        TextView title;
    }
}