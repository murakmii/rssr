package net.bonono.rssreader.repository.realm;

import net.bonono.rssreader.repository.Query;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public interface RealmQuery<T extends RealmObject> extends Query<T> {
    RealmResults<T> toResult(Realm realm);
}
