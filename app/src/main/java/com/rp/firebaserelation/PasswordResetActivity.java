package com.rp.firebaserelation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail;
    Button btnSend;
    Button btnCancel;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        editTextEmail = findViewById(R.id.editTextEmail);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        editTextEmail.setText(email);

        btnSend.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == btnSend){

            String email = editTextEmail.getText().toString().trim();

            if(TextUtils.isEmpty(email)) {

                Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();

            } else {

                progressDialog.setMessage("Sending email...");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {

                                    Toast.makeText(PasswordResetActivity.this, "Password reset email sent", Toast.LENGTH_LONG).show();

                                } else {

                                    Toast.makeText(PasswordResetActivity.this, "Error sending password reset email", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }

        }
        if(v == btnCancel){

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }

    }
}
