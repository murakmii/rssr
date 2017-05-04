package net.bonono.rssreader.lib.rss;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class FeedTest {
    @Test
    public void defaultFeedIsInvalid() throws Exception {
        assertThat(new Feed().isValid(), is(false));
    }

    @Test
    public void feedHasTitleAndUrlIsValid() throws Exception {
        Feed sut = new Feed();
        sut.setTitle("foo");
        sut.setUrl("bar");
        assertThat(sut.isValid(), is(true));
    }
}
