package com.infendwes.books.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.infendwes.books.MainActivity;
import com.infendwes.books.R;

public class LoginActivity extends AppCompatActivity {

    Button LSubmit;
    TextView LReg;
    EditText LEmail,LPassword;
    Dialog dialog;

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LReg = findViewById(R.id.log_reg);
        LSubmit = findViewById(R.id.log_submit);
        LEmail = findViewById(R.id.log_mail);
        LPassword = findViewById(R.id.log_password);
        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);

        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        LSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create();

                if (LEmail.length() == 0){
                    LEmail.setError("Email is Required");
                    dialog.dismiss();
                    return;
                }

                if (LPassword.length() == 0){
                    LPassword.setError("Password is Required");
                    dialog.dismiss();
                    return;
                }

                LoginAcc();
            }
        });
    }

    private void LoginAcc(){
        mAuth.signInWithEmailAndPassword(LEmail.getText().toString(), LPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Welcome "+ mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}