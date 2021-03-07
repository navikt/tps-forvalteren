package no.nav.tps.forvalteren.service.command.tpsconfig;

import java.util.Set;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.config.TpsPropertiesConfig;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;

@Service
@RequiredArgsConstructor
public class GetEnvironments {

    private final TpsPropertiesConfig tpsProperties;
    private final FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public Set<String> getEnvironments() {
        Set<String> environments = tpsProperties.getEnvironments();
        environments.remove("q6");
        environments.remove("q0");

        return filterEnvironmentsOnDeployedEnvironment.execute(environments);
    }
}
