package net.bonono.rssreader.ui.new_subscription_dialog;

import net.bonono.rssreader.domain_logic.rss.Feed;

import io.reactivex.Observable;

public interface NewSubscriptionDialogContract {
    interface View {
        void showLoading(boolean visible);
        Observable<Feed> bindLifeCycleAndScheduler(Observable<Feed> observable);

        void completeToSearch(Feed feed);
        void failedToSearch();
        void duplicated();
        void completeToSubscribe();
    }

    interface Presenter {
        void search(String url);
        void addLastSearched();
    }
}
