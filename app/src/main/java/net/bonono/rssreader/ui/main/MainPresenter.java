package net.bonono.rssreader.ui.main;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.Repository;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.RealmContext;
import net.bonono.rssreader.repository.realm.SiteRepository;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private RealmContext mRepoCtx;
    private Repository<Site> mSiteRepo;
    private Repository<Entry> mEntryRepo;

    public MainPresenter(MainContract.View view, Repository<Site> siteRepo, Repository<Entry> entryRepo) {
        mView = view;
        mSiteRepo = siteRepo;
        mEntryRepo = entryRepo;

        mRepoCtx = new RealmContext();
        mSiteRepo.bindContext(mRepoCtx);
        mEntryRepo.bindContext(mRepoCtx);
    }

    @Override
    public void loadSite(Site site) {
        mView.show(site);
    }

    @Override
    public void loadDefaultSite() {
        List<Site> sites = mSiteRepo.get(new SiteRepository.Unread());

        if (sites.size() > 0) {
            loadSite(sites.get(0));
        } else {
            sites = mSiteRepo.get(new SiteRepository.All());
            if (sites.size() > 0) {
                loadSite(sites.get(0));
            } else {
                mView.showNoSite();
            }
        }
    }

    @Override
    public List<Entry> getEntries(long siteId, EntryRepository.Filter filter) {
        return mEntryRepo.get(new EntryRepository.BelongTo(siteId, filter));
    }

    @Override
    public void activateEntry(Entry entry) {
        mEntryRepo.transaction(entry::doneReading);
        mView.showEntryDetail(entry);
    }

    @Override
    public void dispose() {
        mRepoCtx.close();
    }
}
