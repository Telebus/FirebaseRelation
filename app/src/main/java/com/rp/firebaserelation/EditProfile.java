package com.rp.firebaserelation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName, editTextPhone;

    User user;

    DatabaseReference databaseUsers;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnEdit).setOnClickListener(this);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);

                editTextName.setText(user.getName());
                editTextPhone.setText(user.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void editUser() {

        final String name = editTextName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if(name.isEmpty()) {

            editTextName.setError("Name required!");
            editTextName.requestFocus();
            return;

        }
        if(phone.length() != 9) {

            editTextName.setError("Invalid phone number!");
            editTextName.requestFocus();
            return;

        }

        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        user.setName(name);
        user.setPhone(phone);

        databaseUsers.setValue(user);
        Toast.makeText(this, "Data edited successfully!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnEdit)){

            editUser();
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }
    }
}
