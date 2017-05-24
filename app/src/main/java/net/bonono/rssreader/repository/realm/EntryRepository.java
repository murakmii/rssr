package net.bonono.rssreader.repository.realm;

import android.support.annotation.Nullable;

import net.bonono.rssreader.entity.Entry;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EntryRepository extends RealmRepository<Entry> {
    public enum Filter {
        Unread,
        Bookmark,
        All;

        public io.realm.RealmQuery<Entry> apply(io.realm.RealmQuery<Entry> query) {
            if (this == Unread) {
                query = query.equalTo("read", false);
            } else if (this == Bookmark) {
                query = query.equalTo("bookmark", true);
            }

            return query;
        }
    }

    public EntryRepository() {
        super();
    }

    public EntryRepository(@Nullable Realm realm) {
        super(realm);
    }

    @Override
    public Class<Entry> provideClass() {
        return Entry.class;
    }

    public static class BelongTo implements RealmQuery<Entry> {
        private long mSiteId;
        private Filter mFilter;

        public BelongTo(long siteId, Filter filter) {
            mSiteId = siteId;
            mFilter = filter;
        }

        @Override
        public RealmResults<Entry> toResult(Realm realm) {
            io.realm.RealmQuery<Entry> query = realm.where(Entry.class).equalTo("siteId", mSiteId);
            return mFilter.apply(query).findAllSorted("createdAt", Sort.DESCENDING);
        }
    }
}
