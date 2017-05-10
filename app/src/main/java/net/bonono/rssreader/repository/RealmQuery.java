package net.bonono.rssreader.repository;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmQuery<T extends RealmObject> extends Query<T> {
    public abstract RealmResults<T> toResult(Realm realm);
}
