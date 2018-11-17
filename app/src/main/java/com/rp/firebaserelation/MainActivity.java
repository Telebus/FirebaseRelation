package com.rp.firebaserelation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String USER_DATA = "userdata";

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;

    private FirebaseAuth firebaseAuth;

    DatabaseReference databaseUsers;

    List<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPhone = findViewById(R.id.editTextPhone);

        findViewById(R.id.btnRegister).setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        usersList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Attach a listener to read the data at our posts reference
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();

                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    User user = usersSnapshot.getValue(User.class);

                    usersList.add(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        if(firebaseAuth.getCurrentUser() != null) {

            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }

    }

    private void registerUser() {

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if(name.isEmpty()) {

            editTextName.setError("Name required!");
            editTextName.requestFocus();
            return;

        }
        if(password.isEmpty()) {

            editTextName.setError("Password required!");
            editTextName.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            editTextName.setError("Invalid email!");
            editTextName.requestFocus();
            return;

        }
        if(phone.length() != 9) {

            editTextName.setError("Invalid phone number!");
            editTextName.requestFocus();
            return;

        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            final User user = new User(name, email, phone);

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){



                                        Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                                    } else {

                                        Toast.makeText(MainActivity.this, "Could not register! Try again later", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        } else {

                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnRegister)){

            registerUser();

        }
    }
}
