package net.bonono.rssreader.lib.rss;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private String mTitle, mUrl, mDescription;
    private List<Entry> mEntries;

    public Feed() {
        mEntries = new ArrayList<>();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<Entry> getEntries() {
        return mEntries;
    }

    public void addEntry(Entry entry) {
        mEntries.add(entry);
    }
}
