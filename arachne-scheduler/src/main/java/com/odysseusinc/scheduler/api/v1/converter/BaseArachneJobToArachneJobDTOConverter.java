package com.odysseusinc.scheduler.api.v1.converter;

import com.odysseusinc.arachne.commons.converter.BaseConvertionServiceAwareConverter;
import com.odysseusinc.scheduler.api.v1.dto.ArachneJobDTO;
import com.odysseusinc.scheduler.model.ArachneJob;
import java.sql.Date;
import java.time.ZoneOffset;

public abstract class BaseArachneJobToArachneJobDTOConverter<S extends ArachneJob, T extends ArachneJobDTO> extends BaseConvertionServiceAwareConverter<S, T> {

    protected final void convert(S s, T dto) {

        dto.setId(s.getId());
        dto.setClosed(s.getClosed());
        dto.setEnabled(s.getEnabled());
        dto.setFrequency(s.getFrequency());
        dto.setRecurringTimes(s.getRecurringTimes());
        dto.setRecurringUntilDate(s.getRecurringUntilDate());
        dto.setStartDate(s.getStartDate());
        dto.setWeekDays(s.getWeekDays());
        convertJob(s, dto);
    }

    protected abstract void convertJob(S source, T target);

}
