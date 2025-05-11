package com.example.gameproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainMenu extends AppCompatActivity {

    TextView xp_text,user_text;
    String username = "o";int xp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth=FirebaseAuth.getInstance();

        xp_text = findViewById(R.id.txt_xp);
        user_text = findViewById(R.id.main_menu_user);

        getUserData();

    }

    public void OnGameButtonClick(View view){
            Intent intent;
            if(view.getId()==R.id.button_directions_game){
                intent = new Intent(this,DirectionsGame.class);
            }else if(view.getId()==R.id.button_clock_game){
                intent = new Intent(this,ClockGame.class);
            }else if(view.getId()==R.id.button_days_game){
                intent = new Intent(this,DaysGame.class);
            }else if(view.getId()==R.id.button_spelling_game){
                intent=new Intent(this,spellingGame.class);
            }else if(view.getId()==R.id.button_similar_game){
                intent=new Intent(this,similarPicGame.class);
            }else if(view.getId()==R.id.button_months_game){
                intent=new Intent(this,montsoftheyear1.class);
            }else if(view.getId()==R.id.button_multiplication_game){
                intent=new Intent(this,multiplication.class);
            }else if(view.getId()==R.id.button_seasons_game){
                intent=new Intent(this,seasons.class);
            }else if(view.getId()==R.id.button_remember_game){
                intent=new Intent(this,RememberDigitsGame.class);
            }else{
                intent=new Intent(this, RememberBackwardsGame.class);
            }
            startActivity(intent);
    }


    @Override
    public void onBackPressed() {

    }
    private void getUserData(){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("userData").document(auth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d("db","fetched username : " + documentSnapshot.getString("username"));
                    setTexts(documentSnapshot.getString("username"), documentSnapshot.getLong("xp").intValue() );
                }else{
                    Log.d("db","user could not find");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db","fetch failure");
            }
        });
    }
    private void setTexts(String tmpUsername, int tmpXP){
        username = tmpUsername;
        xp = tmpXP;
        user_text.setText("Hello "+ username);

        getXP();
    }
    private void getXP(){
        Intent intent = getIntent();
        int gainedXP = intent.getIntExtra("xp",0);
        xp += gainedXP;

        xp_text.setText(xp + " XP");

        saveData();
    }
    private void saveData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> data = new HashMap<>();
        data.put("username",username);
        data.put("xp",xp);

        db.collection("userData").document(auth.getCurrentUser().getUid()).update(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db","data saving has failed");
            }
        });
    }
}