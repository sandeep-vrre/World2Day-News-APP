package com.example.world2daynews;

public class News {

    /** news articles title, followed by author, section, date of publish and url*/
    private String mArticleTitle;
    private String mArticleAuthor;
    private String mArticleSection;
    private String mArticlePublishDate;
    private String mArticleUrl;

    /**Constructs a new {@link News} object.*/

    public News(String articleTitle, String articleAuthor, String articleSection, String articlePublishDate, String articleUrl) {
        mArticleTitle = articleTitle;
        mArticleAuthor = articleAuthor;
        mArticleSection = articleSection;
        mArticlePublishDate = articlePublishDate;
        mArticleUrl = articleUrl; }

    // Returns the title, author, section, date of publish and url of news articles.
    public String getArticleTitle() {
        return mArticleTitle;
    }
    public String getArticleAuthor() { return mArticleAuthor; }
    public String getArticleSection() {
        return mArticleSection;
    }
    public String getArticlePublishDate() {
        return mArticlePublishDate;
    }
    public String getArticleUrl() {
        return mArticleUrl;
    }
}
