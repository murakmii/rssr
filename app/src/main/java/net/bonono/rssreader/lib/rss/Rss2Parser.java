package net.bonono.rssreader.lib.rss;

import android.text.TextUtils;
import android.util.Xml;

import net.bonono.rssreader.lib.XmlDefinition;
import net.bonono.rssreader.lib.XmlResult;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class Rss2Parser implements FeedParser {
    private XmlDefinition mDef;

    public Rss2Parser() {
        XmlDefinition item = XmlDefinition.forTag("item");
        item.nest(XmlDefinition.forText("title"));
        item.nest(XmlDefinition.forText("link"));
        item.nest(XmlDefinition.forText("description"));
        item.nest(XmlDefinition.forText("dc:date"));
        item.nest(XmlDefinition.forText("pubDate"));

        XmlDefinition ch = XmlDefinition.forTag("channel");
        ch.nest(XmlDefinition.forText("title"));
        ch.nest(XmlDefinition.forText("link"));
        ch.nest(XmlDefinition.forText("description"));
        ch.nest(item);

        XmlDefinition rss = XmlDefinition.forTag("rss");
        rss.nest(ch);

        mDef = XmlDefinition.rootOf(rss);
    }

    public Feed parse(InputStream is) throws XmlPullParserException, IOException {
        XmlResult ch = mDef.parse(is).get("rss").get("channel");

        Feed feed = new Feed();

        feed.setTitle(ch.get("title").getText());
        feed.setUrl(ch.get("link").getText());
        feed.setDescription(ch.get("description").getText());

        for (XmlResult item : ch.getList("item")) {
            Entry entry = new Entry();
            entry.setTitle(item.get("title").getText());
            entry.setUrl(item.get("link").getText());
            entry.setDescription(item.get("description").getText());

            String pubDate = item.get("pubDate").getText(), dcDate = item.get("dc:date").getText();
            if (!TextUtils.isEmpty(pubDate)) {
                try {
                    entry.setCreatedAt(LocalDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME));
                } catch (DateTimeParseException e) {
                    // ignore failing to parse
                }
            } else if (!TextUtils.isEmpty(dcDate)) { // try to parse dc:date if pubDate isn't found
                if (!TextUtils.isEmpty(dcDate)) {
                    try {
                        entry.setCreatedAt(LocalDateTime.parse(dcDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    } catch (DateTimeParseException e) {
                        // ignore failing to parse
                    }
                }
            }

            feed.addEntry(entry);
        }

        return feed;
    }
}
