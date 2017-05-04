package net.bonono.rssreader.lib.rss;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class FeedParserFactoryTest {
    @Test
    public void returnNullForInvalidXml() throws Exception {
        assertThat(FeedParserFactory.newInstanceFor("<test />"), is(nullValue()));
    }

    @Test
    public void createParserForRss1() throws Exception {
        assertThat(FeedParserFactory.newInstanceFor("<rdf:RDF />"), instanceOf(Rss1Parser.class));
    }

    @Test
    public void createParserForRss2() throws Exception {
        assertThat(FeedParserFactory.newInstanceFor("<rss />"), instanceOf(Rss2Parser.class));
    }

    @Test
    public void createParserForAtom() throws Exception {
        assertThat(FeedParserFactory.newInstanceFor("<feed />"), instanceOf(AtomParser.class));
    }
}
