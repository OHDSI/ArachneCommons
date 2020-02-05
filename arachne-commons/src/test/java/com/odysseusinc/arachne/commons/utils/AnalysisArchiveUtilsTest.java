package com.odysseusinc.arachne.commons.utils;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odysseusinc.arachne.commons.api.v1.dto.CommonAnalysisType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class AnalysisArchiveUtilsTest {

    @Test
    public void getArchiveFileName() {

        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 3, 13, 35, 20);
        assertEquals("c-to_be_or_not_to_be-20200103.zip", AnalysisArchiveUtils.getArchiveFileName(CommonAnalysisType.COHORT, "to be, or<>  (not to be)!", dateTime));
    }


    @Test
    public void getAnalysisNameByProperties() {

        assertEquals("analysis-name", AnalysisArchiveUtils.getAnalysisName(Collections.singletonMap("name", "analysis-name")));

        assertEquals(StringUtils.EMPTY, AnalysisArchiveUtils.getAnalysisName((Map<String, ? extends Object>) null));
        assertEquals(StringUtils.EMPTY, AnalysisArchiveUtils.getAnalysisName(Collections.emptyMap()));
        assertEquals(StringUtils.EMPTY, AnalysisArchiveUtils.getAnalysisName(Collections.singletonMap("wrong-key", "analysis-name")));
    }

    @Test
    public void getAnalysisNameByJson() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree("{\"name\": \"analysis-name\"}");
        assertEquals("analysis-name", AnalysisArchiveUtils.getAnalysisName(node));

        node = mapper.readTree("{\"wrong-field\": \"analysis-name\"}");
        assertEquals(StringUtils.EMPTY, AnalysisArchiveUtils.getAnalysisName(node));

        node = null;
        assertEquals(StringUtils.EMPTY, AnalysisArchiveUtils.getAnalysisName(node));
    }
}