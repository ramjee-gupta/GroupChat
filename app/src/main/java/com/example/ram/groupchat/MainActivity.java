package com.example.amardeep.groupchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private Button mJoingroupBtn;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();//.child("chat");

        mJoingroupBtn = (Button) findViewById(R.id.group_btn);

        mToolbar = (Toolbar) findViewById(R.id.welcome_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Chat");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mJoingroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  FirebaseUser currentUser = mAuth.getCurrentUser();
                String uid = currentUser.getUid();

                mDatabaseRef.child("Users").child(uid).child("name");

                Map addUser = new HashMap();
              //  addUser.put()

                */
                Intent chatIntent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(chatIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {

            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.logout_btn){
            FirebaseAuth.getInstance().signOut();

            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        return  true;

    }
}
