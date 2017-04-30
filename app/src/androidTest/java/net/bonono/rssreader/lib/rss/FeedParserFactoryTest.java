package net.bonono.rssreader.lib.rss;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@RunWith(AndroidJUnit4.class)
public class FeedParserFactoryTest {
    @Test
    public void returnNullForInvalidXml() throws Exception {
        InputStream is = new ByteArrayInputStream("<test />".getBytes());
        assertThat(FeedParserFactory.newInstanceFor(is), is(nullValue()));
    }

    @Test
    public void createParserForRss1() throws Exception {
        InputStream is = new ByteArrayInputStream("<rdf:RDF />".getBytes());
        assertThat(FeedParserFactory.newInstanceFor(is), instanceOf(Rss1Parser.class));
    }
}