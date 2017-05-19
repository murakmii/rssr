package net.bonono.rssreader.ui.main;

import android.support.test.runner.AndroidJUnit4;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.Repository;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class MainPresenterTest {
    private MainContract.View viewMock;
    private Repository<Site> siteRepoMock;
    private Repository<Entry> entryRepoMock;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        viewMock = mock(MainContract.View.class);
        siteRepoMock = (Repository<Site>)mock(Repository.class);
        entryRepoMock = (Repository<Entry>)mock(Repository.class);
    }

    @Test
    public void loadSite() throws Exception {
        Site dummySite = new Site();
        List<Entry> dummyEntries = new ArrayList<>();
        dummyEntries.add(new Entry());

        when(entryRepoMock.get(any(EntryRepository.BelongTo.class))).thenReturn(dummyEntries);

        MainContract.Presenter sut = new MainPresenter(viewMock, siteRepoMock, entryRepoMock);
        sut.loadSite(dummySite);

        verify(viewMock).show(dummySite, dummyEntries);
    }

    @Test
    public void loadDefaultSiteForSiteIsUnread() throws Exception {
        List<Site> dummyUnread = new ArrayList<>();
        dummyUnread.add(new Site());

        when(siteRepoMock.get(any(SiteRepository.Unread.class))).thenReturn(dummyUnread);

        MainContract.Presenter sut = spy(new MainPresenter(viewMock, siteRepoMock, entryRepoMock));
        sut.loadDefaultSite();

        verify(sut).loadSite(dummyUnread.get(0));
    }

    @Test
    public void loadDefaultSiteForSiteIsRead() throws Exception {
        List<Site> dummyRead = new ArrayList<>();
        dummyRead.add(new Site());

        when(siteRepoMock.get(any(SiteRepository.Unread.class))).thenReturn(new ArrayList<>());
        when(siteRepoMock.get(any(SiteRepository.All.class))).thenReturn(dummyRead);

        MainContract.Presenter sut = spy(new MainPresenter(viewMock, siteRepoMock, entryRepoMock));
        sut.loadDefaultSite();

        verify(sut).loadSite(dummyRead.get(0));
    }

    @Test
    public void loadDefaultSiteForNoSites() throws Exception {
        when(siteRepoMock.get(any(SiteRepository.Unread.class))).thenReturn(new ArrayList<>());
        when(siteRepoMock.get(any(SiteRepository.All.class))).thenReturn(new ArrayList<>());

        MainContract.Presenter sut = new MainPresenter(viewMock, siteRepoMock, entryRepoMock);
        sut.loadDefaultSite();

        verify(viewMock).showNoSite();
    }

    @Test
    public void activateEntry() throws Exception {
        Entry dummy = new Entry();

        MainContract.Presenter sut = new MainPresenter(viewMock, siteRepoMock, entryRepoMock);
        sut.activateEntry(dummy);

        verify(entryRepoMock).transaction(any(Runnable.class));
        verify(viewMock).showEntryDetail(dummy);
    }
}
