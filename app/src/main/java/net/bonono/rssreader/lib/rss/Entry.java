package net.bonono.rssreader.lib.rss;

import org.threeten.bp.LocalDateTime;

public class Entry {
    private String mTitle, mUrl, mDescription;
    private LocalDateTime mUpdatedAt, mCreatedAt;

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

    // TODO: need "updatedAt"?

    public LocalDateTime getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        mCreatedAt = createdAt;
    }
}
