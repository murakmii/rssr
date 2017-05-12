package net.bonono.rssreader.entity;

import android.support.test.runner.AndroidJUnit4;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class SiteTest {
    @Test
    public void collectUrlFromHtml() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("html/index2.html");

        Site sut = new Site();
        sut.collectUrlFromHtml(Jsoup.parse(is, null, "http://example.test/"));

        assertThat(sut.getIconUrl(), equalTo("http://example.test/favicon.ico"));
        assertThat(sut.getThumbnailUrl(), equalTo("http://sub.example.test/og.png"));
        assertThat(sut.getFeedUrl(), equalTo("http://example.test/atom.xml"));
    }
}
