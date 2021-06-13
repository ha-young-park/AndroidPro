package com.example.androidproject2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class GridListAdapter extends BaseAdapter {

    ArrayList<ListItem> items = new ArrayList<ListItem>();
    Context context;

    public void addItem(ListItem item) {
        items.add(item);
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }
}
