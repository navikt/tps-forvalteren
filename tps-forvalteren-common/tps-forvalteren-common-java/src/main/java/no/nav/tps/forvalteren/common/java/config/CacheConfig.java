package no.nav.tps.forvalteren.common.java.config;

import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_FASIT = "Fasit";

    @Bean
    public Cache cacheFasit() {
        return new GuavaCache(CACHE_FASIT, CacheBuilder.newBuilder()
                .expireAfterWrite(4, TimeUnit.HOURS)
                .build());
    }
}