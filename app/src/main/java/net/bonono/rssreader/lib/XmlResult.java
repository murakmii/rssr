package net.bonono.rssreader.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlResult {
    private static final XmlResult sNull = new XmlResult(null);

    private HashMap<String, List<XmlResult>> mResult;
    private String mText;

    public XmlResult() {
        mResult = new HashMap<>();
    }

    public XmlResult(String text) {
        mText = text;
    }

    public String text() {
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
}
