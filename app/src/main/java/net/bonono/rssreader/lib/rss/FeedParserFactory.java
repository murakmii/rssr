package net.bonono.rssreader.lib.rss;

import android.support.annotation.Nullable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FeedParserFactory {
    @Nullable
    public static FeedParser newInstanceFor(String s) throws XmlPullParserException, IOException {
        FeedParser fp = null;

        try(ByteArrayInputStream ba = new ByteArrayInputStream(s.getBytes())) {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(ba, null);

            // consume until first tag
            while (parser.getEventType() != XmlPullParser.START_TAG &&
                    parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                parser.next();
            }

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                switch (parser.getName()) {
                    case "rdf:RDF":
                        fp = new Rss1Parser();
                        break;
                    case "rss":
                        fp = new Rss2Parser();
                        break;
                    case "feed":
                        fp = new AtomParser();
                        break;
                }
            }
        }

        return fp;
    }
}
