package net.bonono.rssreader.lib;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

public class XmlDefinition {
    private String mTag;
    private boolean mContainText;
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

    private XmlDefinition(String tag, boolean containText) {
        mTag = tag;
        mContainText = containText;
    }

    public String getTag() {
        return mTag;
    }

    public void nest(XmlDefinition def) {
        mDefs.put(def.getTag(), def);
    }

    public XmlResult process(XmlPullParser parser) throws XmlPullParserException, IOException {
        XmlResult result = null;
        int depth = parser.getDepth();

        while (!(parser.getEventType() == XmlPullParser.END_DOCUMENT ||
                parser.getEventType() == XmlPullParser.END_TAG && parser.getDepth() == depth)) {
            parser.next();

            if (mContainText && parser.getEventType() == XmlPullParser.TEXT) {

                result = new XmlResult(parser.getText());

            } else if (parser.getEventType() == XmlPullParser.START_TAG &&
                    parser.getDepth() == depth + 1 && mDefs.containsKey(parser.getName())) {
                if (result == null) {
                    result = new XmlResult();
                }

                String tag = parser.getName();
                result.add(tag, mDefs.get(tag).process(parser));
            }
        }

        return result;
    }
}
