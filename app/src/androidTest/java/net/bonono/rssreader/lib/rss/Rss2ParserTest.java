package net.bonono.rssreader.lib.rss;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDateTime;

import java.io.InputStream;

import okio.Okio;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class Rss2ParserTest {
    @Test
    public void returnCorrectFeed() throws Exception {
        Rss2Parser sut = new Rss2Parser();

        InputStream is = getClass().getClassLoader().getResourceAsStream("feed/rss2.xml");
        Feed actual = sut.parse(Okio.buffer(Okio.source(is)).readUtf8());

        assertThat(actual.getTitle(), equalTo("foo"));
        assertThat(actual.getUrl(), equalTo("http://example.test"));
        assertThat(actual.getDescription(), equalTo("bar"));

        Entry entry = actual.getEntries().get(0);
        assertThat(entry.getTitle(), equalTo("new entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/new-entry"));
        assertThat(entry.getDescription(), equalTo("this is new entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 5, 1, 12, 34, 56)));

        entry = actual.getEntries().get(1);
        assertThat(entry.getTitle(), equalTo("old entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/old-entry"));
        assertThat(entry.getDescription(), equalTo("this is old entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 4, 30, 11, 0, 0)));

        entry = actual.getEntries().get(2);
        assertThat(entry.getTitle(), equalTo("what"));
        assertThat(entry.getUrl(), equalTo("http://example.test/what"));
        assertThat(entry.getDescription(), equalTo("what is this"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 4, 30, 10, 0, 0)));
    }
}
