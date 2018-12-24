package iit.mehul.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.key;
import static android.R.attr.src;
import android.support.v7.app.AlertDialog;


public class MainActivity extends AppCompatActivity {


    static final String NEWS_STORY_ACTION = "NEWS_STORY_ACTION";
    static final String SOURCE_DATA = "SOURCE_DATA";
    static final String SERVICE_MESSAGE_ACTION = "SERVICE_MESSAGE_ACTION";
    static final String ARTICLE_DATA = "ARTICLE_DATA";

    private DrawerLayout gDrawerLayout;
    private ListView gDrawerList;
    private ActionBarDrawerToggle gDrawerToggle;
    private NewsReceiver newsReceiver;
    private HashMap<String, Source> mapsource = new HashMap<String, Source>();
    private ArrayList<String> sourcedatalist = new ArrayList<>();
    private ArrayList<String> categorylistvalue = null;
    private ArrayList<Article> articlelistdata = new ArrayList<>();
    private Menu menuVal;
    private MyPageAdapter pageAdapterVal;
    private List<Fragment> fragmentData;
    private ViewPager viewPagerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        newsReceiver = new NewsReceiver();
        IntentFilter filterData = new IntentFilter(NEWS_STORY_ACTION);
        registerReceiver(newsReceiver, filterData);

        gDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        gDrawerList = (ListView) findViewById(R.id.left_drawer_ID);
        gDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sourcedatalist));
        onClickData();
        onDrawerToggle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (networkCheck()) {
            if(savedInstanceState!=null){
                setTitle(savedInstanceState.getCharSequence("title"));
                setSources((ArrayList<Source>)savedInstanceState.getSerializable("sourcelist"), savedInstanceState.getStringArrayList("categorylist"));
            } else {
                getNetworkCall();
                }
        } else {
            dialogBuilder();
            }


        fragmentData = getFragments();
        pageAdapterVal = new MyPageAdapter(getSupportFragmentManager());
        viewPagerData = (ViewPager) findViewById(R.id.viewpager);
        viewPagerData.setAdapter(pageAdapterVal);
        viewPagerData.setOffscreenPageLimit(10);
        if (savedInstanceState!=null){
            for(int i =0; i<savedInstanceState.getInt("size");i++) {
                fragmentData.add(getSupportFragmentManager().getFragment(savedInstanceState, "NewsFragment" + Integer.toString(i)));
            }
        } else {
            viewPagerData.setBackgroundResource(R.drawable.img_bgnews);
        }
        pageAdapterVal.notifyDataSetChanged();

    }

    void dialogBuilder()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No network connection");
        builder.setMessage("Please connect to network for loading news");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    void onClickData(){
        gDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );
    }

    void onDrawerToggle(){
        gDrawerToggle = new ActionBarDrawerToggle(
                this,
                gDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

    }

    void getNetworkCall(){
        NewsSourcesDownloader nsdl = new NewsSourcesDownloader(this, "");
        nsdl.execute();
    }

    // Drawer handler
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuVal = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Menu handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (gDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        NewsSourcesDownloader nsdl = new NewsSourcesDownloader(this, ""+item);
        nsdl.execute();
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        viewPagerData.setBackground(null);
        setTitle(sourcedatalist.get(position));
        Intent intent = new Intent(SERVICE_MESSAGE_ACTION);
        intent.putExtra(SOURCE_DATA, mapsource.get(sourcedatalist.get(position)));
        sendBroadcast(intent);
        gDrawerLayout.closeDrawer(gDrawerList);
    }

    // Set the sources and categories after the download
    public void setSources(ArrayList<Source> sourcelist, ArrayList<String> categorylist) {

        clearData();
        Collections.sort(sourcelist);
        for(Source s: sourcelist){
            sourcedatalist.add(s.getNameVal());
            mapsource.put(s.getNameVal(), s);
        }
        ((ArrayAdapter<String>)gDrawerList.getAdapter()).notifyDataSetChanged();

        if (this.categorylistvalue == null){
            this.categorylistvalue = new ArrayList<String> (categorylist);
            if(menuVal!=null) {
                this.categorylistvalue.add(0, "all");
                for (String c : this.categorylistvalue) {
                    menuVal.add(c);
                }
            }
        }

    }

    void clearData(){
        mapsource.clear();
        sourcedatalist.clear();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.categorylistvalue != null) {
            menu.clear();
            for (String c : this.categorylistvalue) {
                menu.add(c);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long bId = 0;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentData.get(position);
        }
        @Override
        public int getCount() {
            return fragmentData.size();
        }
        @Override
        public long getItemId(int position) {
            return bId + position;
        }
        public void notifyChangeInPosition(int n) {
            bId += getCount() + n;
        }

    }

     public class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case NEWS_STORY_ACTION:
                    if (intent.hasExtra(ARTICLE_DATA)) {
                        try {
                            articlelistdata = (ArrayList) intent.getSerializableExtra(ARTICLE_DATA);
                            reDoFragments(articlelistdata);
                        }catch (Exception e){}

                    }
                    break;
            }
        }

        private void reDoFragments(List<Article> allData) {
        if(!networkCheck())
        {
            dialogBuilder();
        }
            for (int i = 0; i < pageAdapterVal.getCount(); i++)
                pageAdapterVal.notifyChangeInPosition(i);

            fragmentData.clear();

            for (int i = 0; i < allData.size(); i++) {
                fragmentData.add(NewsFragment.newInstance(allData.get(i), ""+i, ""+allData.size()));
            }
            dataChanged();

        }
    }

    void dataChanged(){
        pageAdapterVal.notifyDataSetChanged();
        viewPagerData.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        super.onDestroy();
    }

    public boolean networkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        int compteur = 0;
        for(int i =0; i< fragmentData.size();i++) {
            if (fragmentData.get(i).isAdded()) {
                compteur++;
                String temp = "NewsFragment" + Integer.toString(i);
                getSupportFragmentManager()
                        .putFragment(savedInstanceState, temp, fragmentData.get(i));
                Log.d("FRAGMENTS SAVE :", Integer.toString(i));
            }

        }
        savedInstanceState.putInt("size",compteur);

        savedInstanceState.putStringArrayList("categorylist", categorylistvalue );

        ArrayList<Source> temp = new ArrayList<>();
        for (String key : mapsource.keySet()) {
           temp.add(mapsource.get(key));
        }
        savedInstanceState.putSerializable("sourcelist", temp );

        savedInstanceState.putCharSequence("title", getTitle());
    }

}
