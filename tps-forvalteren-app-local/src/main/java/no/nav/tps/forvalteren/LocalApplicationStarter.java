package no.nav.tps.forvalteren;

import static java.util.Objects.isNull;

import javax.security.auth.message.config.AuthConfigFactory;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import no.nav.tps.forvalteren.provider.web.config.ApplicationWebConfig;

@SpringBootApplication
public class LocalApplicationStarter {

    public static void main(String[] args) {

        if (isNull(AuthConfigFactory.getFactory())) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }

        new SpringApplicationBuilder()
                .sources(ApplicationWebConfig.class)
                .profiles("local")
                .run(args);
    }
}
