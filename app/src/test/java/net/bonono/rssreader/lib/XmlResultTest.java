package net.bonono.rssreader.lib;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class XmlResultTest {
    @Test
    public void initializeTextAsNull() throws Exception {
        assertThat(new XmlResult().getText(), is(nullValue()));
    }

    @Test
    public void initializeTextByArgument() throws Exception {
        String expected = "test";
        XmlResult sut = new XmlResult();
        sut.setText(expected);

        assertThat(sut.getText(), equalTo(expected));
    }

    @Test
    public void canAddResult() throws Exception {
        XmlResult sut = new XmlResult(), result = new XmlResult();
        sut.add("test", result);

        assertThat(sut.get("test"), sameInstance(result));
    }

    @Test
    public void canAddSomeResults() throws Exception {
        XmlResult sut = new XmlResult(), result1 = new XmlResult(), result2 = new XmlResult();
        sut.add("test1", result1);
        sut.add("test2", result2);

        assertThat(sut.get("test1"), sameInstance(result1));
        assertThat(sut.get("test2"), sameInstance(result2));
    }

    @Test
    public void canGetResultsAsList() throws Exception {
        XmlResult sut = new XmlResult(), a1 = new XmlResult(), a2 = new XmlResult();
        sut.add("test", a1);
        sut.add("test", a2);

        assertThat(sut.getList("test").get(0), sameInstance(a1));
        assertThat(sut.getList("test").get(1), sameInstance(a2));
    }

    @Test
    public void returnNullObjectIfResultNothing() throws Exception {
        XmlResult sut = new XmlResult();
        XmlResult got = sut.get("test1");

        assertThat(got, notNullValue());
        assertThat(sut.get("test2"), sameInstance(got));
    }
}
