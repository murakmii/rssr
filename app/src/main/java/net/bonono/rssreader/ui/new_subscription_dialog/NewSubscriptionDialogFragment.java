package net.bonono.rssreader.ui.new_subscription_dialog;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxDialogFragment;

import net.bonono.rssreader.R;
import net.bonono.rssreader.databinding.FragmentNewSubscriptionDialogBinding;
import net.bonono.rssreader.lib.WebResource;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewSubscriptionDialogFragment extends RxDialogFragment implements NewSubscriptionDialogContract.View {
    private NewSubscriptionDialogContract.Presenter mPresenter;
    private FragmentNewSubscriptionDialogBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NewSubscriptionDialogPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_subscription_dialog, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.setLoading(false);
        mBinding.check.setOnClickListener(__ -> mPresenter.search(mBinding.url.getText().toString()));
        mBinding.add.setOnClickListener(__ -> mPresenter.addLastSearched());
        mBinding.reSearch.setOnClickListener(__ -> {
            mBinding.setFeed(null);
            mBinding.url.setText(null);
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE); // hide title
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void showLoading(boolean visible) {
        mBinding.setLoading(visible);
    }

    @Override
    public Observable<WebResource> bindLifeCycleAndScheduler(Observable<WebResource> observable) {
        return observable
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void completeToSearch(WebResource webRes) {
        mBinding.setFeed(webRes.getFeeds().get(0));
    }

    @Override
    public void failedToSearch() {
        Toast.makeText(getContext(), R.string.failed_to_search, Toast.LENGTH_LONG).show();
    }
}
