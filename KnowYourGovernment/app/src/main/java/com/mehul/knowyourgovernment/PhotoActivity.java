package com.mehul.knowyourgovernment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    public TextView officeVal;
    public TextView nameVal;
    public TextView locationVal;
    public ImageView imageVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.activity_photo);

        locationVal = findViewById(R.id.locationValueID);
        officeVal = findViewById(R.id.officeValID);
        nameVal = findViewById(R.id.nameValID);
        imageVal = findViewById(R.id.imageValID);
        Intent intent = this.getIntent();
        String header = intent.getStringExtra("header");
        locationVal.setText(header.toString());
        officeVal.setText(intent.getStringExtra("office"));
        nameVal.setText(intent.getStringExtra("name"));
        String color = intent.getStringExtra("color");
        if (color.equals("red")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkRed));
        }
        if (color.equals("blue")) {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.darkBlue));
        }
        if(color.equals("black")){
        }

        if(connected()) {
            final String photoUrl = intent.getStringExtra("photoUrl");
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(imageVal);

                }
            }).build();

            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(imageVal);
        } else{
            imageVal.setImageResource(R.drawable.placeholder);
        }

    }


    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.backpress,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}