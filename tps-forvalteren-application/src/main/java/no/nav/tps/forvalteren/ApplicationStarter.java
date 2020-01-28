package no.nav.tps.forvalteren;

import static java.util.Objects.isNull;

import java.util.Map;
import javax.security.auth.message.config.AuthConfigFactory;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import no.nav.fregpropertyreader.PropertyReader;
import no.nav.tps.forvalteren.provider.web.config.ApplicationConfig;

@SpringBootApplication
public class ApplicationStarter {

    public static void main(String[] args) {

        if (isNull(AuthConfigFactory.getFactory())) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }

        Map<String, Object> properties = PropertyReader.builder()
                .readSecret("SPRING_DATASOURCE_USERNAME", "/var/run/secrets/nais.io/db/username")
                .readSecret("SPRING_DATASOURCE_PASSWORD", "/var/run/secrets/nais.io/db/password")
                .readSecret("SPRING_DATASOURCE_URL", "/var/run/secrets/nais.io/dbPath/jdbc_url")
                .build();

        new SpringApplicationBuilder()
                .sources(ApplicationConfig.class)
                .properties(properties)
                .run(args);
    }
}
