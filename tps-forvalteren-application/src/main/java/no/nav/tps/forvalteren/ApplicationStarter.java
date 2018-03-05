package no.nav.tps.forvalteren;

import no.nav.modig.testcertificates.TestCertificates;

import javax.security.auth.message.config.AuthConfigFactory;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationStarter {

    public static void main(String[] args) {

        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }

        TestCertificates.setupKeyAndTrustStore();

        SpringApplication.run(ApplicationStarter.class, args);
    }
}
