package com.example.amardeep.groupchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName,mEmail,mPassword,mConfirmPassword;
    private Button mRegButton;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = (EditText) findViewById(R.id.reg_name);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mPassword =(EditText) findViewById(R.id.reg_password);
        mConfirmPassword=(EditText) findViewById(R.id.confirm_password);

        mToolbar = (Toolbar) findViewById(R.id.reg_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please Wait");
        mProgress.setCanceledOnTouchOutside(false);

        mRegButton = (Button) findViewById(R.id.registerBtn);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                String confirmPass = mConfirmPassword.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirmPass))
                {
                    if(pass.equals(confirmPass))
                    {
                        registerUser(name,email,pass);
                        mProgress.show();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Password didn't match",Toast.LENGTH_LONG).show();
                        mProgress.hide();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,"Fill all the fields",Toast.LENGTH_LONG).show();
                    mProgress.hide();
                }

            }
        });


    }

    private void registerUser(final String name, String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    Map addUser = new HashMap();
                    addUser.put("name",name);
                    addUser.put("device_token",device_token);

                    mDatabase.child(uid).updateChildren(addUser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){
                                Toast.makeText(RegisterActivity.this,"Errorin database !",Toast.LENGTH_SHORT).show();

                            }
                            mProgress.dismiss();

                            Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    mProgress.hide();
                }

            }
        });

    }
}
