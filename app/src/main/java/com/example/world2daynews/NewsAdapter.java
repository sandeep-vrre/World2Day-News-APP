package com.example.world2daynews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of news articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the article*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        // Find the news article in the list of news
        News currentNews = getItem(position);

        // Find the TextView with view article_title ID
        TextView articleTitleView = listItemView.findViewById(R.id.article_title);

        // Display the title of the current article
        articleTitleView.setText(currentNews.getArticleTitle());

        // Find the TextView with the view article_author ID
        TextView articleAuthorView = listItemView.findViewById(R.id.article_author);

        // Display the author name of the current article
        articleAuthorView.setText(currentNews.getArticleAuthor());

        // Find the TextView with view ID article_section
        TextView articleSectionView = listItemView.findViewById(R.id.article_section);

        // Display the section of the current article in that TextView
        articleSectionView.setText(currentNews.getArticleSection());

        // Find the TextView with view ID article_publish_date
        TextView articlePublishDateView = listItemView.findViewById(R.id.article_publish_date);

        // Display the date of the current article in that TextView
        articlePublishDateView.setText(currentNews.getArticlePublishDate());

        // Return the list item view
        return listItemView;
    }

}
