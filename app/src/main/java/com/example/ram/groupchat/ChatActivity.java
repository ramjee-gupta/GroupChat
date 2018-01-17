package com.example.amardeep.groupchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton mSendBtn;
    private EditText mMessage;
    private String uName;

    private RecyclerView mListView;

   private FirebaseRecyclerAdapter<Message,MessageViewHolder> firebaseRecyclerAdapter;


    private DatabaseReference mRootRef;
    private DatabaseReference mUserref;
    private DatabaseReference mChatRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = (Toolbar) findViewById(R.id.chat_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSendBtn = (ImageButton) findViewById(R.id.send_btn);
        mMessage = (EditText) findViewById(R.id.message_text);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentUser.getUid();

        mRootRef = FirebaseDatabase.getInstance().getReference().child("chat");
        mUserref = FirebaseDatabase.getInstance().getReference().child("Users");//.child(uid);
        mChatRef = FirebaseDatabase.getInstance().getReference().child("chat");
        mAuth = FirebaseAuth.getInstance();

        mListView = (RecyclerView) findViewById(R.id.message_view);




        //----------Recycler view Adapter ---------------------
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        Query messageQuery = mChatRef.orderByChild("time");
        messageQuery.keepSynced(true);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(messageQuery, Message.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options){

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_message_layout, parent, false);

                return new MessageViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {

                holder.setName(model.getName());
                holder.setMessage(model.getMessage());
                holder.setTime(model.getTime());
            }
        };
        mListView.setAdapter(firebaseRecyclerAdapter);

      //  mListView.scrollToPosition();





        //-----------------Extract message from edit text------------click listener for send button----

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String message = mMessage.getText().toString();
                if(!TextUtils.isEmpty( message)){
                    sendMessage( message);
                }
            }
        });

    }

    //--------Method to send messgae to recycler view------------
    private void sendMessage( String message) {

       // mRootRef.push();

        mUserref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                uName = dataSnapshot.child("name").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final String date = DateFormat.getDateTimeInstance().format(new Date());

        Map userMessage = new HashMap<>();
        userMessage.put("name",uName);
        userMessage.put("message",message);
        userMessage.put("time", date);

        mRootRef.push().updateChildren(userMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(databaseError != null)
                {
                    Toast.makeText(ChatActivity.this,"Message couldn't be sent",Toast.LENGTH_LONG).show();

                }else
                {
                    mMessage.setText("");
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }


   //--------Holder Class for Recycler Adapter----------

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        private String message;
        private String time;

        View mView;
        private String name;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setMessage(String message) {

            TextView userMessage = (TextView) mView.findViewById(R.id.message_text);
            userMessage.setText(message);
            //this.message = message;
        }

        public void setTime(String time) {
            TextView sendTime = (TextView) mView.findViewById(R.id.time_id);
            sendTime.setText(time);
            //this.time = time;
        }

        public void setName(String name) {
            TextView senderName = (TextView) mView.findViewById(R.id.sender_name);
            senderName.setText(name);
            //this.name = name;
        }
    }
}
