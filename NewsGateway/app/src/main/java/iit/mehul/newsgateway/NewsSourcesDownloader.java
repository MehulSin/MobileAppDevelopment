package iit.mehul.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsSourcesDownloader extends AsyncTask<String, Void, String> {

    private MainActivity mainAct;
    private String key = "ec9649a597c74992b17194b8155b2cb2";
    private String url = "https://newsapi.org/v1/sources?";
    private ArrayList<Source> listsource = new ArrayList<>();
    private ArrayList<String> listcategory = new ArrayList<>();
    private String categoryVal;

    public NewsSourcesDownloader (MainActivity ma, String category){
        mainAct = ma;
        if(category.equalsIgnoreCase("all") || category.isEmpty()){
            this.categoryVal = "";
        } else {
            this.categoryVal = category;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        parseJSON(s);
        mainAct.setSources(listsource, listcategory);
    }



    @Override
    protected String doInBackground(String... params) {
        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("language", "en");
        buildURL.appendQueryParameter("country", "us");
        buildURL.appendQueryParameter("apiKey", key);
        buildURL.appendQueryParameter("category", categoryVal);
        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
            }
        return sb.toString();
    }


    private void parseJSON(String s) {
        try {
            JSONObject jObject = new JSONObject(s);
            JSONArray jArrSources = jObject.getJSONArray("sources");
            for (int i = 0; i < jArrSources.length(); i++) {
                Source source = new Source();
                source.setIdVal(jArrSources.getJSONObject(i).getString("id"));
                source.setNameVal(jArrSources.getJSONObject(i).getString("name"));
                source.setUrlVal(jArrSources.getJSONObject(i).getString("url"));
                source.setCategoryData(jArrSources.getJSONObject(i).getString("category"));
                listsource.add(source);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
           // listAddValue();
        for(int i=0; i<listsource.size(); i++){
            if( !listcategory.contains(listsource.get(i).getCategoryData()) ){
                listcategory.add(listsource.get(i).getCategoryData());
            }
        }

    }

    private void listAddValue(){
        for(int i=0; i<listsource.size(); i++){
            if( !listcategory.contains(listsource.get(i).getCategoryData()) ){
                listcategory.add(listsource.get(i).getCategoryData());
            }
        }

    }


}
