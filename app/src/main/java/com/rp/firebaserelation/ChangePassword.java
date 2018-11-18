package com.rp.firebaserelation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    EditText editTextPassword;
    EditText editTextConfirmPassword;
    FirebaseAuth auth;
    ProgressDialog dialog;
    Button btnChangePass;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChangePass = findViewById(R.id.btnChangePass);
        btnCancel = findViewById(R.id.btnCancel);

        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        btnChangePass.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    public void change(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){

            String pass1 = editTextPassword.getText().toString();
            String pass2 = editTextConfirmPassword.getText().toString();

            if (pass1 == pass2){

                dialog.setMessage("Changing password!");
                dialog.show();

                user.updatePassword(editTextPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            dialog.dismiss();

                            if (task.isSuccessful()){

                                Toast.makeText(getApplicationContext(), "Your password has been changed!", Toast.LENGTH_LONG).show();
                                auth.signOut();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                            } else {

                                Toast.makeText(getApplicationContext(), "Your password could not be changed!", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

            } else {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "The passwords don't match!", Toast.LENGTH_LONG).show();

            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnChangePass){
            change();
        }
        if (v == btnCancel){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

    }
}
