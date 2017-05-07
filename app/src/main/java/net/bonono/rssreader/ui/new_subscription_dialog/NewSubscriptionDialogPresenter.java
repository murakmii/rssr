package net.bonono.rssreader.ui.new_subscription_dialog;

import net.bonono.rssreader.lib.WebResource;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class NewSubscriptionDialogPresenter implements NewSubscriptionDialogContract.Presenter {
    private NewSubscriptionDialogContract.View mView;
    private WebResource mLastSearched;

    public NewSubscriptionDialogPresenter(NewSubscriptionDialogContract.View view) {
        mView = view;
    }

    @Override
    public void search(String url) {
        mView.showLoading(true);

        mView.bindLifeCycleAndScheduler(WebResource.explore(url))
                .subscribe(new DisposableObserver<WebResource>() {
                    @Override
                    public void onNext(@NonNull WebResource webResource) {
                        mLastSearched = webResource;
                        mView.completeToSearch(webResource);
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
