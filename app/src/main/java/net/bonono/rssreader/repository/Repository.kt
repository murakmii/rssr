package net.bonono.rssreader.repository

interface Repository<T> {
    fun transaction(inTransaction: Runnable)

    fun bindContext(context: Context)

    fun save(entity: T): T

    fun remove(entity: T)
    fun remove(query: Query<T>)

    fun count(query: Query<T>): Int

    operator fun get(query: Query<T>): List<T>
}
