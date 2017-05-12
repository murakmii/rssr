package net.bonono.rssreader.domain_logic.rss;

import android.text.TextUtils;

import net.bonono.rssreader.domain_logic.HttpUtil;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import org.jsoup.Jsoup;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class Feed {
    private Site mSite;
    private List<Entry> mEntries;

    public static Observable<Feed> search(String url) {
        return Observable.create(emitter -> {
            try {
                Feed feed = new Feed();
                feed.setSite(new Site());

                searchInternal(url, feed, true);

                if (!feed.isValid()) {
                    throw new IllegalArgumentException("Can't find feed");
                }

                emitter.onNext(feed);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private static void searchInternal(String url, Feed buffer, boolean dig) throws IOException, XmlPullParserException {
        HttpUtil.ResponseWrapper res = HttpUtil.get(url);
        if (res.isHtml()) {

            try (ResponseBody body = res.body()) {
                buffer.getSite().collectUrlFromHtml(Jsoup.parse(body.byteStream(), res.charset(), res.requestedUrl()));
                if (dig && !TextUtils.isEmpty(buffer.getSite().getFeedUrl())) {
                    searchInternal(buffer.getSite().getFeedUrl(), buffer, false);
                }
            }

        } else if (res.isXml()) {

            try (ResponseBody body = res.body()) {
                String bodyString = body.string(); // TODO: don't use string() method
                FeedParser parser = FeedParserFactory.newInstanceFor(bodyString);

                if (parser != null) {
                    Feed feed = parser.parse(bodyString);
                    buffer.getSite().setUrl(feed.getSite().getUrl());
                    buffer.getSite().setTitle(feed.getSite().getTitle());
                    buffer.getSite().setDescription(feed.getSite().getDescription());
                    buffer.mEntries = feed.mEntries;

                    if (dig && !TextUtils.isEmpty(feed.getSite().getUrl())) {
                        searchInternal(feed.getSite().getUrl(), buffer, false);
                    }
                }
            }

        }
    }

    public Feed() {
        mEntries = new ArrayList<>();
    }

    public void setSite(Site site) {
        mSite = site;
    }

    public Site getSite() {
        return mSite;
    }

    public void addEntry(Entry entry) {
        mEntries.add(entry);
    }

    public List<Entry> getEntries() {
        return mEntries;
    }

    public boolean isValid() {
        return mSite != null &&
                !TextUtils.isEmpty(mSite.getTitle()) &&
                !TextUtils.isEmpty(mSite.getUrl()) &&
                !TextUtils.isEmpty(mSite.getFeedUrl());
    }
}
