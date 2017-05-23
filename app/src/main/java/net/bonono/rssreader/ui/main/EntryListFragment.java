package net.bonono.rssreader.ui.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bonono.rssreader.R;
import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.EntryRepository;

import java.util.List;

public class EntryListFragment extends Fragment {
    private static final String ARG_SITE_ID = "SITE_ID";
    private static final String ARG_FILTER = "FILTER";

    private long mSiteId;
    private EntryRepository.Filter mFilter;
    private RecyclerView mRecycler;

    public static EntryListFragment newInstance(Site site, EntryRepository.Filter filter) {
        Bundle args = new Bundle();
        args.putLong(ARG_SITE_ID, site.getId());
        args.putSerializable(ARG_FILTER, filter);

        EntryListFragment f = new EntryListFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mSiteId = args.getLong(ARG_SITE_ID, -1L);
            mFilter = (EntryRepository.Filter) args.getSerializable(ARG_FILTER);
        }

        if (mSiteId == -1L || mFilter == null) {
            throw new IllegalArgumentException("EntryListFragment requires site id and entry type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler = (RecyclerView)view;
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(llm);

        DividerItemDecoration did = new DividerItemDecoration(getContext(), llm.getOrientation());
        mRecycler.addItemDecoration(did);

        MainContract.View parent = (MainContract.View)getActivity();

        List<Entry> entries = parent.getPresenter().getEntries(mSiteId, mFilter);
        mRecycler.setAdapter(new EntryAdapter(getActivity(), parent, entries));
    }

    private static class EntryViewHolder extends RecyclerView.ViewHolder {
        private Entry mEntry;
        private TextView mTitle, mDate;

        EntryViewHolder(MainContract.View parent, View view) {
            super(view);
            mTitle = (TextView)view.findViewById(R.id.title);
            mDate = (TextView)view.findViewById(R.id.date);

            view.setOnClickListener(__ -> parent.onClickEntry(mEntry));
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

            mDate.setText(mEntry.getCreatedAt().toString());
        }
    }

    private static class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {
        private LayoutInflater mInflater;
        private MainContract.View mParent;
        private List<Entry> mEntries;

        EntryAdapter(Context context, MainContract.View parent, List<Entry> entries) {
            mInflater = LayoutInflater.from(context);
            mParent = parent;
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
            return new EntryViewHolder(mParent, v);
        }
    }
}
