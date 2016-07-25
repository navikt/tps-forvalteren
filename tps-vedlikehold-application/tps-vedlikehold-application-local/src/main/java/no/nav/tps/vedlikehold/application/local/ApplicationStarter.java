package no.nav.tps.vedlikehold.application.local;

import no.nav.modig.core.context.ModigSecurityConstants;
import no.nav.modig.testcertificates.TestCertificates;
import org.springframework.boot.SpringApplication;

/**
 *  @author Kristian Kyvik (Visma Consulting).
 */
public class ApplicationStarter {

    public static void main(String[] args) {
        TestCertificates.setupKeyAndTrustStore();

        System.setProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME, "srvtps-vedlikeho_u");
        System.setProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD, "DxXwbjQfV3C7K9d");

        SpringApplication.run(LocalApplicationConfig.class, args);
    }
}
