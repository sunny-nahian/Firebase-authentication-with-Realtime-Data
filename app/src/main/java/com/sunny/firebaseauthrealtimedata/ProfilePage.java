package com.sunny.firebaseauthrealtimedata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfilePage extends AppCompatActivity {
    FirebaseAuth mAuth;

    // For Image Choose,Display .......
    private Button chooseButton, saveButton, displayButton;
    private ImageView imageView;
    private EditText imageNameEditText;
    private ProgressBar progressBar;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 1 ; // end
    // For data Storage
    DatabaseReference databaseReference;
    StorageReference storageReference; // end

    // for upload 1 task complete
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for title bar remove
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_page);

        // For data Storage
        databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
        storageReference = FirebaseStorage.getInstance().getReference("Upload"); // end

        // For Image Choose,Display ......
        chooseButton = findViewById(R.id.chooseImageButton);
        saveButton = findViewById(R.id.saveImageButton);
        displayButton = findViewById(R.id.displayImageButton);
        imageNameEditText = findViewById(R.id.imageNameEditTextId);
        progressBar = findViewById(R.id.progressbarId);
        imageView = findViewById(R.id.imageViewId);  // end

        mAuth = FirebaseAuth.getInstance();

        // For Image Choose,Display ......
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(),"Uploading in progress",Toast.LENGTH_LONG).show();
                }else {
                    savedata();
                }
            }
        });
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePage.this,ImageActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method For Image Choose,Display ......
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST); // end choose
    }
    //for Image Load
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!= null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri)
                    .fit()
                    .centerCrop()
                    .into(imageView); }
    } // end load work


    // For Getting the extention of the Image ......
    public String getFileExtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    } // end Image extention

    // For Image Choose,Display ......
    private void savedata() {
        final String imageName = imageNameEditText.getText().toString().trim();
        if (imageName.isEmpty()){
            imageNameEditText.setError("Enter image name");
            imageNameEditText.requestFocus();
            return;
        }
        StorageReference ref = storageReference.child(System.currentTimeMillis()+ "." +getFileExtention(imageUri));

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(getApplicationContext(),"Image store Successful",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUrl = uriTask.getResult();

                        Upload upload = new Upload(imageName ,downloadUrl.toString()); // Image refrence link Storage e Save

                        String uploaduniqueKey = databaseReference.push().getKey(); //for Unique Key generate

                        databaseReference.child(uploaduniqueKey).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(),"Image Not store Successful",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // working Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.idsignout){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(ProfilePage.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }  // end
}
