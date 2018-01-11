package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.tps.SkdStartAjourhold;

@Service
public class LagreTilTps {

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";

    @Autowired
    private SkdMessageSenderTrans1 skdMessageSenderTrans1;

    @Autowired
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Autowired
    private CreateRelasjoner createRelasjoner;

    @Autowired
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Autowired
    private SkdStartAjourhold skdStartAjourhold;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdMeldingResolver innvandring;

    public void execute(Long gruppeId, List<String> environments) {
        List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(gruppeId, environments);
        List<String> skdMeldinger = new ArrayList<>();
        List<String> innvandringsMeldinger = skdMessageSenderTrans1.execute(NAVN_INNVANDRINGSMELDING, personerSomIkkeEksitererITpsMiljoe, true);
        List<String> relasjonsMeldinger = createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, true);
        List<String> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, true);
        skdMeldinger.addAll(innvandringsMeldinger);
        skdMeldinger.addAll(relasjonsMeldinger);
        skdMeldinger.addAll(doedsMeldinger);

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        Set<String> environmentsSet = new HashSet<>(environments);
        for (String skdMelding : skdMeldinger) {
            sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, environmentsSet);
        }
        skdStartAjourhold.execute(new HashSet<>(environments));
    }
}
