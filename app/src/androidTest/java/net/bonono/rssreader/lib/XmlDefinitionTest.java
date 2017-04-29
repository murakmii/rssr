package net.bonono.rssreader.lib;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class XmlDefinitionTest {
    @Test
    public void initializeDefinition() throws Exception {
        XmlDefinition sut = XmlDefinition.forTag("test");

        assertThat(sut.getTag(), equalTo("test"));
        assertThat(sut.acceptText(), is(false));
    }

    @Test
    public void initializeDefinitionForText() throws Exception  {
        XmlDefinition sut = XmlDefinition.forText("test");

        assertThat(sut.getTag(), equalTo("test"));
        assertThat(sut.acceptText(), is(true));
    }

    @Test
    public void canParseTag() throws Exception {
        XmlDefinition sut = XmlDefinition.forTag("test");
        String xml = "<test></test>";
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        assertThat(XmlDefinition.rootOf(sut).parse(is), instanceOf(XmlResult.class));
    }

    @Test
    public void canParseChildren() throws Exception {
        XmlDefinition sut = XmlDefinition.forTag("test");
        sut.nest(XmlDefinition.forText("child1"));
        sut.nest(XmlDefinition.forText("child2"));

        String xml = "<test><child1>text1</child1><child2>text2</child2></test>";
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        XmlResult result = XmlDefinition.rootOf(sut).parse(is).get("test");

        assertThat(result.get("child1").getText(), equalTo("text1"));
        assertThat(result.get("child2").getText(), equalTo("text2"));
    }

    @Test
    public void canParseChildrenHasSameName() throws Exception {
        XmlDefinition sut = XmlDefinition.forTag("test");
        sut.nest(XmlDefinition.forText("child"));

        String xml = "<test><child>text1</child><child>text2</child></test>";
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        XmlResult result = XmlDefinition.rootOf(sut).parse(is).get("test");

        assertThat(result.getList("child").get(0).getText(), equalTo("text1"));
        assertThat(result.getList("child").get(1).getText(), equalTo("text2"));
    }
}
