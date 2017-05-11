package net.bonono.rssreader.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Entry extends RealmObject implements Identifiable {
    @PrimaryKey
    private long id;

    @Index
    private long siteId;

    @Required
    @Index
    private String url;

    @Required
    private String title;

    private String description;

    private Date createdAt;

    private boolean read;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public long getSiteId() {
        return siteId;
    }

    public void belongTo(Site site) {
        siteId = site.getId();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
