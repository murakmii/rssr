package net.bonono.rssreader.ui.main;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import java.util.List;

public interface MainContract {
    interface View {
        void showNoSite();
        void show(Site site, List<Entry> entries);
        Presenter getPresenter();
    }

    interface Presenter {
        void loadSite(Site site);
        void loadDefaultSite();
    }
}
