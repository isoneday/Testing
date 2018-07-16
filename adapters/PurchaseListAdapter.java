package com.teamproject.plastikproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.modeldataskedule.Responsedaske;

import java.util.List;

/**
 * Created by rage on 3/21/15.
 */
public class PurchaseListAdapter extends BaseAdapter {
Context con ;
    List<Responsedaske> responsedaskes;
    public PurchaseListAdapter(Context context, List<Responsedaske> responsedaskes) {
      this.responsedaskes=responsedaskes;
      con=context;

    }

    @Override
    public int getCount() {
        return responsedaskes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =inflater.inflate(R.layout.item_purchase_view,null);
        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView description = (TextView)convertView.findViewById(R.id.description);
        title.setText(responsedaskes.get(position).getDay());
        description.setText(responsedaskes.get(position).getTime());
        return convertView;
    }
}
//    private int resource;
//    private LayoutInflater inflater;
//
//    @TargetApi(8)
//    public PurchaseListAdapter(Context context, Cursor c, int resource) {
//        super(context, c);
//        this.resource = resource;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @TargetApi(11)
//    public PurchaseListAdapter(Context context, Cursor c, int flags, int resource) {
//        super(context, c, flags);
//        this.resource = resource;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        View view = inflater.inflate(resource, parent, false);
//        Holder holder = new Holder();
//        holder.title = (TextView) view.findViewById(R.id.title);
//        holder.description = (TextView) view.findViewById(R.id.description);
//        holder.icon = (ImageView) view.findViewById(R.id.icon);
//        view.setTag(holder);
//
//        populateHolder(cursor, holder);
//
//        return view;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        Holder holder = (Holder) view.getTag();
//
//        populateHolder(cursor, holder);
//    }
//
//    private class Holder {
//        TextView title;
//        TextView description;
//        ImageView icon;
//    }
//
//    private void populateHolder(Cursor cursor, Holder holder) {
//        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME);
//        int indexTimeCreate = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE);
//        int indexIsDone = cursor.getColumnIndex(SqlDbHelper.PURCHASE_LIST_COLUMN_DONE);
//
//        long millis = cursor.getLong(indexTimeCreate);
//        boolean isDone = cursor.getInt(indexIsDone) > 0;
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy kk:mm:ss", Locale.ENGLISH);
//        String dateString = formatter.format(new Date(millis));
//
//        holder.title.setText(cursor.getString(indexName));
//        holder.description.setText(dateString);
//
//        if (isDone) {
//            holder.icon.setVisibility(View.VISIBLE);
//        }
//    }
//}
