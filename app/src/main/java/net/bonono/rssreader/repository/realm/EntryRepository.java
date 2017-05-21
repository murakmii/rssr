package net.bonono.rssreader.repository.realm;

import android.support.annotation.Nullable;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EntryRepository extends RealmRepository<Entry> {
    public enum Filter {
        Unread,
        All
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

        public BelongTo(long siteId) {
            this(siteId, Filter.All);
        }

        public BelongTo(long siteId, Filter filter) {
            mSiteId = siteId;
            mFilter = filter;
        }

        public BelongTo(Site site, Filter filter) {
            this(site.getId(), filter);
        }

        @Override
        public RealmResults<Entry> toResult(Realm realm) {
            io.realm.RealmQuery<Entry> query = realm.where(Entry.class).equalTo("siteId", mSiteId);

            if (mFilter == Filter.Unread) {
                query = query.equalTo("read", false);
            }

            return query.findAllSorted("createdAt", Sort.DESCENDING);
        }
    }
}
