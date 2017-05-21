package net.bonono.rssreader.ui.main;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.EntryRepository;

import java.util.List;

public interface MainContract {
    interface View {
        void showNoSite();
        void show(Site site);
        void closeDrawer();
        Presenter getPresenter();
        void showEntryDetail(Entry entry);

        void onClickEntry(Entry entry);
    }

    interface Presenter {
        void loadSite(Site site);
        void loadDefaultSite();

        List<Entry> getEntries(long siteId, EntryRepository.Filter filter);

        void activateEntry(Entry entry);
        void dispose();
    }
}
