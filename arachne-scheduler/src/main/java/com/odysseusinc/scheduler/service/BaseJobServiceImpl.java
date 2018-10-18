package com.odysseusinc.scheduler.service;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.odysseusinc.scheduler.exception.JobNotFoundException;
import com.odysseusinc.scheduler.model.ArachneJob;
import com.odysseusinc.scheduler.model.ScheduledTask;
import com.odysseusinc.scheduler.repository.ArachneJobRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseJobServiceImpl<T extends ArachneJob> implements BaseJobService<T> {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected static final String ADDING_NEW_TO_SCHEDULER_LOG = "Scheduling new job";
    protected static final String REMOVING_FROM_SCHEDULER_LOG = "Removing job with id='{}' from scheduler";
    protected static final String NOT_EXISTS_EXCEPTION = "Job for analysis with id='%s' is not exist";

    private final TaskScheduler taskScheduler;
    private final CronDefinition cronDefinition;
    private final ArachneJobRepository<T> jobRepository;

    private final Map<Long, ScheduledFuture> taskInWork = new ConcurrentHashMap<>();

    protected BaseJobServiceImpl(TaskScheduler taskScheduler,
                                 CronDefinition cronDefinition,
                                 ArachneJobRepository<T> jobRepository) {

        this.taskScheduler = taskScheduler;
        this.cronDefinition = cronDefinition;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T createJob(T job) {

        beforeCreate(job);
        job.setId(null);
        final boolean isClosed = isClosed(job);
        job.setClosed(isClosed);
        final T saved = jobRepository.save(job);
        if (!isClosed && saved.getEnabled()) {
            addToScheduler(saved);
        }
        afterCreate(job);
        return saved;
    }

    @Override
    @Transactional
    public T updateJob(T job) throws JobNotFoundException {

        final T exists = jobRepository.findOne(job.getId());
        if (exists == null) {
            final String message = String.format(NOT_EXISTS_EXCEPTION, job.getId());
            removeFromScheduler(job.getId());
            throw new JobNotFoundException(message);
        }
        beforeUpdate(exists, job);
        boolean updatedByUser = job.getExecutedTimes() == 0 && Objects.isNull(job.getLastExecutedAt());
        if (updatedByUser) {
            exists.setEnabled(job.getEnabled());
            exists.setStartDate(job.getStartDate());
            exists.setCron(job.getCron());
            exists.setWeekDays(job.getWeekDays());
            exists.setFrequency(job.getFrequency());
            exists.setRecurringTimes(job.getRecurringTimes());
            exists.setRecurringUntilDate(job.getRecurringUntilDate());
            exists.setExecutedTimes(0);
            exists.setLastExecutedAt(null);
            updateAdditionalFields(exists, job);
        } else {
            exists.setExecutedTimes(job.getExecutedTimes());
            exists.setLastExecutedAt(job.getLastExecutedAt());
        }
        final boolean isClosed = isClosed(exists);
        exists.setClosed(isClosed);
        // If job is finished, automatically turn it off
        if (isClosed && !updatedByUser) {
            exists.setEnabled(false);
        }
        if (isClosed || !exists.getEnabled()){
            removeFromScheduler(job.getId());
        } else if (updatedByUser) {
            removeFromScheduler(job.getId());
            addToScheduler(exists);
        }
        T updated = jobRepository.save(exists);
        afterUpdate(updated);
        return updated;
    }

    @Override
    public void reassignAllJobs() {
        List<T> jobs = jobRepository.findAllByEnabledTrueAndIsClosedFalse();
        jobs.forEach(job -> {
            removeFromScheduler(job.getId());
            addToScheduler(job);
        });
    }

    @Override
    @Transactional
    public void delete(T job) {

        jobRepository.delete(job);
    }

    protected void beforeCreate(T job) {
    }

    protected void afterCreate(T job) {
    }

    protected void beforeUpdate(T exists, T updated) {
    }

    protected abstract void updateAdditionalFields(T exists, T job);

    protected void afterUpdate(T job) {
    }

    protected boolean isClosed(T job) {

        boolean result = false;
        if (job.getRecurringTimes() > 0) {
            result = job.getExecutedTimes() >= job.getRecurringTimes();
        }
        final CronParser parser = new CronParser(cronDefinition);
        final ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(job.getCron()));
        final ZonedDateTime lastExecuted = ZonedDateTime.now();
        final ZonedDateTime nextExecution = executionTime.nextExecution(lastExecuted);
        final Date recurringUntilDate = job.getRecurringUntilDate();
        if (recurringUntilDate != null) {
            final ZonedDateTime recurringUntil = getZonedDateTime(recurringUntilDate);
            final long diff = ChronoUnit.SECONDS.between(nextExecution, recurringUntil);
            result = result || diff <= 0;
        }
        return result;
    }

    private ZonedDateTime getZonedDateTime(Date date) {

        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    protected void addToScheduler(T job) {

        LOGGER.debug(ADDING_NEW_TO_SCHEDULER_LOG);
        removeFromScheduler(job.getId());
        final ScheduledFuture<?> runningTask = taskScheduler.schedule(
                buildScheduledTask(job),
                new CronTrigger(job.getCron())
        );
        taskInWork.put(job.getId(), runningTask);
    }

    protected void removeFromScheduler(Long id) {

        LOGGER.debug(REMOVING_FROM_SCHEDULER_LOG, id);
        final ScheduledFuture scheduledFuture = taskInWork.remove(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    protected abstract ScheduledTask<T> buildScheduledTask(T job);

}
