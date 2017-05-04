package net.bonono.rssreader.lib.rss;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import net.bonono.rssreader.lib.XmlDefinition;
import net.bonono.rssreader.lib.XmlResult;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Rss1Parser implements FeedParser {
    private XmlDefinition mDef;

    public Rss1Parser() {
        XmlDefinition item = XmlDefinition.forTag("item");
        item.nest(XmlDefinition.forText("title"));
        item.nest(XmlDefinition.forText("link"));
        item.nest(XmlDefinition.forText("description"));
        item.nest(XmlDefinition.forText("dc:date"));

        XmlDefinition ch = XmlDefinition.forTag("channel");
        ch.nest(XmlDefinition.forText("title"));
        ch.nest(XmlDefinition.forText("link"));
        ch.nest(XmlDefinition.forText("description"));

        XmlDefinition rdf = XmlDefinition.forTag("rdf:RDF");
        rdf.nest(ch);
        rdf.nest(item);

        mDef = XmlDefinition.rootOf(rdf);
    }

    @Override
    public Feed parse(String xml) throws XmlPullParserException, IOException {
        Feed feed = new Feed();

        try (ByteArrayInputStream ba = new ByteArrayInputStream(xml.getBytes())) {
            XmlResult rss1 = mDef.parse(ba).get("rdf:RDF");

            XmlResult ch = rss1.get("channel");
            feed.setTitle(ch.get("title").getText());
            feed.setUrl(ch.get("link").getText());
            feed.setDescription(ch.get("description").getText());

            for (XmlResult item : rss1.getList("item")) {
                Entry entry = new Entry();
                entry.setTitle(item.get("title").getText());
                entry.setUrl(item.get("link").getText());
                entry.setDescription(item.get("description").getText());

                String dateTime = item.get("dc:date").getText();
                if (!TextUtils.isEmpty(dateTime)) {
                    try {
                        entry.setCreatedAt(LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    } catch (DateTimeParseException e) {
                        // ignore failing to parse
                    }
                }

                feed.addEntry(entry);
            }
        }

        return feed;
    }
}
