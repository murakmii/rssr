package net.bonono.rssreader.lib;

import android.support.annotation.VisibleForTesting;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class XmlDefinition {
    private String mTag;
    private boolean mAcceptText;
    private HashMap<String, XmlDefinition> mDefs = new HashMap<>();

    public static XmlDefinition rootOf(XmlDefinition def) {
        XmlDefinition root = new XmlDefinition(null, false);
        root.nest(def);
        return root;
    }

    public static XmlDefinition forTag(String tag) {
        return new XmlDefinition(tag, false);
    }

    public static XmlDefinition forText(String tag) {
        return new XmlDefinition(tag, true);
    }

    private XmlDefinition(String tag, boolean acceptText) {
        mTag = tag;
        mAcceptText = acceptText;
    }

    public String getTag() {
        return mTag;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public boolean acceptText() {
        return mAcceptText;
    }

    public void nest(XmlDefinition def) {
        mDefs.put(def.getTag(), def);
    }

    public XmlResult parse(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
        return parse(parser);
    }

    public XmlResult parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        XmlResult result = new XmlResult();
        int startDepth = parser.getDepth();

        while (!doesReachEnd(parser, startDepth)) {
            parser.next();

            if (mAcceptText && parser.getEventType() == XmlPullParser.TEXT) {
                result.setText(parser.getText());
            } else if (doesAcceptCurrentTag(parser, startDepth)) {
                String tag = parser.getName();
                result.add(tag, mDefs.get(tag).parse(parser));
            }
        }

        return result;
    }

    private static boolean doesReachEnd(XmlPullParser parser, int startDepth)
            throws XmlPullParserException, IOException {

        return parser.getEventType() == XmlPullParser.END_DOCUMENT ||
                (parser.getEventType() == XmlPullParser.END_TAG && parser.getDepth() == startDepth);
    }

    private boolean doesAcceptCurrentTag(XmlPullParser parser, int startDepth)
            throws XmlPullParserException, IOException {

        return parser.getEventType() == XmlPullParser.START_TAG &&
                parser.getDepth() == startDepth + 1 &&
                mDefs.containsKey(parser.getName());
    }
}
