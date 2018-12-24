package com.mehul.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class OfficialActivity extends AppCompatActivity {
    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWNDATA = "Unknown";
    public static final String DEMOCRAT = "Democratic";
    public static final String DEMOCRAT2 = "Democrat";
    public static final String REPUBLIC = "Republican";

    public TextView locView;
    public TextView offView;
    public TextView nameOfficialView;
    public TextView partyDetailView;
    public ImageView imageDataView;
    public TextView addressDetailView;
    public TextView phoneNumView;
    public TextView emailIdView;
    public TextView websiteDetailView;
    public TextView addressDetailLabel;
    public TextView phoneNumLabel;
    public TextView emailIDLabel;
    public TextView websiteDetailLabel;

    public ImageView youtubeLinkButton;
    public ImageView googleplusLinkButton;
    public ImageView twitterLinkButton;
    public ImageView facebookLinkButton;

    public Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        locView = findViewById(R.id.locationValueID); locView.setTextColor(Color.WHITE);
        offView = findViewById(R.id.officeValID);
        nameOfficialView = findViewById(R.id.nameValID);
        partyDetailView = findViewById(R.id.partyValID);
        imageDataView = findViewById(R.id.imageValID);
        addressDetailView = findViewById(R.id.addressValID);
        phoneNumView = findViewById(R.id.phoneValID);
        emailIdView = findViewById(R.id.emailValID);
        websiteDetailView = findViewById(R.id.websiteValID);
        this.addressDetailLabel = findViewById(R.id.addressValLabel);
        this.phoneNumLabel = findViewById(R.id.phoneValLabel);
        this.emailIDLabel = findViewById(R.id.emailValLabel);
        this.websiteDetailLabel = findViewById(R.id.websiteValLabel);
        addressDetailLabel.setText(("Address:").toString());
        phoneNumLabel.setText(("Phone:").toString());
        emailIDLabel.setText(("Email:").toString());
        websiteDetailLabel.setText(("Website:").toString());
        addressDetailLabel.setTextColor(Color.WHITE);
        phoneNumLabel.setTextColor(Color.WHITE);
        emailIDLabel.setTextColor(Color.WHITE);
        websiteDetailLabel.setTextColor(Color.WHITE);

        this.youtubeLinkButton = findViewById(R.id.youtubeButtonID);
        this.googleplusLinkButton = findViewById(R.id.googleplusButtonID);
        this.twitterLinkButton = findViewById(R.id.twitterButtonID);
        this.facebookLinkButton = findViewById(R.id.facebookButtonID);

        youtubeLinkButton.setImageResource(R.drawable.youtubeicon);
        googleplusLinkButton.setImageResource(R.drawable.googleplusicon);
        twitterLinkButton.setImageResource(R.drawable.twittericon);
        facebookLinkButton.setImageResource(R.drawable.facebookicon);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        official = (Official) bundle.getSerializable("official");
        locView.setText(intent.getStringExtra("header"));
        if( official.getOffice().equals(NO_DATA)){ hideView(offView);}
        else{offView.setText(official.getOffice());offView.setTextColor(Color.WHITE);}
        if( official.getName().equals(NO_DATA)){ hideView(nameOfficialView);}
        else{nameOfficialView.setText(official.getName());nameOfficialView.setTextColor(Color.WHITE);}
        if( official.getParty().equals(UNKNOWNDATA)){ hideView(partyDetailView);}
        else{
            partyDetailView.setText("(" + official.getParty() + ")"); partyDetailView.setTextColor(Color.WHITE);
            if(official.getParty().equals(DEMOCRAT) || official.getParty().equals(DEMOCRAT2)){
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(  R.color.darkBlue));
            }
            if(official.getParty().equals(REPUBLIC)){
                getWindow().getDecorView().setBackgroundColor( getResources().getColor(  R.color.darkRed));
            }
        }

        if(connected()) {


            imageDataView.setImageResource(R.drawable.placeholder);

            if (official.getPhotoUrl().equals(NO_DATA)) {
                imageDataView.setImageResource(R.drawable.missingimage);
            } else {
                final String photoUrl = official.getPhotoUrl();
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String changedUrl = photoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(imageDataView);

                    }
                }).build();

                picasso.load(photoUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(imageDataView);
            }

        } else {
            imageDataView.setImageResource(R.drawable.placeholder);
        }
        addressDetailView.setText(official.getAddress()); addressDetailView.setTextColor(Color.WHITE);
        phoneNumView.setText(official.getPhone()); phoneNumView.setTextColor(Color.WHITE);
        emailIdView.setText(official.getEmail()); emailIdView.setTextColor(Color.WHITE);
        websiteDetailView.setText(official.getUrl()); websiteDetailView.setTextColor(Color.WHITE);
        if(official.getYoutube().equals(NO_DATA)){
            hideView(youtubeLinkButton);
        }
        if(official.getGoogleplus().equals(NO_DATA)){
            hideView(googleplusLinkButton);
        }
        if(official.getTwitter().equals(NO_DATA)){
            hideView(twitterLinkButton);
        }
        if(official.getFacebook().equals(NO_DATA)){
            hideView(facebookLinkButton);
        }
        Linkify.addLinks(addressDetailView,Linkify.MAP_ADDRESSES);
        Linkify.addLinks(phoneNumView,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(emailIdView,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(websiteDetailView,Linkify.WEB_URLS);
    }

    private static void hideView(View v){
        v.setVisibility(View.GONE);
    }


    public void youtubeClicked(View v){
        String name = official.getYoutube();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://www.youtube.com/" + name)));

        }
    }


    public void openPhotoActivity(View v){
        if(official.getPhotoUrl().equals(NO_DATA)){
            return;
        }
        Intent intent = new Intent(OfficialActivity.this, PhotoActivity.class);
        intent.putExtra("header", locView.getText().toString());
        intent.putExtra("office", official.getOffice());
        intent.putExtra("name", official.getName());

        if(official.getParty().equals(DEMOCRAT) || official.getParty().equals(DEMOCRAT2)){
            intent.putExtra("color","blue");
        }
        if(official.getParty().equals(REPUBLIC)){
            intent.putExtra("color","red");
        }
        if(official.getParty().equals(UNKNOWNDATA)){
            intent.putExtra("color", "black");
        }
        intent.putExtra("photoUrl", official.getPhotoUrl());

        startActivity(intent);

    }

    public void googleplusClicked(View v){
        String name = official.getGoogleplus();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,

                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void facebookClicked(View v){
        String urlVal;
        String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebook();


        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlVal = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlVal = "fb://page/" + official.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlVal = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse( urlVal));
        startActivity(facebookIntent);
    }



    public void twitterClicked(View v){
        Intent intent;
        String id = official.getTwitter();
        try {
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + id));
        }
        startActivity(intent);
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


    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}