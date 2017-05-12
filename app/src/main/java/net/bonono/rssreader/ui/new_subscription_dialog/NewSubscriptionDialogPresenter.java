package net.bonono.rssreader.ui.new_subscription_dialog;

import net.bonono.rssreader.domain_logic.rss.Feed;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class NewSubscriptionDialogPresenter implements NewSubscriptionDialogContract.Presenter {
    private NewSubscriptionDialogContract.View mView;
    private Feed mLastSearched;

    public NewSubscriptionDialogPresenter(NewSubscriptionDialogContract.View view) {
        mView = view;
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
        // TODO: implement
    }
}
