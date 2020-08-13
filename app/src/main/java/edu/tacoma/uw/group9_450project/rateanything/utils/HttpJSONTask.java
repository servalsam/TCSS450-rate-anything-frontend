package edu.tacoma.uw.group9_450project.rateanything.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpJSONTask extends AsyncTask<String, Void, Void> {
    private String url;
    private JSONObject jsonObjSent;
    HttpURLConnection urlConnection = null;

    public HttpJSONTask(String url, Map<String, String> data) {
        this.url = url;
        this.jsonObjSent = new JSONObject(data);
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");


            if (this.jsonObjSent != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(jsonObjSent.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {
                Log.v("Status", "Success");
            }

        } catch (Exception conErr) {
            Log.e("Connection error:", conErr.getMessage());
        }
        return null;
    }
}
