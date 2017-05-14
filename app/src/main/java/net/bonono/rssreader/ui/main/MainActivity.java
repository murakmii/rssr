package net.bonono.rssreader.ui.main;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bonono.rssreader.R;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import java.util.List;

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
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        mPresenter = new MainPresenter(this);
        mPresenter.loadDefaultSite();
    }

    @Override
    public void show(Site site, List<Entry> entries) {
        mToolbar.setTitle(site.getTitle());
        mRecycler.setAdapter(new EntryAdapter(this, entries));
        closeDrawer();
    }

    @Override
    public void showNoSite() {

    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public MainContract.Presenter getPresenter() {
        return mPresenter;
    }

    private class EntryViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        public EntryViewHolder(View view) {
            super(view);
            mView = view;
        }

        public void setEntry(Entry entry) {
            ((TextView)mView.findViewById(R.id.title)).setText(entry.getTitle());
        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {
        private LayoutInflater mInflater;
        private List<Entry> mEntries;

        public EntryAdapter(Context context, List<Entry> entries) {
            mInflater = LayoutInflater.from(context);
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
            return new EntryViewHolder(v);
        }
    }
}
