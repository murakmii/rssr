package net.bonono.rssreader.repository.realm;

import net.bonono.rssreader.entity.Entry;

public class EntryRepository extends RealmRepository<Entry> {
    @Override
    public Class<Entry> provideClass() {
        return Entry.class;
    }
}
