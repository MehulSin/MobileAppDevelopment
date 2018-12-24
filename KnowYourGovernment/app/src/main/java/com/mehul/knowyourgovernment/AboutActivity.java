package com.mehul.knowyourgovernment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class AboutActivity extends AppCompatActivity {

    private TextView title;
    private TextView copyright;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        title= findViewById(R.id.abouttitleID);
        copyright = findViewById(R.id.copyrightValueID);
        version = findViewById(R.id.versionValueID);

        title.setText("Know Your Government");
        copyright.setText("© 2018, Mehul Singh");
        version.setText("Version 1.0");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}
