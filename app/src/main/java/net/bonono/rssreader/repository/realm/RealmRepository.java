package net.bonono.rssreader.repository.realm;

import android.support.annotation.Nullable;

import net.bonono.rssreader.entity.Identifiable;
import net.bonono.rssreader.repository.Query;
import net.bonono.rssreader.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmRepository<T extends RealmObject> implements Repository<T> {
    private Realm mRealm;

    public RealmRepository() {
        this(null);
    }

    public RealmRepository(@Nullable RealmRepository<?> transactionRepo) {
        if (transactionRepo == null) {
            mRealm = Realm.getDefaultInstance();
        } else {
            if (!transactionRepo.mRealm.isInTransaction()) {
                throw new IllegalArgumentException("Passed repository should be processing in transaction");
            }

            mRealm = transactionRepo.mRealm;
        }
    }

    @Override
    public void transaction(Runnable inTransaction) {
        if (mRealm.isInTransaction()) {
            inTransaction.run();
        } else {
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
    }

    @Override
    public T save(T entity) {
        if (entity instanceof Identifiable && ((Identifiable)entity).getId() <= 0L) {
            ((Identifiable)entity).setId(computeId());
        }

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

    @Override
    public int count(Query<T> query) {
        return assertQuery(query).toResult(mRealm).size();
    }

    @Override
    public List<T> get(Query<T> query) {
        return assertQuery(query).toResult(mRealm);
    }

    protected abstract Class<T> provideClass();

    private RealmQuery<T> assertQuery(Query<T> query) {
        if (!(query instanceof RealmQuery)) {
            throw new IllegalArgumentException("Query isn't RealmQuery");
        }

        return (RealmQuery<T>)query;
    }

    private synchronized long computeId() {
        Number id = mRealm.where(provideClass()).max("id");
        return id == null ? 1L : (((long)id) + 1);
    }
}
