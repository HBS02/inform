package com.example.inform;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HelpActivity extends AppCompatActivity {
    private EditText chat_name;
    private ImageButton chat_button;

    private Intent intent;

    Map<String, Object> map = new HashMap<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();            // 로그인 사용자 정보 가져옴,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();

        chat_name = (EditText) findViewById(R.id.chat_name);            // 목적지
        chat_button = (ImageButton) findViewById(R.id.chat_button);          // 개설버튼

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (chat_name.getText().toString().equals(""))
                        return;

                    final String CHAT_NAME = chat_name.getText().toString();        // 목적지 입력

                    map.put(firebaseuser.getUid(), "");          // firebase.getuid 값 = 로그인사용자의 uid 입력
                    databaseReference.child("NewEyes").child(CHAT_NAME).updateChildren(map);     // CHAT_NAME(목적지) 하위 값에 getuid = 로그인uid 값 입력

                    databaseReference.child("chatuser").child(firebaseuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {    // "chatuser"데이터의 uid 하위 데이터들을 불러옴.
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {                  // 데이터 변경
                            databaseReference.child("chatuser").child(firebaseuser.getUid()).child("chatName").setValue(CHAT_NAME);       // 채팅방 개설하면 "chatuser"데이터의 chatname 초기화되어 있는 ""값을 입력한 목적지로 변경
                            databaseReference.child("chatuser").child(firebaseuser.getUid()).child("chat").setValue(true);        // 채팅방 개설하면 "chatuser"의 chat 데이터를 true 로 변경
                            // 기존 chatName과 chat의 데이터는 "", false (User.class)
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Intent intent = new Intent(HelpActivity.this, UserChatActivity.class);  //userchatactivity로 값 (chatName, userName)전송
                    intent.putExtra("chatName", CHAT_NAME);     // chatName = "목적지"
                    intent.putExtra("userName", firebaseuser.getUid());     // userName = "로그인된 사용자의 uid"
                    startActivity(intent);

            }
        });


    }


}