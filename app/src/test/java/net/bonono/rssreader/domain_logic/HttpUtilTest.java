package net.bonono.rssreader.domain_logic;

import net.bonono.rssreader.domain_logic.HttpUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class HttpUtilTest {
    private MockWebServer server;

    @Before
    public void setUp() {
        server = new MockWebServer();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void returnResponseData() throws Exception {
        server.enqueue(new MockResponse().setBody("ABCDE"));
        server.start();

        HttpUtil.ResponseWrapper sut = HttpUtil.get(server.url("/").toString());
        assertThat(sut.body().string(), equalTo("ABCDE"));
    }

    @Test
    public void followRedirects() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(301).setHeader("Location", "/redirect"));
        server.enqueue(new MockResponse().setBody("ABCDE"));
        server.start();

        HttpUtil.ResponseWrapper sut = HttpUtil.get(server.url("/test").toString());

        assertThat(sut.body().string(), equalTo("ABCDE"));
        assertThat(server.takeRequest().getPath(), equalTo("/test"));
        assertThat(server.takeRequest().getPath(), equalTo("/redirect"));
    }

    @Test(expected = IOException.class)
    public void returnNullIfGivenUnexpectedCode() throws Exception {
        server.enqueue(new MockResponse().setBody("ABCDE").setResponseCode(500));
        server.start();

        HttpUtil.get(server.url("/test").toString());
    }

    @Test
    public void detectCharset() throws Exception {
        server.enqueue(new MockResponse().setBody("ABCDE").setResponseCode(200).setHeader("Content-Type", "text/html; charset=utf-8"));
        server.start();

        HttpUtil.ResponseWrapper sut = HttpUtil.get(server.url("/test").toString());

        assertThat(sut.charset(), equalTo("UTF-8"));
        assertThat(sut.isHtml(), is(true));
    }
}
