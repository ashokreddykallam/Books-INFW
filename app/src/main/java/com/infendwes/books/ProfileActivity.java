package com.infendwes.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView PName,PBadge,PDescription;
    CircleImageView PProfile;
    ProgressBar PProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        PName = findViewById(R.id.P_name);
        PBadge = findViewById(R.id.P_Badge);
        PProfile = findViewById(R.id.P_Profile);
        PDescription = findViewById(R.id.P_Description);
        PProgress = findViewById(R.id.P_Progress);

        PName.setVisibility(View.INVISIBLE);
        PBadge.setVisibility(View.INVISIBLE);
        PProfile.setVisibility(View.INVISIBLE);
        PDescription.setVisibility(View.INVISIBLE);

        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            PName.setText(document.get("name").toString());
                            String verify = document.get("verify").toString();
                            PDescription.setText(document.get("description").toString());

                            if (document.get("profile").toString().trim().equals("default")){

                            }
                            else{
                                Glide.with(ProfileActivity.this).load(document.get("profile").toString()).into(PProfile);
                            }

                            if (verify.equals("verified")){
                                PBadge.setVisibility(View.VISIBLE);
                            }
                            PProgress.setVisibility(View.INVISIBLE);
                            PName.setVisibility(View.VISIBLE);
                            PBadge.setVisibility(View.VISIBLE);
                            PProfile.setVisibility(View.VISIBLE);
                            PDescription.setVisibility(View.VISIBLE);


                        }else {
                            Toast.makeText(ProfileActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //ini
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //S
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        //Click
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_categories:
                        startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_profile:

                        return true;
                }
                return false;
            }
        });
    }
}
