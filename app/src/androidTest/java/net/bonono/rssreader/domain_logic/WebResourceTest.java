package net.bonono.rssreader.domain_logic;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Okio;
import okio.Source;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class WebResourceTest {
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void collectDataFromHtmlOnly() throws Exception {
        Source html = Okio.source(getClass().getClassLoader().getResourceAsStream("html/index.html"));
        server.enqueue(new MockResponse()
                .setBody(Okio.buffer(html).readUtf8())
                .setHeader("Content-Type", "text/html; charset=utf-8")
        );

        WebResource sut = new WebResource();
        sut.collectDataFromHtml(HttpUtil.get(server.url("/").toString()), false);

        assertThat(sut.getImageUrl(), equalTo(server.url("/favicon.ico").toString()));
        assertThat(sut.getFeeds().size(), is(0));
        assertThat(server.getRequestCount(), is(1));
    }

    @Test
    public void collectDataFromHtmlAndXml() throws Exception {
        Source html = Okio.source(getClass().getClassLoader().getResourceAsStream("html/index.html"));
        server.enqueue(new MockResponse()
                .setBody(Okio.buffer(html).readUtf8())
                .setHeader("Content-Type", "text/html; charset=utf-8")
        );

        Source xml = Okio.source(getClass().getClassLoader().getResourceAsStream("feed/atom10.xml"));
        server.enqueue(new MockResponse()
                .setBody(Okio.buffer(xml).readUtf8())
                .setHeader("Content-Type", "text/xml; charset=utf-8")
        );

        WebResource sut = new WebResource();
        sut.collectDataFromHtml(HttpUtil.get(server.url("/").toString()), true);

        assertThat(sut.getImageUrl(), equalTo(server.url("/favicon.ico").toString()));
        assertThat(sut.getFeeds().size(), is(1));
        assertThat(sut.getFeeds().get(0).getSite().getTitle(), equalTo("foo"));
        assertThat(server.takeRequest().getPath(), equalTo("/"));
        assertThat(server.takeRequest().getPath(), equalTo("/atom.xml"));
    }

    @Test
    public void collectDataFromXmlOnly() throws Exception {
        Source xml = Okio.source(getClass().getClassLoader().getResourceAsStream("feed/atom10.xml"));
        server.enqueue(new MockResponse()
                .setBody(Okio.buffer(xml).readUtf8())
                .setHeader("Content-Type", "text/xml; charset=utf-8")
        );

        WebResource sut = new WebResource();
        sut.collectDataFromXml(HttpUtil.get(server.url("/").toString()), false);

        assertThat(sut.getFeeds().size(), is(1));
        assertThat(sut.getFeeds().get(0).getSite().getTitle(), equalTo("foo"));
        assertThat(sut.getImageUrl(), is(nullValue()));
    }
}
