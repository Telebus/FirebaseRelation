package com.rp.firebaserelation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewWelcome;

    DatabaseReference databaseUsers;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        textViewWelcome = findViewById(R.id.textViewWelcome);

        Intent intent = getIntent();

        String username = intent.getStringExtra(MainActivity.USER_NAME);

        textViewWelcome.setText("Welcome, " + username);

        findViewById(R.id.btnLogout).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnLogout)){

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
    }
}
