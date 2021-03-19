package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@Service
@RequiredArgsConstructor
public class SendSkdMeldingTilGitteMiljoer {

    private final SendEnSkdMelding sendEnSkdMelding;
    private final FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public Map<String, String> execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Set<String> environments) {

        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);
        Map<String, String> responseSkdMeldinger = new HashMap<>(envToCheck.size());

        for (String env : envToCheck) {
            String responsMelding = sendEnSkdMelding.sendSkdMelding(skdMelding, skdRequestMeldingDefinition, env);
            responseSkdMeldinger.put(env, responsMelding);
        }
        return responseSkdMeldinger;
    }

    public String execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, String environment) {

        return sendEnSkdMelding.sendSkdMelding(skdMelding, skdRequestMeldingDefinition, environment);
    }
}
