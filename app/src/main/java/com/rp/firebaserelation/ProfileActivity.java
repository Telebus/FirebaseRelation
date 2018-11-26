package com.rp.firebaserelation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewVerified;
    TextView textViewWelcome;
    DatabaseReference databaseUsers;
    FirebaseAuth firebaseAuth;
    Button btnChangePass;
    Button btnDeleteAccount;
    ProgressDialog dialog;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnDeleteAccount =  findViewById(R.id.btnDeleteAccount);
        dialog = new ProgressDialog(this);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("All your data will be lost");
                alertDialog.setPositiveButton("Delete anyway", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        delete();

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;

                    }
                });
                alertDialog.show();
            }
        });


        btnChangePass = findViewById(R.id.btnChangePass);

        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        textViewWelcome = findViewById(R.id.textViewWelcome);

        textViewVerified = findViewById(R.id.textViewVerified);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (user != null){

                    textViewWelcome.setText(user.getName());
                    if(firebaseUser.isEmailVerified()){

                        textViewVerified.setText("Email Verified");

                    } else {

                        textViewVerified.setText("Email not Verified (Click here to verify)");
                        textViewVerified.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ProfileActivity.this, "Email verification sent, check your inbox!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(this);
        findViewById(R.id.btnEdit).setOnClickListener(this);
        btnChangePass.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }

    }

    public void delete(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

            dialog.setMessage("Changing password!");
            dialog.show();

            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            dialog.dismiss();

                            if (task.isSuccessful()){

                                firebaseAuth.signOut();
                                databaseUsers.removeValue();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(getApplicationContext(), "Your account has been deleted!", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getApplicationContext(), "Your account could not be deleted!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnLogout)){

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }
        if(v == findViewById(R.id.btnEdit)){

            finish();
            startActivity(new Intent(getApplicationContext(), EditProfile.class));

        }

        if (v == btnChangePass){

            finish();
            startActivity(new Intent(getApplicationContext(), ChangePassword.class));

        }
    }
}
