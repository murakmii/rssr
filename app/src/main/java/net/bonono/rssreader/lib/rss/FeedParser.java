package net.bonono.rssreader.lib.rss;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Feed parser should implements this interface.
 */
public interface FeedParser {
    Feed parse(String xml) throws XmlPullParserException, IOException;
}
