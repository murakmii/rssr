package net.bonono.rssreader

import android.app.Application

import com.jakewharton.threetenabp.AndroidThreeTen

import io.realm.Realm

class RssReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Realm.init(this)
    }
}
