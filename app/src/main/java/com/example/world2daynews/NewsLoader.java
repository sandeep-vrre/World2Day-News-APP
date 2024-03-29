package com.example.world2daynews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     * @param context of the activity
     * @param url to load data from*/
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url; }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**background thread*/
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform network request, parse and extract a list of news.
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}
