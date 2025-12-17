package com.example.demo.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@ConditionalOnProperty(value = "batch.send.morning.emails.tasklet.active", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class SendMorningEmailsTaskletConfiguration {

    private static final String JOB_NAME = "sendMorningEmailsTaskletJob";
    private static final String STEP_NAME = "sendMorningEmailsTaskletStep";

    private final JobLauncher jobLauncher;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SendMorningEmailsBatchTasklet sendMorningEmailsBatchTasklet;

    @Bean
    public Job sendMorningEmailsTaskletJob(Step sendMorningEmailsTaskletStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .start(sendMorningEmailsTaskletStep)
                .build();
    }

    @Bean
    public Step sendMorningEmailsTaskletStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(sendMorningEmailsBatchTasklet)
                .build();
    }

    @Scheduled(cron = "${batch.send.morning.emails.cron.expression}", zone = "${batch.default.zone}")
    public void perform() {
        log.info("Attempting to start {}...", JOB_NAME);
        try {
            // Using a parameter that identifies the execution window (e.g., current date).
            // This prevents concurrent runs within the same day if multiple pods trigger the job.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("runTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH")))
                    .toJobParameters();
            jobLauncher.run(sendMorningEmailsTaskletJob(sendMorningEmailsTaskletStep()), jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.info("{} is already running.", JOB_NAME);
        } catch (Exception e) {
            log.error("Error while running {}", JOB_NAME, e);
        }
    }
}
