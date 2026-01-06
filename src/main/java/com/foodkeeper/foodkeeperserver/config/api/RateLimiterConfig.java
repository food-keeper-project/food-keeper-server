package com.foodkeeper.foodkeeperserver.config.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.caffeine.Bucket4jCaffeine;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RateLimiterConfig {

    @Value("${rate-limiter.capacity:5}")
    private int capacity;

    @Value("${rate-limiter.refill-token-amount:5}")
    private int refillTokenAmount;

    @Value("${rate-limiter.refill-duration-seconds:10}")
    private long refillDurationSeconds;

    @Value("${rate-limiter.cache-max-size:10000}")
    private long cacheMaxSize;

    @Bean
    public ProxyManager<String> proxyManager() {
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .maximumSize(cacheMaxSize);

        return Bucket4jCaffeine.<String>builderFor(caffeineBuilder).build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(
                        limit ->
                                limit.capacity(capacity)
                                        .refillIntervally(
                                                refillTokenAmount,
                                                Duration.ofSeconds(refillDurationSeconds)))
                .build();
    }
}
