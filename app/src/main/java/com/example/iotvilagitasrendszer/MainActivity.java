package com.example.iotvilagitasrendszer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView Lux;
    private SeekBar Reference;
    private ProgressBar progressBar;
    private ImageView RGBColor;
    View mColorView;
    Bitmap bitmap;
    private DatabaseReference myRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        RGBColor.setDrawingCacheEnabled(true);
        RGBColor.buildDrawingCache(true);


        RGBColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    bitmap = RGBColor.getDrawingCache();

                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    //getting RGB values'
                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    // set background color of view according to the picked color
                   // mColorView.setBackgroundColor(Color.rgb(r,g,b));
                    mColorView.setBackgroundColor(Color.rgb(r,g,b));
                    Lux.setText("RGB: "+r +", "+ g +", "+ b);
                }
                return true;
            }
        });
        Reference.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressBar.setProgress(progress);

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

    private void initialization (){

        Lux =(TextView) findViewById(R.id.lux);
        Reference = (SeekBar) findViewById(R.id.reference);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RGBColor = (ImageView) findViewById(R.id.RGB_color);
        mColorView = (View) findViewById(R.id.colorView);

    }


}
