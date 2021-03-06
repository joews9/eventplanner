package com.event.joe.savedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.event.joe.com.event.joe.eventlistadapter.EventListDetailProvider;
import com.event.joe.eventplanner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Aimee and Baby on 16/02/2016.
 */
public class SavedListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public SavedListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{
        TextView eventTitle;
        TextView eventDate;


    }
    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.saved_event_list_cell, parent, false);
            handler = new DataHandler();
            handler.eventDate = (TextView)row.findViewById(R.id.textItemSaved2);
            handler.eventTitle = (TextView)row.findViewById(R.id.textItemSaved);
            row.setTag(handler);

        }else{
            handler = (DataHandler)row.getTag();
        }
        SavedListDetailProvider eventProvider = (SavedListDetailProvider)this.getItem(position);
        handler.eventTitle.setText(eventProvider.getEventTitle());
        handler.eventDate.setText(eventProvider.getEventDate());


        return row;
    }
}
