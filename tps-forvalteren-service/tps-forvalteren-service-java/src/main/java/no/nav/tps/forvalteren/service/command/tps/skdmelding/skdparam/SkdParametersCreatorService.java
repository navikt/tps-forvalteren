package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.UNKNOWN_SKD_PARAMETER_STRATEGY;

@Service
public class SkdParametersCreatorService {

    @Autowired
    private List<SkdParametersStrategy> skdParametersStrategies;

    public Map<String, String> execute(TpsSkdRequestMeldingDefinition skdMeldingDefinition, Person person) {
        for (SkdParametersStrategy strategy : skdParametersStrategies) {
            if (strategy.isSupported(skdMeldingDefinition.getSkdParametersCreator())) {
                return strategy.execute(person);
            }
        }
        throw new HttpInternalServerErrorException(UNKNOWN_SKD_PARAMETER_STRATEGY, "api/v1/testdata/saveTPS");
    }
}

