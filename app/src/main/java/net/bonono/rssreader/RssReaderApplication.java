package net.bonono.rssreader;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class RssReaderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
