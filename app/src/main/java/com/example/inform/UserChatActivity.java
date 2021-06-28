package com.example.inform;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserChatActivity extends AppCompatActivity{

    private String CHAT_NAME;
    private String USER_NAME;
    private String HELP_NAME;
    private String ADDRESS_NAME;

    private ListView chat_view;
    private Button chat_end;
    private EditText userchat_edit;
    private Button userchat_send;

    private String message;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private  FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchat);


        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chat_view);        // 채팅대화 리스트뷰
        chat_end = (Button) findViewById(R.id.chat_end);        // 채팅방 종료버튼
        userchat_edit = (EditText) findViewById(R.id.userchat_edit);
        userchat_send = (Button) findViewById(R.id.userchat_sent);

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();   // 사용자 정보 가져오기
        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장


        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");      // 채팅방 이름
        USER_NAME = intent.getStringExtra("userName");      // 사용자uid


        // 채팅 방 입장
        openChat(CHAT_NAME, USER_NAME);
        chat_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);



        databaseReference.child("chatuser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);           // Use의 모든 데이터를 chatuser의 데이터로 입력.
                HELP_NAME =user.getName();      //HELP_NAME = 사용자 이름
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        userchat_send.setOnClickListener(new View.OnClickListener() {           // 메시지 전송
            @Override
            public void onClick(View v) {
                if (userchat_edit.getText().toString().equals(""))
                    return;
                ChatDTO chat = new ChatDTO(HELP_NAME, userchat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("NewEyes").child(CHAT_NAME).child(USER_NAME).push().setValue(chat); // 데이터 푸쉬
                userchat_edit.setText(""); //입력창 초기화

            }
        });


        chat_end.setOnClickListener(new View.OnClickListener() {        // 채팅방 종료 버튼 눌렀을 때
            @Override
            public void onClick(View v) {

                    firebaseDatabase.getReference().child("NewEyes").child(CHAT_NAME).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {    // 채팅방 삭제
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserChatActivity.this, "채팅방을 종료합니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //유저 채팅방 비활성화
                    databaseReference.child("chatuser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReference.child("chatuser").child(firebaseUser.getUid()).child("chatName").setValue("");    // 값 기본값으로 다시 초기화
                            databaseReference.child("chatuser").child(firebaseUser.getUid()).child("chat").setValue(false);      // 값 기본값으로 다시 초기화
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    finish();
            }
        });
    }


    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);         // username, message
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
        chat_view.setSelection(adapter.getCount() - 1);

    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
    }

    private void openChat(String chatName, String UserName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);
        databaseReference.child("NewEyes").child(CHAT_NAME).addListenerForSingleValueEvent(new ValueEventListener() {    //  목적지 채팅방의 모든 데이터 불러옴.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("NewEyes").child(CHAT_NAME).child(USER_NAME).addChildEventListener(new ChildEventListener() {    // "chat" 목적지(채팅방), uid 하위 데이터들을 불러옴.
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {                 // 메시지 출력.
                        addMessage(dataSnapshot, adapter);
                        Log.e("LOG", "s:"+s);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        removeMessage(dataSnapshot, adapter);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
//        databaseReference.child("NewEyes").child(chatName).child(UserName).addChildEventListener(new ChildEventListener() {         // 채팅방(목적지), uid 데이터 불러옴.
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                addMessage(dataSnapshot, adapter);
//                Log.e("LOG", "s:"+s);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                removeMessage(dataSnapshot, adapter);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

}
