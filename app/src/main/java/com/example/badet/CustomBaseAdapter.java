package com.example.badet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String listName[];
    String listContacts[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, List<String> NameList, List<String> ContactList){
            this.context = ctx;
            this.listName = NameList.toArray(new String[1]);
            this.listContacts = ContactList.toArray(new String[2]);
            inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return listName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.activity_layout_list, null);
        TextView nameView = (TextView) convertView.findViewById(R.id.con_name);
        TextView contactView = (TextView) convertView.findViewById(R.id.con_number);
        nameView.setText(listName[i]);
        contactView.setText(listContacts[i]);
        return convertView;
    }
}
