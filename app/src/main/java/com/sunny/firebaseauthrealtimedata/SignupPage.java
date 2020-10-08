package com.sunny.firebaseauthrealtimedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPage extends AppCompatActivity{
    private EditText signupfirstname, signUplastname, signupEmail, signupPhone, signupPassword, signupConpassword;
    private Button vbtnsignup,vviewbtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    // For Data Store using Realtime data
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for title bar remove
//      requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_page);
        this.setTitle("Sign Up Page");
        databaseReference = FirebaseDatabase.getInstance().getReference("user_details");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.signupprogressBar);

        signupfirstname =(EditText) findViewById(R.id.signUpFirstname);
        signUplastname =(EditText) findViewById(R.id.signUpLastname);
        signupEmail =(EditText) findViewById(R.id.signUpEmail);
        signupPhone =(EditText) findViewById(R.id.signUpPhone);
        signupPassword =(EditText) findViewById(R.id.signUpPass);
        signupConpassword =(EditText) findViewById(R.id.signUpConpass);
        vbtnsignup =(Button) findViewById(R.id.Signupbtn);
        vviewbtn = (Button)findViewById(R.id.idviewbtn);

        vbtnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });
        vviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPage.this,DetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userRegister() {
        String firstName = signupfirstname.getText().toString().trim();
        String lastName = signUplastname.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String phone = signupPhone.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String conpassword = signupConpassword.getText().toString().trim();

        //Write data for sharedpreference
        if (email.equals("") && password.equals("") && conpassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter the data", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("com.sunny.firebaseauthrealtimedata_userDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit(); // edot method dea data write korbo
            editor.putString("usernameKey",firstName);
            editor.putString("usernameKey2",lastName);
            editor.putString("emailKey",email);
            editor.putString("phoneKey",phone);
            editor.putString("passwoedKey",password);
            editor.putString("conpassKey",conpassword);
            editor.putBoolean("ISLOGGEDIN",true); // avi
            editor.commit();
            signupfirstname.setText("");
            signUplastname.setText("");
            signupEmail.setText("");
            signupPhone.setText("");
            signupPassword.setText("");
            signupConpassword.setText("");
            Toast.makeText(getApplicationContext(), "Sharedpreference is stored",Toast.LENGTH_SHORT).show();
        } // end sharedpreference

        //checking the validity of the email
        if (firstName.isEmpty()){
            signupfirstname.setError("Enter first Name");
            signupfirstname.requestFocus();
        }else if (lastName.isEmpty()){
            signupfirstname.setError("Enter Last Name");
            signupfirstname.requestFocus();
        }else if(email.isEmpty())
        {
            signupEmail.setError("Enter an email address");
            signupEmail.requestFocus();
            return;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signupEmail.setError("Enter a valid email address");
            signupEmail.requestFocus();
        }
        else if (phone.isEmpty()){
            signupPhone.setError("Enter phone number");
            signupPhone.requestFocus();
        }
        else if (password.length()<5){
            signupPassword.setError("Enter Minimum six digits");
            signupPassword.requestFocus();
        }
        else if (password.isEmpty()){
            signupPassword.setError("Enter Password");
            signupPassword.requestFocus();
        }else
        if (conpassword.isEmpty()){
            signupConpassword.setError("Enter Confirm Password");
            signupConpassword.requestFocus();
        }
        //checking the validity of the password
        else if(email.isEmpty())
        {
            signupEmail.setError("Enter a password");
            signupEmail.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(getApplicationContext(),"Register is successful",Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(SignupPage.this,ProfilePage.class);
                           startActivity(intent);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User is already Registered",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Error :"  + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            // For Data store using realtime database
            String key = databaseReference.push().getKey();
            userDataModel registerDataModel = new userDataModel(firstName,lastName,email,phone,password,conpassword); // RegisterDataNodek ar object create if access data from modle class
            databaseReference.child(key).setValue(registerDataModel); // Database e register info rekhe dlam
            Toast.makeText(getApplicationContext(),"Realtime Data store successfully",Toast.LENGTH_SHORT).show();
            signupfirstname.setText("");
            signUplastname.setText("");
            signupEmail.setText("");
            signupPhone.setText("");
            signupPassword.setText("");
            signupConpassword.setText(""); // end
        }
    }
}
