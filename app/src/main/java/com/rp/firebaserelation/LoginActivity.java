package com.rp.firebaserelation;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewRegister;
    TextView textViewForgot;
    Button btnLogin;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgot = findViewById(R.id.textViewForgot);
        btnLogin = findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(this);

        textViewRegister.setOnClickListener(this);
        textViewForgot.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {

            //email is empty
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            //stopping the function execution further
            return;

        }
        if (TextUtils.isEmpty(password)) {

            //password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
            //stopping the function execution further
            return;

        }
        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()) {

                            //start profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                        } else {

                            Toast.makeText(LoginActivity.this, "Could not SignIn. Try again.", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            userLogin();
        }
        if (v == findViewById(R.id.textViewRegister)){

            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }
        if (v == textViewForgot){

            String email = editTextEmail.getText().toString().trim();

            if(TextUtils.isEmpty(email)) {

                //email is empty
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
                //stopping the function execution further
                return;

            } else {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Do you want to send a password reset email?");
                alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth.sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(LoginActivity.this, "Error sending password reset email", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

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

        }
    }
}
