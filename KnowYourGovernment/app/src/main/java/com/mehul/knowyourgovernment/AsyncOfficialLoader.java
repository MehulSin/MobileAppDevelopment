package com.mehul.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncOfficialLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActVal;

    private static final String KEY = "AIzaSyA97MGGagDnaQ5jqgl0xZSIX-p65wetMQo";
    private final String dataURLVal = "https://www.googleapis.com/civicinfo/v2/representatives?key="+ KEY +"&address=";
    public static final String DATA_UNAVAILABLE = "No Data Provided";
    public static final String UNKNOWNVALUE = "Unknown";

    private String cityname;
    private String statename;
    private String zipcode;

    public AsyncOfficialLoader(MainActivity ma){ mainActVal = ma;}

    @Override
    protected void onPostExecute(String sVal){
        if(sVal == null){
            Toast.makeText(mainActVal,"Civic Info servic is not available for given location",Toast.LENGTH_SHORT).show();
            mainActVal.setOfficialList(null);
            return;
        }
        if(sVal.isEmpty()){
            Toast.makeText(mainActVal,"Data for given location is not available for the specified location",Toast.LENGTH_SHORT).show();
            mainActVal.setOfficialList(null);
            return;
        }
        ArrayList<Official> officialList = parseJSON(sVal);
        Object [] results = new Object[2];
        if(cityname.equals("")){
            results[0] = statename + " " + zipcode;
        }
        else{
        results[0] = cityname + ", " + statename + " " + zipcode;}
        results[1] = officialList;
        mainActVal.setOfficialList(results);
        return;

    }

       @Override
    protected String doInBackground(String... params){

        String dataURL = dataURLVal + params[0];
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();
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
         }
      catch (Exception e) {
            return null;
        }
     return sb.toString();
    }

    private ArrayList<Official> parseJSON(String sVal){
        ArrayList<Official> officialList = new ArrayList<>();
        try{
            JSONObject allData = new JSONObject(sVal);
            JSONObject normalizedInputValue = allData.getJSONObject("normalizedInput");
            JSONArray offices = allData.getJSONArray("offices");
            JSONArray officials = allData.getJSONArray("officials");
            cityname = normalizedInputValue.getString("city");
            statename = normalizedInputValue.getString("state");
            zipcode = normalizedInputValue.getString("zip");
            for(int m = 0;m < offices.length(); m++){
                JSONObject objVal = offices.getJSONObject(m);
                String officeName = objVal.getString("name");
                String officialIndices = objVal.getString("officialIndices");
                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] temp2 = temp.split(",");
                int [] indices = new int [temp2.length];
                for(int j = 0; j < temp2.length; j++){
                    indices[j] = Integer.parseInt(temp2[j]);
                }
                for(int j = 0; j < indices.length; j++ ){
                    JSONObject innerObj = officials.getJSONObject(indices[j]);
                    String name = innerObj.getString("name");
                    String address = "";
                    if(! innerObj.has("address")){
                        address = DATA_UNAVAILABLE;
                    }
                    else {
                        JSONArray addressArray = innerObj.getJSONArray("address");
                        JSONObject addressObject = addressArray.getJSONObject(0);

                        if (addressObject.has("line1")) {
                            address += addressObject.getString("line1") + "\n";
                        }
                        if (addressObject.has("line2")) {
                            address += addressObject.getString("line2") + "\n";
                        }
                        if (addressObject.has("city")) {
                            address += addressObject.getString("city") + " ";
                        }
                        if (addressObject.has("state")) {
                            address += addressObject.getString("state") + ", ";
                       }
                        if (addressObject.has("zip")) {
                            address += addressObject.getString("zip");
                        }
                    }
                    String party = (innerObj.has("party") ? innerObj.getString("party") : UNKNOWNVALUE );
                    String phones = ( innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : DATA_UNAVAILABLE );
                    String urls = ( innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0) : DATA_UNAVAILABLE );
                    String emails = (innerObj.has("emails") ? innerObj.getJSONArray("emails").getString(0) : DATA_UNAVAILABLE );
                    String photoURL = (innerObj.has("photoUrl") ? innerObj.getString("photoUrl") : DATA_UNAVAILABLE);
                    JSONArray channels = ( innerObj.has("channels") ? innerObj.getJSONArray("channels") : null );
                    String googleplus = ""; String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");
                            switch (type){
                                case "GooglePlus":
                                    googleplus = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Facebook":
                                    facebook = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitter = channels.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtube = channels.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    else{
                        googleplus = DATA_UNAVAILABLE;
                        facebook = DATA_UNAVAILABLE;
                        twitter = DATA_UNAVAILABLE;
                        youtube = DATA_UNAVAILABLE;
                    }
                    Official o = new Official(name, officeName, party,
                            address, phones, urls, emails, photoURL,
                            googleplus, facebook, twitter, youtube);
                    officialList.add(o);
                }
            }

            return officialList;
            }
            catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}