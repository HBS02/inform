package com.example.inform;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class ChatLayout extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();            // 로그인 사용자 정보 가져옴,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlayout);
        ImageButton button1 = (ImageButton) findViewById(R.id.button);
        ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    databaseReference.child("chatuser").child(firebaseuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user =dataSnapshot.getValue(User.class);           // Use의 모든 데이터를 chatuser의 데이터로 입력.
                            if(user.chat==true)
                            {
                                Intent intent = new Intent(getApplicationContext(), UserChatActivity.class);
                                intent.putExtra("chatName", user.chatName);         // 보내는 chatName 데이터는 초기데이터인 초기화된 값 ""
                                intent.putExtra("userName", firebaseuser.getUid());    // 여기부분 수정.
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                }
        });

    }



}