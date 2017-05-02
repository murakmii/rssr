package net.bonono.rssreader.lib.rss;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDateTime;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AtomParserTest {
    @Test
    public void canParseSourceAtom03() throws Exception {
        AtomParser sut = new AtomParser();
        Feed actual = sut.parse(getClass().getClassLoader().getResourceAsStream("feed/atom03.xml"));

        assertThat(actual.getTitle(), equalTo("foo"));
        assertThat(actual.getUrl(), equalTo("http://example.test"));
        assertThat(actual.getDescription(), equalTo("bar"));

        Entry entry = actual.getEntries().get(0);
        assertThat(entry.getTitle(), equalTo("new entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/new-entry"));
        assertThat(entry.getDescription(), equalTo("this is new entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56)));

        entry = actual.getEntries().get(1);
        assertThat(entry.getTitle(), equalTo("old entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/old-entry"));
        assertThat(entry.getDescription(), equalTo("this is old entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 5, 2, 11, 0, 0)));
    }

    @Test
    public void canParseSourceAtom10() throws Exception {
        AtomParser sut = new AtomParser();
        Feed actual = sut.parse(getClass().getClassLoader().getResourceAsStream("feed/atom10.xml"));

        assertThat(actual.getTitle(), equalTo("foo"));
        assertThat(actual.getUrl(), equalTo("http://example.test"));
        assertThat(actual.getDescription(), equalTo("bar"));

        Entry entry = actual.getEntries().get(0);
        assertThat(entry.getTitle(), equalTo("new entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/new-entry"));
        assertThat(entry.getDescription(), equalTo("this is new entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56)));

        entry = actual.getEntries().get(1);
        assertThat(entry.getTitle(), equalTo("old entry"));
        assertThat(entry.getUrl(), equalTo("http://example.test/old-entry"));
        assertThat(entry.getDescription(), equalTo("this is old entry"));
        assertThat(entry.getCreatedAt(), equalTo(LocalDateTime.of(2017, 5, 2, 11, 0, 0)));
    }

    @Test
    public void canParseDate() {
        assertThat(AtomParser.parseDate("2017-05-02"), is(nullValue()));
        assertThat(AtomParser.parseDate("2017-05-02T12:34:56Z"), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56)));
        assertThat(AtomParser.parseDate("2017-05-02T12:34:56.789Z"), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56, 789 * 1000000)));
        assertThat(AtomParser.parseDate("2017-05-02T12:34:56+09:00"), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56)));
        assertThat(AtomParser.parseDate("2017-05-02T12:34:56.789+09:00"), equalTo(LocalDateTime.of(2017, 5, 2, 12, 34, 56, 789 * 1000000)));
    }
}
