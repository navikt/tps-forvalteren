package no.nav.tps.vedlikehold.application.local;

import no.nav.tps.vedlikehold.application.local.config.LocalApplicationConfig;
import org.springframework.boot.SpringApplication;

/**
 *  @author Kristian Kyvik (Visma Consulting).
 */
public class ApplicationStarter {

    public static void main(String[] args) {

        SpringApplication.run(LocalApplicationConfig.class, args);
    }
}
