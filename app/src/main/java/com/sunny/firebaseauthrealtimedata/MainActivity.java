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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity{
    private EditText loginEmail, loginPassword;
    private TextView vsignUptxt;
    private Button loginbtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

//    for Sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for title bar remove
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.setTitle("Log in Page");

        final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        if(isloggedin)
        {
            Intent main = new Intent(MainActivity.this,ProfilePage.class);
            startActivity(main);
            finish();
        }

//        existLogin();

        progressBar =findViewById(R.id.loginprogressBar);
        mAuth = FirebaseAuth.getInstance();

        loginEmail =(EditText) findViewById(R.id.loginEmaild);
        loginPassword =(EditText) findViewById(R.id.loginPassId);
        loginbtn =(Button) findViewById(R.id.loginBtnId);
        vsignUptxt =(TextView) findViewById(R.id.signuptext);


        vsignUptxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignupPage.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               userLogin();
            }
        });
    }

    private void userLogin() {
        // mail , password check already in or not
        String lemail = loginEmail.getText().toString().trim();
        String lpassword = loginPassword.getText().toString().trim();

        sharedPreferences = getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("EMAIL_ID",lemail);
        editor.putString("PASSWORD",lpassword);

        editor.putBoolean("ISLOGGEDIN",true);
        editor.commit();

        //checking the validity of the email
        if(lemail.isEmpty())
        {
            loginEmail.setError("Enter an email address");
            loginEmail.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(lemail).matches())
        {
            loginEmail.setError("Enter a valid email address");
            loginEmail.requestFocus();
            return;
        }

        //checking the validity of the password
        if(lpassword.isEmpty())
        {
            loginPassword.setError("Enter a password");
            loginPassword.requestFocus();
            return;
        }
         if (lpassword.length()<5){
            loginPassword.setError("Enter Minimum six digits");
            loginPassword.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(lemail,lpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 progressBar.setVisibility(View.GONE);
                 if (task.isSuccessful()){
                     Intent intent = new Intent(MainActivity.this,ProfilePage.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(intent);
                 } else {
                     Toast.makeText(getApplicationContext(),"Couldn't find your account",Toast.LENGTH_SHORT).show();
                 }
             }
         });
        loginEmail.setText("");
        loginPassword.setText("");
    }

//    //Sharedpreference Call
//    public void existLogin(){
//        SharedPreferences sharedPreferences = getSharedPreferences("com.sunny.firebaseauthrealtimedata_userDetails", Context.MODE_PRIVATE);
//
//    }

}
