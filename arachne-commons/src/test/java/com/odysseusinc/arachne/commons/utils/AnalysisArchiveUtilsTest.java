package com.odysseusinc.arachne.commons.utils;

import static org.junit.Assert.*;

import com.odysseusinc.arachne.commons.api.v1.dto.CommonAnalysisType;
import java.time.LocalDateTime;
import org.junit.Test;

public class AnalysisArchiveUtilsTest {

    @Test
    public void getArchiveFileName() {

        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 3, 13, 35, 20);
        assertEquals("c-to_be_or_not_to_be-20200103133520.zip", AnalysisArchiveUtils.getArchiveFileName(CommonAnalysisType.COHORT, "to be, or<>  (not to be)!", dateTime));
    }

}