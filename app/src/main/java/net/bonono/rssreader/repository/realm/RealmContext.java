package net.bonono.rssreader.repository.realm;

import net.bonono.rssreader.repository.Context;

import io.realm.Realm;

public class RealmContext implements Context {
    private Realm mRealm;

    public RealmContext() {
        mRealm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return mRealm;
    }

    public void close() {
        mRealm.close();
    }
}
