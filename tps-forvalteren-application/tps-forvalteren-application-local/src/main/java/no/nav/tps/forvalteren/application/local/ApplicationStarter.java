package no.nav.tps.forvalteren.application.local;

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

        System.setProperty(ModigSecurityConstants.SYSTEMUSER_USERNAME, "srvtps-forvalter_u");
        System.setProperty(ModigSecurityConstants.SYSTEMUSER_PASSWORD, "1QbjePTFkb7EOhL");

        SpringApplication.run(LocalApplicationConfig.class, args);
    }
}
