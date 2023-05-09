package br.com.quartz.demo.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

import br.com.quartz.demo.job.TaskJob;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuartzConfig {

	Logger LOGGER = LoggerFactory.getLogger(QuartzConfig.class);

	private static final String TRIGGER_NAME = "Quartz_Trigger";
	private static final String TRIGGER_GROUP_NAME = "Quartz_Trigger_Group";
	private static final String JOB_DETAIL_NAME = "Quartz_Job_Detail";
	private static final String JOB_DETAIL_DESCRIPTION = "Invoke ";

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	QuartzPropertiesConfig quartzPropertiesConfig;

	@Bean
	SpringBeanJobFactory createSpringBeanJobFactory() {
		LOGGER.info("Configuring springBeanJobFactory");
		return new SpringBeanJobFactory() {
			@Override
			protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
				final Object job = super.createJobInstance(bundle);
				applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
				return job;
			}
		};
	}

	@Bean
	public SchedulerFactoryBean createSchedulerFactory(SpringBeanJobFactory springBeanJobFactory,
			Trigger trigger) {
		LOGGER.info("Configuring schedulerFactoryBean");
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setAutoStartup(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		schedulerFactory.setTriggers(trigger);

		springBeanJobFactory.setApplicationContext(applicationContext);
		schedulerFactory.setJobFactory(springBeanJobFactory);

		return schedulerFactory;
	}

	@Bean
	@ConditionalOnExpression("'${quartz.factory.simpleTrigger.enabled}'=='true'")
	public SimpleTriggerFactoryBean createSimpleTriggerFactoryBean(JobDetail jobDetail) {
		LOGGER.info("Configuring simpleTriggerFactoryBean");
		SimpleTriggerFactoryBean simpleTriggerFactory = new SimpleTriggerFactoryBean();
		simpleTriggerFactory.setJobDetail(jobDetail);
		simpleTriggerFactory.setStartDelay(quartzPropertiesConfig.getSimpleTriggerStartDelay() * 1000);
		simpleTriggerFactory.setRepeatInterval(quartzPropertiesConfig.getSimpleTriggerRepeatInterval() * 1000);
		simpleTriggerFactory.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		simpleTriggerFactory.setName(TRIGGER_NAME);

		return simpleTriggerFactory;
	}

	@Bean
	@ConditionalOnExpression("'${quartz.factory.cronTrigger.enabled}'=='true'")
	public CronTriggerFactoryBean createCronTriggerFactoryBean(JobDetail jobDetail) {
		LOGGER.info("Configuring cronTriggerFactoryBean");
		CronTriggerFactoryBean cronTriggerFactory = new CronTriggerFactoryBean();
		cronTriggerFactory.setJobDetail(jobDetail);
		cronTriggerFactory.setName(TRIGGER_NAME);
		cronTriggerFactory.setGroup(TRIGGER_GROUP_NAME);
		cronTriggerFactory.setStartDelay(quartzPropertiesConfig.getSimpleTriggerStartDelay() * 1000);
		cronTriggerFactory.setCronExpression(quartzPropertiesConfig.getCronTriggerCron());

		return cronTriggerFactory;
	}

	@Bean
	public JobDetailFactoryBean createJobDetailFactoryBean() {
		LOGGER.info("Configuring jobDetailFactoryBean");
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(TaskJob.class);
		jobDetailFactory.setName(JOB_DETAIL_NAME);
		jobDetailFactory.setDescription(JOB_DETAIL_DESCRIPTION
				.concat(TaskJob.class.getSimpleName()));
		jobDetailFactory.setDurability(true);

		return jobDetailFactory;
	}

}
