package net.bonono.rssreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import net.bonono.rssreader.lib.XmlDefinition;
import net.bonono.rssreader.lib.XmlResult;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
