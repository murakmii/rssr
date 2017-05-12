package net.bonono.rssreader.repository.realm;

import android.support.annotation.Nullable;

import net.bonono.rssreader.entity.Entry;

public class EntryRepository extends RealmRepository<Entry> {
    public EntryRepository(@Nullable RealmRepository<?> transactionRepo) {
        super(transactionRepo);
    }

    @Override
    public Class<Entry> provideClass() {
        return Entry.class;
    }
}
