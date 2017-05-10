package net.bonono.rssreader.repository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmRepository<T extends RealmObject> implements Repository<T> {
    private Realm mRealm;

    public RealmRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void transaction(Runnable inTransaction) {
        mRealm.executeTransaction(realm -> {
            Realm tmp = mRealm;
            mRealm = realm;
            try {
                inTransaction.run();
            } catch (Exception e) {
                mRealm = tmp;
                throw e;
            }
        });
    }

    @Override
    public T save(T entity) {
        return mRealm.copyToRealm(entity);
    }

    @Override
    public void remove(T entity) {
        entity.deleteFromRealm();
    }

    @Override
    public void remove(Query<T> query) {
        assertQuery(query).toResult(mRealm).deleteAllFromRealm();
    }

    public int count(Query<T> query) {
        return assertQuery(query).toResult(mRealm).size();
    }

    public List<T> get(Query<T> query) {
        RealmResults<T> result = assertQuery(query).toResult(mRealm);

        int limit = query.limit(), offset = query.offset(), all = result.size();
        if (offset >= all) {
            return new ArrayList<>();
        }

        int end = limit == -1 ? all : Math.min(offset + limit, all);

        List<T> list = new ArrayList<>();
        for (int i = offset; i < end; i++) {
            list.add(result.get(i));
        }

        return list;
    }

    private RealmQuery<T> assertQuery(Query<T> query) {
        if (!(query instanceof RealmQuery)) {
            throw new IllegalArgumentException("Query isn't RealmQuery");
        }

        return (RealmQuery<T>)query;
    }
}
