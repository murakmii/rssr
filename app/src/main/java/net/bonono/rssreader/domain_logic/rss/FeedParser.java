package net.bonono.rssreader.domain_logic.rss;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
/**
 * Feed parser should implements this interface.
 */
public interface FeedParser {
    Feed parse(String xml) throws XmlPullParserException, IOException;
}
