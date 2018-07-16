package com.teamproject.plastikproject.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.modeldataskedule.Responsedaske;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/**
 * Created by rage on 3/21/15.
 */
public class PurchaseListAdapterori extends CursorAdapter {
    private int resource;
    private LayoutInflater inflater;
    List<Responsedaske> responsedas;
    @TargetApi(8)
    public PurchaseListAdapterori(Context context, Cursor c, int resource, List<Responsedaske> responsedaskes) {
        super(context, c);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
        responsedas=responsedaskes;
    }

    @TargetApi(11)
    public PurchaseListAdapterori(Context context, Cursor c, int flags, int resource, List<Responsedaske> responsedaskes) {
        super(context, c, flags);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
        responsedas=responsedaskes;
    }

    @Override
    public int getCount() {
        return responsedas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Holder holder = new Holder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.description = (TextView) view.findViewById(R.id.description);
        holder.icon = (ImageView) view.findViewById(R.id.icon);
        view.setTag(holder);

//        populateHolder(cursor, holder);

        return view;
   //     return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Holder holder = (Holder) parent.getTag();
        // int d = cursor.getPosition();
        holder.title.setText(responsedas.get(0).getDay());
        holder.description.setText(responsedas.get(0).getTime());

        return null;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Holder holder = (Holder) view.getTag();
       // int d = cursor.getPosition();
        holder.title.setText(responsedas.get(0).getDay());
        holder.description.setText(responsedas.get(0).getTime());

//        populateHolder(cursor, holder);
    }

    private class Holder {
        TextView title;
        TextView description;
        ImageView icon;
    }

    private void populateHolder(Cursor cursor, Holder holder) {
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
        int indexTimeCreate = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE);
        int indexIsDone = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE);

        long millis = cursor.getLong(indexTimeCreate);
        boolean isDone = cursor.getInt(indexIsDone) > 0;

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy kk:mm:ss", Locale.ENGLISH);
        String dateString = formatter.format(new Date(millis));

        holder.title.setText(cursor.getString(indexName));
        holder.description.setText("ss"+dateString);

        if (isDone) {
            holder.icon.setVisibility(View.VISIBLE);
        }
    }
}
