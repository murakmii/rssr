package net.bonono.rssreader.entity;

import android.icu.util.TimeZone;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        if (createdAt == null) {
            return null;
        } else {
            return LocalDateTime.ofEpochSecond(createdAt.getTime() / 1000, 0, ZoneOffset.UTC);
        }
    }

    public void setCreatedAt(@Nullable LocalDateTime createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = new Date(createdAt.toEpochSecond(ZoneOffset.UTC) * 1000);
        }
    }

    public boolean hasRead() {
        return read;
    }
}
