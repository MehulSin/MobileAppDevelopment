package com.example.mehullaptop.convertor;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import java.text.NumberFormat;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.Display;
import  android.content.res.Configuration;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton radioyear2;
    private RadioButton radioyear3;
    private RadioButton radioyear5;
    private Button bttnConvert;
    String convertList;
    String historyVal;
    TextView tvconvertedval;
    TextView t1;

    EditText e1;
    private static final String TAG = "MainActivity";

    //This function is the start point for the app.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Calling on create");
        setContentView(R.layout.activity_main);
        bttnConvert = (Button) findViewById(R.id.buttonConvert);
        t1 = (TextView) findViewById(R.id.textViewHistory);
        tvconvertedval = (TextView) findViewById(R.id.textViewconvertedvalue);
        e1 = (EditText) findViewById(R.id.editText_ftc);
        bttnConvert.setOnClickListener(this);//this is for the click event of convert button
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    //This function is for removing text from editbox while key press
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TextView e1 = (TextView) findViewById(R.id.textViewconvertedvalue);
        e1.setText(null);
        return super.onKeyDown(keyCode, event);
    }

    //This function is for removing text from editbox while key press
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView e1 = (TextView) findViewById(R.id.textViewconvertedvalue);
        e1.setText(null);
        return super.onKeyUp(keyCode, event);
    }


    //This maintains the lifecycle of the android app


    //This function retains data when app is started after any notification or message
    @Override
    protected void onStart() {
        super.onStart();
    }

    //This function retains data when app goes in background after any notification or message
    @Override
    protected void onPause() {
        super.onPause();
    }

    //This function retains data when app is started after any notification or message
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // Restore UI state from the savedInstanceState.
    // This bundle has also been passed to onCreate.

    public void onStartInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
        String historyOnRotation = savedInstanceState.getString("History");
        String converted = savedInstanceState.getString("ConvertedValue");
        t1.setText(historyOnRotation);
        tvconvertedval.setText(converted);
        t1.setMovementMethod(new ScrollingMovementMethod());
    }



    public void onRadioBttnClickHandler(View view) {
        RadioButton rb1 = (RadioButton) findViewById(R.id.radiobttn_ftc);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radiobttn_ctf);
        EditText e1 = (EditText) findViewById(R.id.editText_ftc);
        TextView t1 = (TextView) findViewById(R.id.textViewconvertedvalue);
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {

            case R.id.radiobttn_ftc:
                if (checked)
                    //if farenheit to celsius conversion is to be done
                    //enable textbox 1 and disable textbox 2
                    e1.getText().clear();
                t1.setText("");
                t1.setFocusable(false);

                break;

            case R.id.radiobttn_ctf:
                if (checked)
                    //if celsius to farenheit conversion is to be done
                    //enable textbox 2 and disable textbox 1
                    e1.getText().clear();
                t1.setText("");
                t1.setFocusable(false);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("History",t1.getText().toString());
        outState.putString("ConvertedValue",tvconvertedval.getText().toString());
        super.onSaveInstanceState(outState);
    }

    // Restore UI state from the savedInstanceState.
    // This bundle has also been passed to onCreate.

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
        String historyOnRotation = savedInstanceState.getString("History");
        String Converted = savedInstanceState.getString("ConvertedValue");
        t1.setText(historyOnRotation);
        t1.setMovementMethod(new ScrollingMovementMethod());
        tvconvertedval.setText(Converted);
    }




    public void onClick(View v) {
        String value = null;
        float convertedVal = 0;

        RadioButton rb1 = (RadioButton) findViewById(R.id.radiobttn_ftc);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radiobttn_ctf);



        String historyNullValue = null;
        if(rb1.isChecked()) {
            value = e1.getText().toString();
            if (value == null || value.equals("")){
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Conversion value not found");
                dlgAlert.setTitle("Please enter value for conversion ");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } else {
                convertedVal = (Float.valueOf(value) - 32) * 5 / 9;

                tvconvertedval.setText(String.format("%.1f",(convertedVal)));
                convertList = "F to C :" + value + "->" + String.format("%.1f",convertedVal);
                historyVal = t1.getText().toString();
                t1.setMovementMethod(new ScrollingMovementMethod());
                if(historyVal==null || historyVal.equals(""))
                {
                    historyVal=convertList;
                }
                else {
                    historyVal =   convertList + "\n" + historyVal ;
                }
                t1.setText(historyVal);
            }


        }
        else if(rb2.isChecked())
        {
            value = e1.getText().toString();
            if (value == null || value.equals("")) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Conversion value not found");
                dlgAlert.setTitle("Please enter value for conversion");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } else {

                convertedVal = (((Float.valueOf(value) * 9) / 5) + 32);
                tvconvertedval.setText((String.valueOf(convertedVal)));
                convertList = "C to F :" + value + "->" + convertedVal;
                historyVal = t1.getText().toString();
                t1.setMovementMethod(new ScrollingMovementMethod());
                if(historyVal==null || historyVal.equals(""))
                {
                    historyVal =  convertList ;
                }
                else {
                    historyVal =   convertList + "\n"  + historyVal ;
                }
                t1.setText(historyVal);

            }

        }

        else if(rb1.isChecked()==false && rb2.isChecked()==false){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a conversion method");
            dlgAlert.setTitle("Connversion not found");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

        else
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a conversion method");
            dlgAlert.setTitle("Connversion not found");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }
}
