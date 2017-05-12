package net.bonono.rssreader.entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Site extends RealmObject implements Identifiable {
    @PrimaryKey
    private long id;

    @Required
    @Index
    private String url;

    @Required
    private String title;

    private String description;

    private String iconUrl;

    private String thumbnailUrl;

    private String feedUrl;

    private int unreadCount;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void collectUrlFromHtml(Document doc) {
        for (Element link : doc.select("link[rel='shortcut icon']")) {
            iconUrl = link.attr("abs:href");
        }

        for (Element ogImage : doc.select("meta[property='og:image']")) {
            thumbnailUrl = ogImage.attr("content");
        }

        List<Element> feedUrls = new ArrayList<>();
        for (Element link : doc.select("link[rel=alternate]")) {
            if (link.hasAttr("type") && link.attr("type").contains("xml")) {
                feedUrls.add(link);
            }
        }

        Collections.sort(feedUrls, (lhs, rhs) -> {
            if (lhs.attr("type").contains("atom")) {
                return -1;
            } else if (rhs.attr("type").contains("atom")) {
                return 1;
            } else {
                return 0;
            }
        });

        if (feedUrls.size() > 0) {
            feedUrl = feedUrls.get(0).attr("abs:href");
        }
    }
}
