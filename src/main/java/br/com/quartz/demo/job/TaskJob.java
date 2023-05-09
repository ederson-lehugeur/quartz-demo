package br.com.quartz.demo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.quartz.demo.service.TaskService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskJob implements Job {

	Logger LOGGER = LoggerFactory.getLogger(TaskJob.class);

	@Autowired
	TaskService taskService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Job -> {}, description -> {}, fired -> {}",
				context.getJobDetail().getKey().getName(),
				context.getJobDetail().getDescription(),
				context.getFireTime());
		taskService.doSomething();
        LOGGER.info("Next job scheduled to {}", context.getNextFireTime());
	}

}
