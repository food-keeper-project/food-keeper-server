package com.foodkeeper.foodkeeperserver.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {

    @Bean(name = "fcmExecutor")
    public Executor fcmExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean(name = "imageExecutor")
    public Executor imageExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean(name = "clovaExecutor")
    public Executor clovaExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
