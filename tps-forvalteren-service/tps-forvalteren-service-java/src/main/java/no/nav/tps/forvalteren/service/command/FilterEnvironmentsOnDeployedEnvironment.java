package no.nav.tps.forvalteren.service.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilterEnvironmentsOnDeployedEnvironment {

    @Value("${environment.class}")
    private String deployedEnvironment;

    public Set<String> execute(Set<String> environments) {
        char env = deployedEnvironment.charAt(0);
        switch (env) {
        case 'u':
            return EnvironmentsFilter.create()
                    .include("u*")
                    .filter(environments);
        case 't':
            return EnvironmentsFilter.create()
                    .include("t*")
                    .filter(environments);
        case 'q':
            return EnvironmentsFilter.create()
                    .include("q*")
                    .filter(environments);
        case 'p':
            return EnvironmentsFilter.create()
                    .include("p*")
                    .filter(environments);
        default:
            return EnvironmentsFilter.create()
                    .include("u*")
                    .filter(environments);
        }
    }
}
