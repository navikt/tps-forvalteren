package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam;

import java.util.List;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.UNKNOWN_SKD_PARAMETER_STRATEGY;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkdParametersCreatorService {

    private List<SkdParametersStrategy> skdParametersStrategies;

    @Autowired
    public SkdParametersCreatorService(List<SkdParametersStrategy> skdParametersStrategies) {
        this.skdParametersStrategies = skdParametersStrategies;
    }

    public SkdMeldingTrans1 execute(TpsSkdRequestMeldingDefinition skdMeldingDefinition, Person person) {
        for (SkdParametersStrategy strategy : skdParametersStrategies) {
            if (strategy.isSupported(skdMeldingDefinition.getSkdParametersCreator())) {
                return strategy.execute(person);
            }
        }
        throw new HttpInternalServerErrorException(UNKNOWN_SKD_PARAMETER_STRATEGY, "api/v1/testdata/saveTPS");
    }
}

