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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.infendwes.books.MainActivity;
import com.infendwes.books.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText RName,REmail,RPassword;
    Button BSubmit;

     FirebaseAuth mAuth;
     Dialog dialog;

    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BSubmit = findViewById(R.id.reg_submit);
        RName = findViewById(R.id.reg_name);
        REmail = findViewById(R.id.reg_mail);
        RPassword = findViewById(R.id.reg_password);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);

        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);





        BSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RName.length() == 0){
                    RName.setError("Name is Required");

                    return;
                }

                if (REmail.length() == 0){
                    REmail.setError("Email is Required");

                    return;
                }

                if (RPassword.length() == 0){
                    RPassword.setError("Password is Required");

                    return;
                }

                CreateAcc();
            }
        });


    }


    private void CreateAcc(){
        //Create Acc.
        mAuth.createUserWithEmailAndPassword(REmail.getText().toString(), RPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Send
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", RName.getText().toString());
                    user.put("email", REmail.getText().toString());
                    user.put("profile", "default");
                    user.put("verify", "no");
                    user.put("notes", "clean-fresh");

                    db.collection("users").document(mAuth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Welcome!" + RName.getText(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}