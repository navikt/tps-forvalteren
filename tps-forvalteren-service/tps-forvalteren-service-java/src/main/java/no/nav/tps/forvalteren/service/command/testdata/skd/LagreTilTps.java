package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;

@Service
public class LagreTilTps {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Autowired
    private CreateRelasjoner createRelasjoner;

    @Autowired
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SkdMeldingResolver innvandring;

    public void execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerIGruppen, environments);

        List<Person> personerSomAlleredeEksitererITpsMiljoe = personerIGruppen;
        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomIkkeEksitererITpsMiljoe);

        List<String> innvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, personerSomIkkeEksitererITpsMiljoe, true);
        List<String> innvandringUpdateMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_UPDATE_MLD_NAVN, personerSomAlleredeEksitererITpsMiljoe, true);
        List<String> relasjonsMeldinger = createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, true);
        List<String> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, true);

        List<String> skdMeldinger = new ArrayList<>();
        skdMeldinger.addAll(innvandringsMeldinger);
        skdMeldinger.addAll(innvandringUpdateMeldinger);
        skdMeldinger.addAll(relasjonsMeldinger);
        skdMeldinger.addAll(doedsMeldinger);

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        Set<String> environmentsSet = new HashSet<>(environments);
        for (String skdMelding : skdMeldinger) {
            sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, environmentsSet);
        }
    }
}
