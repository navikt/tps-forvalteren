package no.nav.tps.forvalteren;

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

        SpringApplication.run(ApplicationStarter.class, args);
    }
}
