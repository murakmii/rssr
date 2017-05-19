package net.bonono.rssreader.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bonono.rssreader.R;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.EntryRepository;
import net.bonono.rssreader.repository.realm.SiteRepository;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private RecyclerView mRecycler;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_root);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mRecycler = (RecyclerView)findViewById(R.id.recycler);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(llm);

        DividerItemDecoration did = new DividerItemDecoration(mRecycler.getContext(), llm.getOrientation());
        mRecycler.addItemDecoration(did);

        mPresenter = new MainPresenter(this, new SiteRepository(), new EntryRepository());
        mPresenter.loadDefaultSite();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dispose();
    }

    @Override
    public void show(Site site, List<Entry> entries) {
        mToolbar.setTitle(site.getTitle());
        mRecycler.setAdapter(new EntryAdapter(this, mPresenter, entries));
        closeDrawer();
    }

    @Override
    public void showNoSite() {

    }

    @Override
    public void showEntryDetail(Entry entry) {
        // dummy
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(entry.getUrl())));
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public MainContract.Presenter getPresenter() {
        return mPresenter;
    }

    private static class EntryViewHolder extends RecyclerView.ViewHolder {
        private Entry mEntry;
        private TextView mTitle;

        public EntryViewHolder(MainContract.Presenter presenter, View view) {
            super(view);
            mTitle = (TextView)view.findViewById(R.id.title);

            view.setOnClickListener(__ -> presenter.activateEntry(mEntry));
        }

        public void setEntry(Entry entry) {
            if (mEntry != null) {
                mEntry.removeAllChangeListeners();
            }

            mEntry = entry;
            mEntry.addChangeListener(__ -> updateText());

            updateText();
        }

        private void updateText() {
            mTitle.setText(mEntry.getTitle());
            if (mEntry.hasRead()) {
                mTitle.setTypeface(null, Typeface.NORMAL);
                mTitle.setTextColor(Color.GRAY);
            } else {
                mTitle.setTypeface(null, Typeface.BOLD);
                mTitle.setTextColor(Color.BLACK);
            }
        }
    }

    private static class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {
        private LayoutInflater mInflater;
        private MainContract.Presenter mPresenter;
        private List<Entry> mEntries;

        public EntryAdapter(Context context, MainContract.Presenter presenter, List<Entry> entries) {
            mInflater = LayoutInflater.from(context);
            mPresenter = presenter;
            mEntries = entries;
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }

        @Override
        public void onBindViewHolder(EntryViewHolder vh, int position) {
            vh.setEntry(mEntries.get(position));
        }

        @Override
        public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mInflater.inflate(R.layout.view_entry_in_list, parent, false);
            return new EntryViewHolder(mPresenter, v);
        }
    }
}
