package net.bonono.rssreader.repository;

import java.util.List;

public interface Repository<T> {
    void transaction(Runnable inTransaction);

    void bindContext(Context context);

    T save(T entity);

    void remove(T entity);
    void remove(Query<T> query);

    int count(Query<T> query);

    List<T> get(Query<T> query);
}
