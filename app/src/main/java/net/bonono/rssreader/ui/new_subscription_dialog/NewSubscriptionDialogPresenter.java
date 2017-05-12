package net.bonono.rssreader.ui.new_subscription_dialog;

import net.bonono.rssreader.domain_logic.rss.Feed;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.Repository;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.RealmRepository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class NewSubscriptionDialogPresenter implements NewSubscriptionDialogContract.Presenter {
    private NewSubscriptionDialogContract.View mView;
    private Repository<Site> mRepo;
    private Feed mLastSearched;

    public NewSubscriptionDialogPresenter(NewSubscriptionDialogContract.View view) {
        mView = view;
        mRepo = new SiteRepository();
    }

    @Override
    public void search(String url) {
        mView.showLoading(true);

        mView.bindLifeCycleAndScheduler(Feed.search(url))
                .subscribe(new DisposableObserver<Feed>() {
                    @Override
                    public void onNext(@NonNull Feed feed) {
                        mLastSearched = feed;
                        mView.completeToSearch(feed);
                        mView.showLoading(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.failedToSearch();
                        mView.showLoading(false);
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void addLastSearched() {
        if(mRepo.count(new SiteRepository.SameUrl(mLastSearched.getSite().getUrl())) > 0) {
            mView.duplicated();
        } else {
            mRepo.transaction(() -> {
                Site saved = mRepo.save(mLastSearched.getSite());

                EntryRepository entryRepo = new EntryRepository((RealmRepository)mRepo);
                for (Entry e : mLastSearched.getEntries()) {
                    e.belongTo(saved);
                    entryRepo.save(e);
                }
            });
            mView.completeToSubscribe();
        }

        mLastSearched = null;
    }
}
