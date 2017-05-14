package net.bonono.rssreader.ui.drawer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.bonono.rssreader.R;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.ui.main.MainContract;
import net.bonono.rssreader.ui.new_subscription_dialog.NewSubscriptionDialogFragment;

import java.util.List;

public class DrawerFragment extends Fragment implements DrawerContract.View {
    private DrawerContract.Presenter mPresenter;
    private ListView mList;
    private SiteAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DrawerPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SiteAdapter(getActivity(), mPresenter.loadSites());

        mList = (ListView)view;
        mList.setAdapter(mAdapter);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.view_header_in_drawer, mList, false);

        header.findViewById(R.id.new_subscription).setOnClickListener(button -> {
            new NewSubscriptionDialogFragment().show(getActivity().getSupportFragmentManager(), "dialog");

            Activity activity = getActivity();
            if (activity instanceof MainContract.View) {
                ((MainContract.View)activity).closeDrawer();
            }
        });

        mList.addHeaderView(header);
        mList.setOnItemClickListener((parent, item, position, id) -> {
            Activity activity = getActivity();
            if (activity instanceof MainContract.View) {
                ((MainContract.View)activity).getPresenter().loadSite((Site) mAdapter.getItem(position - 1));
            }
        });
    }

    private static class SiteAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<Site> mSites;

        public SiteAdapter(Context context, List<Site> sites) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mSites = sites;
        }

        @Override
        public int getCount() {
            return mSites.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            if (view == null) {
                view = mInflater.inflate(R.layout.view_site_in_drawer, container, false);
            }

            Site s = mSites.get(position);
            if (!TextUtils.isEmpty(s.getIconUrl())) {
                Glide.with(mContext).load(s.getIconUrl()).into((ImageView)view.findViewById(R.id.icon));
            }

            ((TextView)view.findViewById(R.id.title)).setText(s.getTitle());
            ((TextView)view.findViewById(R.id.unread)).setText(Integer.toString(s.getUnreadCount()));

            return view;
        }

        @Override
        public Object getItem(int position) {
            return mSites.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
