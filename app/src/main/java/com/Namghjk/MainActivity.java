package com.Namghjk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String STATE_COLORS = "COLORS";
    static final int RECTANGLE_QTD = 4;
    SeekBar mSeekBar;
    private View[] mRectangles =  new View[RECTANGLE_QTD];
    private RandomColor[] mColors = new RandomColor[RECTANGLE_QTD];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setClick();

        mSeekBar = (SeekBar) findViewById(R.id.mSeekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setRectanglesColors(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // goi id cac hinh chu nhat
        mRectangles[0] = findViewById(R.id.layout1);
        mRectangles[1] = findViewById(R.id.layout2);
        mRectangles[2] = findViewById(R.id.layout3);
        mRectangles[3] = findViewById(R.id.layout4);

        // Check xem co tao lai 1 kieu mau da bo trc do k
        if (savedInstanceState != null) {
            //khoi phuc lai mau tu trang thai da save
            ArrayList<String> colorsFromSave = savedInstanceState.getStringArrayList(STATE_COLORS);
            for (int i = 0; i < RECTANGLE_QTD; i++) {
                mColors[i] = new RandomColor(this, colorsFromSave.get(i));
            }
        } else {
            // tao mau cho moi hinh
            restartColors();
        }

        // initial color (mau ban dau)
        setRectanglesColors(mSeekBar.getProgress());


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save colors
        ArrayList<String> colorsToSave = new ArrayList<String>();
        for(int i=0; i<RECTANGLE_QTD; i++) {
            colorsToSave.add(mColors[i].serialized());
        }
        savedInstanceState.putStringArrayList(STATE_COLORS, colorsToSave);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setClick(){
        View layoutMaster = findViewById(R.id.lauoutMaster);
        layoutMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartColors();

            }
        });
    }

    private void setRectanglesColors(int step){
        for(int i = 0; i<RECTANGLE_QTD; i++){
            mRectangles[i].setBackgroundColor(mColors[i].actualColor(step));
        }
    }

    private void restartColors(){
        for (int i= 0; i< RECTANGLE_QTD; i++){
            mColors[i] =  new RandomColor(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Settings_Menu){
            DialogInterface.OnClickListener  onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if ( i==DialogInterface.BUTTON_POSITIVE ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(getString(R.string.moma_site)));
                        startActivity(intent);

                    }
                }
            };

            AlertDialog.Builder builder =  new AlertDialog.Builder(this);
            TextView message = new TextView(this);
            message.setText(R.string.dialog_message);
            message.setGravity(Gravity.CENTER);
            builder.setTitle("Informations");
            builder.setIcon(R.drawable.ic_information);
            builder.setView(message);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.visit_moma, onClickListener);
            builder.setNegativeButton(R.string.not_now, onClickListener);
            AlertDialog dialog = builder.show();

        }
        return super.onOptionsItemSelected(item);
    }


}



