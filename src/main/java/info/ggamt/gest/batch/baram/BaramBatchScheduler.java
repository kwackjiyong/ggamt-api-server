package info.ggamt.gest.batch.baram;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class BaramBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job gthrCurrentHistoryJob;
    private final Job gthrCurrentMacroJob;

    public BaramBatchScheduler(JobLauncher jobLauncher, Job gthrCurrentHistoryJob, Job gthrCurrentMacroJob) {
        this.jobLauncher = jobLauncher;
        this.gthrCurrentHistoryJob = gthrCurrentHistoryJob;
        this.gthrCurrentMacroJob = gthrCurrentMacroJob;
    }

    // 매시간마다 실행되는 스케줄링 작업
    @Scheduled(cron = "0 1 * * * ?")
    public void runBatchBaramCurrentHistoryJob() throws Exception {
        System.out.println("Batch Job 시작 시간: " + new Date());

        // Job 파라미터 설정 (예: 시간 단위로 실행할 경우마다 새로운 JobInstance를 위해)
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("startTime", new Date());

        jobLauncher.run(gthrCurrentHistoryJob, jobParametersBuilder.toJobParameters());

        System.out.println("Batch Job 종료 시간: " + new Date());
    }

    @Scheduled(cron = "0 10-59/10 * * * ?")
    public void runBatchBaramCurrentMacroJob() throws Exception {
        System.out.println("Batch Job 시작 시간: " + new Date());

        // Job 파라미터 설정 (예: 시간 단위로 실행할 경우마다 새로운 JobInstance를 위해)
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("startTime", new Date());

        jobLauncher.run(gthrCurrentMacroJob, jobParametersBuilder.toJobParameters());

        System.out.println("Batch Job 종료 시간: " + new Date());
    }
}
