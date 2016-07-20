package no.nav.tps.vedlikehold.application.local;

import no.nav.modig.testcertificates.TestCertificates;
import org.springframework.boot.SpringApplication;

/**
 *  @author Kristian Kyvik (Visma Consulting).
 */
public class ApplicationStarter {

    public static void main(String[] args) {
        TestCertificates.setupKeyAndTrustStore();

        SpringApplication.run(LocalApplicationConfig.class, args);
    }
}
