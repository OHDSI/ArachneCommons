package com.odysseusinc.arachne.commons.utils;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

/**
 * @author vkoulakov
 * @since 8/1/19.
 */
public class QuoteUtilsTest {

    @Test
    public void splitAndKeep() {

        List<String> result = QuoteUtils.splitAndKeep("a;b;c", ";");
        assertThat(result, hasSize(5));
        assertThat(result, contains("a", ";", "b", ";", "c"));
    }

    @Test
    public void replaceWithConcat() {

        String result = QuoteUtils.replaceWithConcat("select 'a''b''c' as col");
        assertThat(result, is(equalTo("select CONCAT('a','\\'','b','\\'','c') as col")));
        String result2 = QuoteUtils.replaceWithConcat("select 'test' as col");
        assertThat(result2, is(equalTo("select 'test' as col")));
        String result3 = QuoteUtils.replaceWithConcat("IF OBJECT_ID('@eventsTable', 'U') IS NOT NULL DROP TABLE @eventsTable;");
        assertThat(result3, is(equalTo("IF OBJECT_ID('@eventsTable', 'U') IS NOT NULL DROP TABLE @eventsTable;")));
    }

    @Test
    public void escapeSingleQuotes() {

        String result = QuoteUtils.escapeSingleQuotes("select 'a''b''c' as col");
        assertThat(result, is(equalTo("select 'a\\'b\\'c' as col")));
    }
}