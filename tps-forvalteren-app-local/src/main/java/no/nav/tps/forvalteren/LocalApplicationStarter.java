package no.nav.tps.forvalteren;

import static java.util.Objects.isNull;

import javax.security.auth.message.config.AuthConfigFactory;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LocalApplicationStarter {

    public static void main(String[] args) {

        if (isNull(AuthConfigFactory.getFactory())) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }

        new SpringApplicationBuilder()
                .sources(LocalApplicationStarter.class)
                .profiles("local")
                .run(args);
    }
}
