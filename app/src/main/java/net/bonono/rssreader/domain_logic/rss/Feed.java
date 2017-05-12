package net.bonono.rssreader.domain_logic.rss;

import android.text.TextUtils;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private Site mSite;
    private List<Entry> mEntries;

    public Feed() {
        mEntries = new ArrayList<>();
    }

    public void setSite(Site site) {
        mSite = site;
    }

    public Site getSite() {
        return mSite;
    }

    public void addEntry(Entry entry) {
        mEntries.add(entry);
    }

    public List<Entry> getEntries() {
        return mEntries;
    }

    public boolean isValid() {
        return mSite != null &&
                !TextUtils.isEmpty(mSite.getTitle()) &&
                !TextUtils.isEmpty(mSite.getUrl());
    }
}
