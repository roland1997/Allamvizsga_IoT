package com.example.iotvilagitasrendszer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView Lux,text1;
    private SeekBar Reference;
    private ImageView RGBColor;
    private Button btnCLS, btn;
    View mColorView;
    Bitmap bitmap;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Integer r,g,b;
    String [] listItems={"1","2","3","4","5"};
    boolean [] checkedItems;
    ArrayList<Integer> mUserSelected = new ArrayList<>();
    Intent intent;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, Settings.class);
        initialization();
        getLux();
        seekbar();

        rgbPicker();
        getNames();
        chooseLedStrip();

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                startActivity(intent);
           }
       });





    }

    private void initialization (){

        Lux =(TextView) findViewById(R.id.lux);
        text1 =(TextView) findViewById(R.id.textView);
        Reference = (SeekBar) findViewById(R.id.reference);
        RGBColor = (ImageView) findViewById(R.id.RGB_color);
        mColorView = (View) findViewById(R.id.colorView);
        btnCLS = (Button) findViewById(R.id.btnChLS);
        btn = (Button) findViewById(R.id.button2);




    }

    private void chooseLedStrip(){
        checkedItems = new boolean[listItems.length];
        btnCLS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Led Strips");
                if (!listItems.equals(null) ) {
                    mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                            if (isChecked) {
                                mUserSelected.add(position);

                            } else {
                                mUserSelected.remove(Integer.valueOf(position));
                            }
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton(getString(R.string.ok_label), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String item = "";
                            for (int i = 0; i < mUserSelected.size(); i++) {
                                item = item + listItems[mUserSelected.get(i)];
                                if (i != mUserSelected.size() - 1) {
                                    item = item + ", ";
                                }
                            }
                            text1.setText(item);

                        }

                    });

                    mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    mBuilder.setNeutralButton(getString(R.string.clear_all_label), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < checkedItems.length; i++) {
                                checkedItems[i] = false;
                                mUserSelected.clear();
                                text1.setText("");

                            }

                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            }
        });
        for (int i =0; i<checkedItems.length; i++) {
            Log.d("kijeloltek" + i, String.valueOf(checkedItems[i]));
        }
    }

    private void getNames(){

        myRef = FirebaseDatabase.getInstance().getReference("RGB");
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                int j=0;
                for(DataSnapshot i: dataSnapshot.getChildren()){
                    for(DataSnapshot k : i.getChildren()){
                        listItems[j] = k.getKey();
                        j++;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
    private void getLux(){
        // Read from the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Intensity");
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long intensity =(Long) dataSnapshot.getValue();
                Lux.setText(String.valueOf("Lux: "+ intensity));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void seekbar(){
        Reference.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Reference");


                myRef.setValue((progress*4095)/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void rgbPicker(){
        RGBColor.setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            RGBColor.buildDrawingCache(true);
        }


        RGBColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    bitmap = RGBColor.getDrawingCache();

                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    //getting RGB values'
                     r = Color.red(pixel);
                     g = Color.green(pixel);
                     b = Color.blue(pixel);

                    // set background color of view according to the picked color
                    // mColorView.setBackgroundColor(Color.rgb(r,g,b));
                    mColorView.setBackgroundColor(Color.rgb(r,g,b));

                }
                return true;
            }
        });
    }

    private void setRGB (String selected){


        myRef = FirebaseDatabase.getInstance().getReference("RGB");
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot i: dataSnapshot.getChildren()){
                    for(DataSnapshot j : i.getChildren()){

                        for(DataSnapshot k : j.getChildren()){
                            if(k.getKey().equals("B")){
                                myRef.child("RGB").child(i.getKey()).child(j.getKey()).child(k.getKey()).setValue(b);
                                myRef.child("RGB").child(i.getKey()).child(j.getKey()).child(k.getKey()).setValue(r);
                                myRef.child("RGB").child(i.getKey()).child(j.getKey()).child(k.getKey()).setValue(g);

                            }
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

}
