package com.example.inform;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View view)
    {
        Intent intent = new Intent(this, Bus.class);
        startActivity(intent);
    }

        public void click2(View view)
        {
            Intent intent = new Intent(this, Food.class);
            startActivity(intent);
        }

        public void click3(View view)
        {
            Intent intent = new Intent(this, Map.class);
            startActivity(intent);
        }

        public void click4(View view)
        {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }

        public void click5(View view)
        {
            Intent intent = new Intent(this, My.class);
            startActivity(intent);
        }
    public void click6(View view)
    {
        Intent intent = new Intent(this, ProActivity.class);
        startActivity(intent);
    }


}
