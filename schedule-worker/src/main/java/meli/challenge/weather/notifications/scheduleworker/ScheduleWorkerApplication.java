package meli.challenge.weather.notifications.scheduleworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableCaching
@EnableRetry
public class ScheduleWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleWorkerApplication.class, args);
	}

}
