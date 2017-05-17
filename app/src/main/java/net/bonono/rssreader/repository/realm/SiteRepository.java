package net.bonono.rssreader.repository.realm;

import net.bonono.rssreader.entity.Site;
import net.bonono.rssreader.repository.realm.RealmRepository;

import io.realm.Realm;
import io.realm.RealmResults;

public class SiteRepository extends RealmRepository<Site> {
    public SiteRepository() {
        super();
    }

    public SiteRepository(Realm realm) {
        super(realm);
    }

    @Override
    protected Class<Site> provideClass() {
        return Site.class;
    }

    public static class All implements RealmQuery<Site> {
        @Override
        public RealmResults<Site> toResult(Realm realm) {
            return realm.where(Site.class).findAllSorted("id");
        }
    }

    public static class SameUrl implements RealmQuery<Site> {
        private String mUrl;

        public SameUrl(String url) {
            mUrl = url;
        }

        @Override
        public RealmResults<Site> toResult(Realm realm) {
            return realm.where(Site.class).equalTo("url", mUrl).findAll();
        }
    }

    public static class Unread implements RealmQuery<Site> {
        @Override
        public RealmResults<Site> toResult(Realm realm) {
            return realm.where(Site.class).greaterThan("unreadCount", 0).findAllSorted("id");
        }
    }
}
