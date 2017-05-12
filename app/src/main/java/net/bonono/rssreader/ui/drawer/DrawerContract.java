package net.bonono.rssreader.ui.drawer;

import net.bonono.rssreader.entity.Site;

import java.util.List;

public interface DrawerContract {
    interface View { }

    interface Presenter {
        List<Site> loadSites();
    }
}
