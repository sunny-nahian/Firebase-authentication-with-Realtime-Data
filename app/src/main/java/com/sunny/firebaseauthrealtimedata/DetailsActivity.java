package com.sunny.firebaseauthrealtimedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    private List<userDataModel> userList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for title bar remove
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details);

        databaseReference = FirebaseDatabase.getInstance().getReference("user_details");

        userList = new ArrayList<>();
        customAdapter = new CustomAdapter(DetailsActivity.this,userList);

        listView = findViewById(R.id.idDetailsListview);
    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

              for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                  userDataModel userDataModel = dataSnapshot1.getValue(com.sunny.firebaseauthrealtimedata.userDataModel.class);
                  userList.add(userDataModel);
              }

              listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}
