package com.odysseusinc.arachne.commons.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odysseusinc.arachne.commons.api.v1.dto.CommonAnalysisType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class AnalysisArchiveUtilsTest {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void shouldGetArchiveFileName() {

        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 3, 13, 35, 20);
        assertThat(
                AnalysisArchiveUtils.getArchiveFileName(CommonAnalysisType.COHORT, "to be, or<>  (not to be)!", dateTime),
                is(equalTo("c-to_be_or_not_to_be-20200103.zip"))
        );
    }


    @Test
    public void shouldGetAnalysisNameByProperties() {

        assertEquals("analysis-name", AnalysisArchiveUtils.getAnalysisName(Collections.singletonMap("name", "analysis-name")));

        assertThat(
                AnalysisArchiveUtils.getAnalysisName((Map<String, Object>) null),
                is(equalTo(StringUtils.EMPTY))
        );
        assertThat(
                AnalysisArchiveUtils.getAnalysisName(Collections.emptyMap()),
                is(equalTo(StringUtils.EMPTY))
        );
        assertThat(
                AnalysisArchiveUtils.getAnalysisName(Collections.singletonMap("wrong-key", "analysis-name")),
                is(equalTo(StringUtils.EMPTY))
        );
    }

    @Test
    public void shouldGetAnalysisNameByJsonForWrongArguments() {

        assertThat(
                AnalysisArchiveUtils.getAnalysisName(Collections.singletonMap("name", "analysis-name")),
                is(equalTo("analysis-name"))
        );

    }

    @Test
    public void shouldNotGetAnalysisNameByJsonForWrongArguments() throws Exception {

        JsonNode node = MAPPER.readTree("{\"wrong-field\": \"analysis-name\"}");
        assertThat(
                AnalysisArchiveUtils.getAnalysisName(node),
                is(equalTo(StringUtils.EMPTY))
        );

        node = null;
        assertThat(
                AnalysisArchiveUtils.getAnalysisName(node),
                is(equalTo(StringUtils.EMPTY))
        );
    }
}