package net.bonono.rssreader.repository;

public abstract class Query<T> {
    private int mOffset = 0;
    private int mLimit = -1;

    public void offset(int offset) {
        mOffset = offset;
    }

    public int offset() {
        return mOffset;
    }

    public void limit(int limit) {
        mLimit = limit;
    }

    public int limit() {
        return mLimit;
    }
}
