package com.example.amardeep.groupchat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView mRegisterButton, mResetBtn;
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    private ProgressDialog mProgres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword =(EditText) findViewById(R.id.login_password);
        mLoginBtn =(Button) findViewById(R.id.login_btn);
        mRegisterButton = (TextView) findViewById(R.id.reg_btn);
        mResetBtn = (TextView) findViewById(R.id.reset_btn);


        mProgres = new ProgressDialog(this);
        mProgres.setMessage("Loading...");

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.login_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Chat");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String email = mEmail.getText().toString();
                 String pass = mPassword.getText().toString();

                 if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
                     loginUser(email,pass);
                     mProgres.show();
                 }else{
                     Toast.makeText(LoginActivity.this,"Invalid User and Password",Toast.LENGTH_LONG).show();
                 }
            }
        });

        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });


    }

    private void resetPassword() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View alertPrompt = inflater.inflate(R.layout.alert_prompt,null);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setView(alertPrompt);
        final EditText userInput = (EditText) alertPrompt
                .findViewById(R.id.email_box);
       // alertBuilder.setTitle("Enter your email");

        alertBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String emailAddress =  userInput.getText().toString();
                        if(!TextUtils.isEmpty(emailAddress))
                        {
                            mAuth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Reset Link set to your email", Toast.LENGTH_SHORT).show();

                                                //AlertDialog alert = new AlertDialog(this);
                                                // Log.d(TAG, "Email sent.");
                                            }else{

                                                Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }else{
                            Toast.makeText(LoginActivity.this, "Enter your email id", Toast.LENGTH_SHORT).show();
                        }




                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }

    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    mProgres.dismiss();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Something went wrong ",Toast.LENGTH_LONG).show();
                    mProgres.hide();
                }

            }
        });


    }
}
