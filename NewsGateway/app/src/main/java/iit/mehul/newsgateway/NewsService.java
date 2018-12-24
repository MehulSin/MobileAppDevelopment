package iit.mehul.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import java.io.Serializable;
import java.util.ArrayList;

import static iit.mehul.newsgateway.MainActivity.SERVICE_MESSAGE_ACTION;
import static iit.mehul.newsgateway.MainActivity.NEWS_STORY_ACTION;
import static iit.mehul.newsgateway.MainActivity.ARTICLE_DATA;
import static iit.mehul.newsgateway.MainActivity.SOURCE_DATA;

public class NewsService extends Service {

    private boolean running = true;
    private ServiceReceiver serviceReceiver;
    private ArrayList<Article> articleslist = new ArrayList<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(SERVICE_MESSAGE_ACTION);
        registerReceiver(serviceReceiver, filter1);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(serviceReceiver);
        running = false;
        super.onDestroy();
    }

    public void setArticles(ArrayList<Article> list) {
        articleslist.clear();
        articleslist = new ArrayList<>(list);
        sendIntent();
    }

    private void sendIntent(){
        Intent intent = new Intent(NEWS_STORY_ACTION);
        intent.putExtra(ARTICLE_DATA, (Serializable) articleslist);
        sendBroadcast(intent);
        articleslist.clear();
    }

    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case SERVICE_MESSAGE_ACTION:
                    if (intent.hasExtra(SOURCE_DATA)) {
                        Source source = (Source) intent.getSerializableExtra(SOURCE_DATA);
                        NewsArticleDownloader nadl = new NewsArticleDownloader(NewsService.this, "" + source.getIdVal());
                        nadl.execute();
                    }
                    break;
            }
        }
    }

}
