package net.bonono.rssreader;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class RssReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
