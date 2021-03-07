package no.nav.tps.forvalteren.common.java.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_FASIT = "Fasit";
    public static final String CACHE_AVSPILLER = "Avspiller";
    public static final String CACHE_TPSCONFIG = "TpsConfig";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_FASIT, CACHE_AVSPILLER, CACHE_TPSCONFIG);
    }
}