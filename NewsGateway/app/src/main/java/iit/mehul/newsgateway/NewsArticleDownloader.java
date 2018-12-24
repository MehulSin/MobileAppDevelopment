package iit.mehul.newsgateway;
import org.json.JSONArray;
import org.json.JSONObject;
import android.net.Uri;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewsArticleDownloader extends AsyncTask<String, Void, String> {

    private NewsService newsService;
    private String keyVal = "ec9649a597c74992b17194b8155b2cb2";
    private String urlVal = "https://newsapi.org/v1/articles?";
    private ArrayList<Article> listArticleData = new ArrayList<>();
    private String sourceVal;

    public NewsArticleDownloader (NewsService newsServiceData, String sourceData){
        this.newsService = newsServiceData;
        this.sourceVal = sourceData;
    }

    @Override
    protected void onPostExecute(String resultVal) {
        parseJSON(resultVal);
        newsService.setArticles(listArticleData);
    }


    @Override
    protected String doInBackground(String[] params) {

        Uri.Builder buildURL = Uri.parse(urlVal).buildUpon();
        buildURL.appendQueryParameter("apiKey", keyVal);
        buildURL.appendQueryParameter("source", sourceVal);
        String urlToUse = buildURL.build().toString();
        String nullVal = null;

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            InputStream is = connect.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String lineVal;
            while ((lineVal = reader.readLine()) != null) {
                sb.append(lineVal).append('\n');
            }

        } catch (Exception e) {
            return nullVal;
        }
        return sb.toString();
    }


    private void parseJSON(String s) {
        try {
            JSONObject jObj = new JSONObject(s);
            JSONArray jArrayVal = jObj.getJSONArray("articles");
            for (int i = 0; i < jArrayVal.length(); i++) {
                Article articleData = new Article();
                articleData.setAuthorName(jArrayVal.getJSONObject(i).getString("author").replace("null",""));
                articleData.setTitleName(jArrayVal.getJSONObject(i).getString("title").replace("null",""));
                articleData.setApiAddressDetail(jArrayVal.getJSONObject(i).getString("url"));
                articleData.setUrlImage(jArrayVal.getJSONObject(i).getString("urlToImage"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                articleData.setDateTimeValue(jArrayVal.getJSONObject(i).getString("publishedAt"));
                articleData.setNewsDescData(jArrayVal.getJSONObject(i).getString("description"));
                listArticleData.add(articleData);
            }

        } catch (Exception e) {

        }
    }
}
