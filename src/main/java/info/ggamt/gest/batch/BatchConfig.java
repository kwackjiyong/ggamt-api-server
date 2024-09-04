package info.ggamt.gest.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import info.ggamt.gest.service.baram.BaramService;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    @Autowired
    private BaramService baramService;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job gthrCurrentHistoryJob(Step gthrCurrentHistoryStep) {
        return new JobBuilder("gthrCurrentHistoryJob", jobRepository)
            .start(gthrCurrentHistoryStep)
            .build();
    }

    @Bean
    public Step gthrCurrentHistoryStep(Tasklet gthrCurrentHistoryTasklet) {
        return new StepBuilder("gthrCurrentHistoryStep", jobRepository)
            .tasklet(gthrCurrentHistoryTasklet, transactionManager)
            .build();
    }

    @Bean
    public Tasklet gthrCurrentHistoryTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Spring Batch gthrCurrentHistoryJob 실행 중...");
            //동시접속자수 수집
            baramService.gthrCurrentUser();
            return RepeatStatus.FINISHED;
        };
    }
}