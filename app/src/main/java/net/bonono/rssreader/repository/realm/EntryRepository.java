package net.bonono.rssreader.repository.realm;

import android.support.annotation.Nullable;

import net.bonono.rssreader.entity.Entry;
import net.bonono.rssreader.entity.Site;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EntryRepository extends RealmRepository<Entry> {
    public EntryRepository() {
        super();
    }

    public EntryRepository(@Nullable RealmRepository<?> transactionRepo) {
        super(transactionRepo);
    }

    @Override
    public Class<Entry> provideClass() {
        return Entry.class;
    }

    public static class BelongTo implements RealmQuery<Entry> {
        private Site mSite;

        public BelongTo(Site site) {
            mSite = site;
        }

        @Override
        public RealmResults<Entry> toResult(Realm realm) {
            return realm
                    .where(Entry.class)
                    .equalTo("siteId", mSite.getId())
                    .findAllSorted("createdAt", Sort.DESCENDING);
        }
    }
}
