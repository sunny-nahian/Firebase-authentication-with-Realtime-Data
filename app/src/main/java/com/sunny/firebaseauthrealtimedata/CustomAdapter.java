package com.sunny.firebaseauthrealtimedata;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<userDataModel> {

    private Activity context;
    private List<userDataModel> userList;

    //create construstor using genarate

    public CustomAdapter(Activity context, List<userDataModel> userList) {
        super(context, R.layout.sample_layout, userList);
        this.context = context;
        this.userList = userList;
    }
    //Layout ke View e Convert using getView

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // converting sample_layout using LayoutInflater
        LayoutInflater layoutInflater = context.getLayoutInflater();
       View view = layoutInflater.inflate(R.layout.sample_layout,null,true);

       userDataModel userDataModel = userList.get(position);
        TextView t1 = view.findViewById(R.id.idfNameListView);
        TextView t2 = view.findViewById(R.id.idlNameListView);
        TextView t3 = view.findViewById(R.id.idEmailListView);
        TextView t4 = view.findViewById(R.id.idPhoneListView);
        TextView t5 = view.findViewById(R.id.idPasswordListView);

        t1.setText("Name: " +userDataModel.getFirst());
        t2.setText(userDataModel.getLast());
        t3.setText("Email: " +userDataModel.getEmail());
        t4.setText("Phone: " +userDataModel.getPhone());
        t5.setText("Password: " +userDataModel.getPassword());

        return view;
    }
}
