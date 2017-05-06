package net.bonono.rssreader.lib;

import android.support.annotation.VisibleForTesting;

import net.bonono.rssreader.lib.rss.Feed;
import net.bonono.rssreader.lib.rss.FeedParser;
import net.bonono.rssreader.lib.rss.FeedParserFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class WebResource {
    private String mImageUrl;
    private List<String> mFeedUrls = new ArrayList<>();
    private List<Feed> mFeeds = new ArrayList<>();

    public static Observable<WebResource> explore(String startAt) {
        return Observable.create(emitter -> {
            WebResource webRes = new WebResource();
            try {
                HttpUtil.ResponseWrapper res = HttpUtil.get(startAt);
                if (res.isHtml()) {
                    webRes.collectDataFromHtml(res, true);
                } else if (res.isXml()) {
                    webRes.collectDataFromXml(res, true);
                }

                if (webRes.mFeeds.size() == 0) {
                    throw new IllegalArgumentException("Can't find feed");
                }

                emitter.onNext(webRes);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public WebResource() { }

    public String getImageUrl() {
        return mImageUrl;
    }

    public List<Feed> getFeeds() {
        return mFeeds;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void collectDataFromHtml(HttpUtil.ResponseWrapper response, boolean collectXml)
            throws IOException, XmlPullParserException {
        if (!response.isHtml()) {
            return;
        }

        try (ResponseBody body = response.body()) {
            Document doc = Jsoup.parse(body.byteStream(), response.charset(), response.requestedUrl());

            // find favicon
            for (Element link : doc.select("link[rel='shortcut icon']")) {
                mImageUrl = link.attr("abs:href");
            }

            if (collectXml) {
                for (Element link : doc.select("link[rel=alternate]")) {
                    collectDataFromXml(HttpUtil.get(link.attr("abs:href")), false);
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void collectDataFromXml(HttpUtil.ResponseWrapper response, boolean collectHtml)
            throws IOException, XmlPullParserException {
        if (!response.isXml()) {
            return;
        }

        try (ResponseBody body = response.body()) {
            String bodyString = body.string(); // TODO: don't use string() method
            FeedParser parser = FeedParserFactory.newInstanceFor(bodyString);

            if (parser != null) {
                Feed feed = parser.parse(bodyString);
                if (!feed.isValid()) {
                    return;
                }

                if (collectHtml) {
                    collectDataFromHtml(HttpUtil.get(feed.getUrl()), false);
                }

                mFeedUrls.add(response.requestedUrl());
                mFeeds.add(feed);
            }
        }
    }
}
