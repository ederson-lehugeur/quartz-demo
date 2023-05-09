package br.com.quartz.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuartzPropertiesConfig {

	@Value("${quartz.factory.simpleTrigger.startDelay}")
	Integer simpleTriggerStartDelay;

	@Value("${quartz.factory.simpleTrigger.repeatInterval}")
	Integer simpleTriggerRepeatInterval;

	@Value("${quartz.factory.cronTrigger.startDelay}")
	Integer cronTriggerStartDelay;

	@Value("${quartz.factory.cronTrigger.cron}")
	String cronTriggerCron;

	public Integer getSimpleTriggerStartDelay() {
		return simpleTriggerStartDelay;
	}

	public Integer getSimpleTriggerRepeatInterval() {
		return simpleTriggerRepeatInterval;
	}

	public Integer getCronTriggerStartDelay() {
		return cronTriggerStartDelay;
	}

	public String getCronTriggerCron() {
		return cronTriggerCron;
	}

}
