package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@Service
@RequiredArgsConstructor
public class SendSkdMeldingTilGitteMiljoer {

    private final SendEnSkdMelding sendEnSkdMelding;

    public Map<String, String> execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Set<String> environments) {

        return environments.parallelStream()
                        .collect(Collectors.toMap(env -> env,
                                env -> sendEnSkdMelding.sendSkdMelding(skdMelding, skdRequestMeldingDefinition, env)));
    }

    public String execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, String environment) {

        return sendEnSkdMelding.sendSkdMelding(skdMelding, skdRequestMeldingDefinition, environment);
    }
}