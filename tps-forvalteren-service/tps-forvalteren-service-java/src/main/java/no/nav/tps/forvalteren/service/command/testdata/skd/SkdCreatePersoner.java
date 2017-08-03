package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class SkdCreatePersoner {

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdParametersCreatorService skdParametersCreatorService;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private GetSkdMeldingByName getSkdMeldingByName;

    public void execute(String skdMeldingNavn, List<Person> personer, List<String> environments){
        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = getSkdMeldingByName.execute(skdMeldingNavn).get();

        for(Person person : personer){
                Map<String,String> skdParametere = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
                String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere);
                sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
        }
    }
}
