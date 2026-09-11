package pl.advansoft.aachen.notification;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NotificationApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
