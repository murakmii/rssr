package net.bonono.rssreader.domain_logic.rss;

import android.text.TextUtils;

import net.bonono.rssreader.domain_logic.xml.XmlDefinition;
import net.bonono.rssreader.domain_logic.xml.XmlResult;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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

    @Override
    public Feed parse(String xml) throws XmlPullParserException, IOException {
        Feed feed = new Feed();

        try (ByteArrayInputStream ba = new ByteArrayInputStream(xml.getBytes())) {
            XmlResult ch = mDef.parse(ba).get("rss").get("channel");

            Site site = new Site();
            site.setTitle(ch.get("title").getText());
            site.setUrl(ch.get("link").getText());
            site.setDescription(ch.get("description").getText());
            feed.setSite(site);

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
        }

        return feed;
    }
}
