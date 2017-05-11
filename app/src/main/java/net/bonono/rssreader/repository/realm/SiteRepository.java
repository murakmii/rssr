package net.bonono.rssreader.repository.realm;

import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.RealmRepository;

public class SiteRepository extends RealmRepository<Site> {
    @Override
    protected Class<Site> provideClass() {
        return Site.class;
    }
}
