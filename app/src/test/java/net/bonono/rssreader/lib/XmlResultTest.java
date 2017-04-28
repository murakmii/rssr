package net.bonono.rssreader.lib;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlResultTest {
    @Test
    public void initializeTextAsNull() throws Exception {
        assertNull(new XmlResult().text());
    }

    @Test
    public void initializeTextByArgument() throws Exception {
        String expected = "test";
        assertEquals(expected, new XmlResult(expected).text());
    }

    @Test
    public void canAddResult() throws Exception {
        XmlResult sut = new XmlResult(), result = new XmlResult();
        sut.add("test", result);

        assertSame(result, sut.get("test"));
    }

    @Test
    public void canAddSomeResults() throws Exception {
        XmlResult sut = new XmlResult(), result1 = new XmlResult(), result2 = new XmlResult();
        sut.add("test1", result1);
        sut.add("test2", result2);

        assertSame(result1, sut.get("test1"));
        assertSame(result2, sut.get("test2"));
    }

    @Test
    public void canGetResultsAsList() throws Exception {
        XmlResult sut = new XmlResult(), a1 = new XmlResult(), a2 = new XmlResult();
        sut.add("test", a1);
        sut.add("test", a2);

        assertSame(a1, sut.getList("test").get(0));
        assertSame(a2, sut.getList("test").get(1));
    }

    @Test
    public void returnNullObjectIfResultNothing() throws Exception {
        XmlResult sut = new XmlResult();
        XmlResult got = sut.get("test1");

        assertNotNull(got);
        assertSame(got, sut.get("test2"));
    }
}
