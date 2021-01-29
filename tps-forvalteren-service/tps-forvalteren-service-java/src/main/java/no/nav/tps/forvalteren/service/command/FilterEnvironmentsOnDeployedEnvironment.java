package no.nav.tps.forvalteren.service.command;

import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FilterEnvironmentsOnDeployedEnvironment {

    @Value("${tpsf.environment.name}")
    private String deployedEnvironment;

    public Set<String> execute(Set<String> environments) {
        switch (deployedEnvironment) {
        case "prod":
            return EnvironmentsFilter.create()
                    .include("p*")
                    .filter(environments);
        case "dev":
        default:
            return EnvironmentsFilter.create()
                    .include("u*")
                    .include("t*")
                    .include("q*")
                    .filter(environments);
        }
    }
}
