package net.bonono.rssreader.ui.new_subscription_dialog;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Okio;
import okio.Source;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class NewSubscriptionDialogPresenterTest {
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
    public void whenSucceededToSearch() throws Exception {
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

        NewSubscriptionDialogContract.View mock = mock(NewSubscriptionDialogContract.View.class);
        when(mock.bindLifeCycleAndScheduler(any())).then(AdditionalAnswers.returnsFirstArg());

        NewSubscriptionDialogContract.Presenter sut = new NewSubscriptionDialogPresenter(mock);

        sut.search(server.url("/").toString());

        verify(mock).showLoading(true);
        verify(mock, timeout(3000)).completeToSearch(any());
        verify(mock).showLoading(false);
    }

    @Test
    public void whenFailedToSearch() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404));

        NewSubscriptionDialogContract.View mock = mock(NewSubscriptionDialogContract.View.class);
        when(mock.bindLifeCycleAndScheduler(any())).then(AdditionalAnswers.returnsFirstArg());

        NewSubscriptionDialogContract.Presenter sut = new NewSubscriptionDialogPresenter(mock);

        sut.search(server.url("/").toString());

        verify(mock).showLoading(true);
        verify(mock, timeout(3000)).failedToSearch();
        verify(mock).showLoading(false);
    }
}
