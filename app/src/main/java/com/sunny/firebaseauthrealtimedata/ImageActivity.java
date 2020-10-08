package com.sunny.firebaseauthrealtimedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter; //
    List<Upload> uploadList;
    DatabaseReference databaseReference;

    ProgressBar progressBar1;

    FirebaseStorage firebaseStorage; // for image delete etc....

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for title bar remove
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);

        firebaseStorage = FirebaseStorage.getInstance();
        progressBar1 = findViewById(R.id.RecyclerprogressBarId);

        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Upload"); //Same as Profile page Reference

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadList.clear(); // for List clear

                // database theke data fatch
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Upload upload = dataSnapshot1.getValue(Upload.class);
                    upload.setKey(dataSnapshot1.getKey()); // for Context Image Delete etc
                    uploadList.add(upload);
                }
                    recyclerViewAdapter = new RecyclerViewAdapter(ImageActivity.this,uploadList);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    //for recyclerView Listener Add
                   recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                       @Override
                       public void onItemClick(int position) {
                           String text = uploadList.get(position).getImageName();
//                           Toast.makeText(getApplicationContext(), text+ "is selected" + position,Toast.LENGTH_LONG).show();
                       }

                       @Override
                       public void onDelete(int position) {
                           Upload selectedItem = uploadList.get(position);
                           final String key = selectedItem.getKey();
                           StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
                           storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   databaseReference.child(key).removeValue();
                                   Toast.makeText(getApplicationContext(),"Item is deleted",Toast.LENGTH_LONG).show();
                               }
                           });
                       }
                       @Override
                       public void onUpdate(int position) {
                       }
                   });

                progressBar1.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error: "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar1.setVisibility(View.INVISIBLE);
            }
        });
    }
}
