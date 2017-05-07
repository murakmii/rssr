package net.bonono.rssreader.ui.new_subscription_dialog;

import net.bonono.rssreader.lib.WebResource;

import io.reactivex.Observable;

public interface NewSubscriptionDialogContract {
    interface View {
        void showLoading(boolean visible);
        Observable<WebResource> bindLifeCycleAndScheduler(Observable<WebResource> observable);

        void completeToSearch(WebResource webRes);
        void failedToSearch();
    }

    interface Presenter {
        void search(String url);
        void addLastSearched();
    }
}
