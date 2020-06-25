package com.example.iotvilagitasrendszer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    private EditText LS1,LS2,LS3,LS4,LS5;
    private Button Change;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String s1,s2,s3,s4,s5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialization();
        getPrefernces();

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("RGB").removeValue();

                s1 = LS1.getText().toString();
                s2 = LS2.getText().toString();
                s3 = LS3.getText().toString();
                s4 = LS4.getText().toString();
                s5 = LS5.getText().toString();
                Log.d("s1",s1);
                Log.d("s1",s2);
                Log.d("s1",s3);
                Log.d("s1",s4);
                Log.d("s1",s5);
                saveElement();



                setDatabaseStripNames("1",s1);
                setDatabaseStripNames("2",s2);
               setDatabaseStripNames("3",s3);
               setDatabaseStripNames("4",s4);
                setDatabaseStripNames("5",s5);

                startActivity(intent);
            }
        });
    }
    private void setDatabaseStripNames(String  nr, String name){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("RGB").child(nr).child(name).child("R").setValue(0);
        myRef.child("RGB").child(nr).child(name).child("G").setValue(0);
        myRef.child("RGB").child(nr).child(name).child("B").setValue(0);
    }

    private void initialization(){
        LS1 = findViewById(R.id.ls1);
        LS2 = findViewById(R.id.ls2);
        LS3 = findViewById(R.id.ls3);
        LS4 = findViewById(R.id.ls4);
        LS5 = findViewById(R.id.ls5);
        Change = findViewById(R.id.btnChange);
    }

    private void getPrefernces(){
        SharedPreferences sp =this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if(sp.contains("s1")){
            String e = sp.getString("s1","not found");
            LS1.setText(e);
        }
        if(sp.contains("s2")){
            String e = sp.getString("s2","not found");
            LS2.setText(e);
        }
        if(sp.contains("s3")){
            String e = sp.getString("s3","not found");
            LS3.setText(e);
        }
        if(sp.contains("s4")){
            String e = sp.getString("s4","not found");
            LS4.setText(e);
        }
        if(sp.contains("s5")){
            String e = sp.getString("s5","not found");
            LS5.setText(e);
        }


    }

    private void saveElement(){
        SharedPreferences sp =this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("s1",s1);
        editor.putString("s2",s2);
        editor.putString("s3",s3);
        editor.putString("s4",s4);
        editor.putString("s5",s5);
        editor.apply();

    }

}
