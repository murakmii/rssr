package net.bonono.rssreader.lib.rss;

import android.support.annotation.Nullable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class FeedParserFactory {
    @Nullable
    public static FeedParser newInstanceFor(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);

        // consume until first tag
        while(parser.getEventType() != XmlPullParser.START_TAG &&
                parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            parser.next();
        }

        FeedParser fp = null;
        if (parser.getEventType() == XmlPullParser.START_TAG) {
            switch (parser.getName()) {
                case "rdf:RDF":
                    fp = new Rss1Parser();
                    break;
                case "rss":
                    fp = new Rss2Parser();
                    break;
            }
        }

        if (is.markSupported()) {
            is.reset();
        }

        return fp;
    }
}
