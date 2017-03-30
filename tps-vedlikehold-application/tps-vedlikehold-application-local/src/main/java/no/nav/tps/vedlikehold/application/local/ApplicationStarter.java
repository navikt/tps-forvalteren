package no.nav.tps.vedlikehold.application.local;

import no.nav.modig.core.context.ModigSecurityConstants;
import no.nav.modig.testcertificates.TestCertificates;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.SpringApplication;

import javax.security.auth.message.config.AuthConfigFactory;

public class ApplicationStarter {

    public static void main(String[] args) {

        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }

        TestCertificates.setupKeyAndTrustStore();

        System.setProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME, "srvtps-vedlikeho_u");
        System.setProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD, "DxXwbjQfV3C7K9d");

        SpringApplication.run(LocalApplicationConfig.class, args);
    }
}
