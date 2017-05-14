package net.bonono.rssreader.ui.main;

import android.util.Log;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
    }

    @Override
    public void loadSite(Site site) {
        List<Entry> entries = new EntryRepository().get(new EntryRepository.BelongTo(site));
        mView.show(site, entries);
    }

    @Override
    public void loadDefaultSite() {
        SiteRepository siteRepo = new SiteRepository();
        List<Site> sites = siteRepo.get(new SiteRepository.Unread());

        if (sites.size() > 0) {
            loadSite(sites.get(0));
        } else {
            sites = siteRepo.get(new SiteRepository.All());
            if (sites.size() > 0) {
                loadSite(sites.get(0));
            } else {
                mView.showNoSite();
            }
        }
    }
}
