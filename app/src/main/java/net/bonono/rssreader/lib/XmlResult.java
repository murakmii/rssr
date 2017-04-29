package net.bonono.rssreader.lib;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlResult {
    private static final XmlResult sNull = new XmlResult();

    private HashMap<String, List<XmlResult>> mResult;
    private HashMap<String, String> mAttrs;
    private String mText;

    public XmlResult() {
        // do nothing
    }

    public XmlResult(XmlPullParser parser) {
        int attrCount = parser.getAttributeCount();
        if (attrCount > 0) {
            mAttrs = new HashMap<>();
            for (int i = 0; i < attrCount; i++) {
                mAttrs.put(parser.getAttributeName(i), parser.getAttributeValue(i));
            }
        }
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public void add(String tag, XmlResult result) {
        if (mResult == null) {
            mResult = new HashMap<>();
        }

        if (!mResult.containsKey(tag)) {
            mResult.put(tag, new ArrayList<XmlResult>());
        }

        mResult.get(tag).add(result);
    }

    public List<XmlResult> getList(String tag) {
        return has(tag) ? mResult.get(tag) : new ArrayList<XmlResult>();
    }

    public XmlResult get(String tag) {
        return has(tag) ? getList(tag).get(0) : sNull;
    }

    public boolean has(String tag) {
        return mResult != null && mResult.containsKey(tag);
    }

    public String getAttr(String name) {
        return mAttrs == null ? null : mAttrs.get(name);
    }
}
