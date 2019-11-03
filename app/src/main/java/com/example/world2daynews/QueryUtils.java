package com.example.world2daynews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/** requesting and receiving news data from The Guardian API*/
public final class QueryUtils {

    /** for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**Create a private constructor because no one should ever create a {@link QueryUtils} object.*/
    private QueryUtils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        Log.i(LOG_TAG, "NEWS: the jsonResponse is: " + jsonResponse);
        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}s
        return newsList;
    }

    /**Returns new URL object from the given string URL.*/
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("QueryUtils", "Error parsing JSON date: ", e);
            return "";
        }
    }

    /** Make an HTTP request to the given URL and return a String as the response.*/
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**Convert the {@link InputStream} into a String which contains thewhole JSON response from the server.*/
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**Return a list of {@link News} objects that has been built up from parsing the given JSON response.*/
    private static List<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, return.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // an empty ArrayList that we can start adding news articles to
        List<News> newsList = new ArrayList<>();
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Extract the JSONObject associated with the key called "response"
            JSONObject baseJsonResponseResult = baseJsonResponse.getJSONObject("response");

            //Extract the JSONArray "results" from within the JSONObject baseJSONResponse
            JSONArray newsArray = baseJsonResponseResult.getJSONArray("results");

            // For each article in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news article at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Assign value of the key called "webTitle" to articleTitle
                String articleTitle = currentNews.getString("webTitle");

                // Get JSONArray tags from within results object
                JSONArray tagsArray = currentNews.getJSONArray("tags");

                // Name the first JSONObject currentTags so we can get the string of webTitle key
                JSONObject currentTags = tagsArray.getJSONObject(0);

                //Assign the value of the key called "webTitle" to articleAuthor
                String articleAuthor = currentTags.getString("webTitle");

                // Assign the value of the key called "sectionName" to articleSection
                String articleSection = currentNews.getString("sectionName");

                // Extract value from the key called "webPublicationDate"
                String rawDate = currentNews.getString("webPublicationDate");

                // Format the date
                String articlePublishDate = formatDate(rawDate);

                // Extract the value for the key called "url"
                String articleUrl = currentNews.getString("webUrl");

                // Add a new {@link News} to the list of news articles.
                newsList.add(new News(articleTitle, articleAuthor, articleSection, articlePublishDate, articleUrl));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return newsList;
    }

}
