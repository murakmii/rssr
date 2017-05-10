package net.bonono.rssreader.repository;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public interface Query<T extends RealmObject> {
    RealmResults<T> toResult(Realm realm);

    int offset();
    int limit();
}
