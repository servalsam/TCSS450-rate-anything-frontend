package edu.tacoma.uw.group9_450project.rateanything.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Description: HttpJSONTask utilizes a string URL and mapped data to build a JSON object.
 * The JSON object can be sent to any supplied URL in an asynchronous fashion.
 * Author: Sam Wainright, Rich Williams
 * Latest update: 8 Aug 2020
 */
public class HttpJSONTask extends AsyncTask<String, Void, Void> {
    private String url;
    private JSONObject jsonObjSent;
    HttpURLConnection urlConnection = null;

    /**
     *  Constructor handles a string URL and a map containing string-
     *  paired values and creates a HttpJSONTask object.
     *
     * @param url The supplied URL to connect to over the internet.
     * @param data The string-paired data to be sent.
     */
    public HttpJSONTask(String url, Map<String, String> data) {
        this.url = url;
        this.jsonObjSent = new JSONObject(data);
    }

    /**
     *  Overridden function to asynchronously make a connection via a supplied URL
     *  in a POST orientation.
     *
     * @param params The URL objects being supplied to execute a connection over the internet.
     * @return Void return.
     */
    @Override
    protected Void doInBackground(String... params) {

        try {
            // Try to make a connection.
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            // If the data is not a null object, send it!
            if (this.jsonObjSent != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(jsonObjSent.toString());
                writer.flush();

            }

            // Get the status code.
            int statusCode = urlConnection.getResponseCode();

            // Success.
            if (statusCode == 200) {
                Log.v("Status", "Success");
            }

        } catch (Exception conErr) {
            // Failure...
            Log.e("Connection error:", conErr.getMessage());
        }
        return null;
    }
}

