package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.UtvandringAarsakskode32.UTVANDRING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.VergemaalAarsakskode37.VERGEMAAL_MLD_NAVN;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.ServiceRoutineResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LagreTilTps {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Autowired
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @Autowired
    private CreateRelasjoner createRelasjoner;

    @Autowired
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Autowired
    private CreateFoedselsmeldinger createFoedselsmeldinger;

    @Autowired
    private CreateVergemaal createVergemaal;

    @Autowired
    private CreateUtvandring createUtvandring;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    @Autowired
    private SkdMeldingSender skdMeldingSender;

    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    public RsSkdMeldingResponse execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        return sendMeldinger(personerIGruppen, environments);
    }

    public RsSkdMeldingResponse execute(List<Person> personerIGruppen, List<String> environments) {
        return sendMeldinger(personerIGruppen, environments);
    }

    private RsSkdMeldingResponse sendMeldinger(List<Person> personerIGruppen, List<String> environments){
        List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerIGruppen, environments);
        List<Person> personerSomAlleredeEksitererITpsMiljoe = createListPersonerSomAlleredeEksiterer(personerIGruppen, personerSomIkkeEksitererITpsMiljoe);
        List<Person> personerSomSkalFoedes = findPersonerSomSkalHaFoedselsmelding.execute(personerIGruppen);

        Set<String> environmentsSet = new HashSet<>(environments);

        personerSomIkkeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);

        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomIkkeEksitererITpsMiljoe);

        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        listTpsResponsene.addAll(skdMeldingSender.sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendUpdateInnvandringsMeldinger(personerSomAlleredeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendFoedselsMeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendRelasjonsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendDoedsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendVergemaalsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendUtvandringsmeldinger(personerSomAlleredeEksitererITpsMiljoe, environmentsSet));

        List<ServiceRoutineResponseStatus> serviceRoutineResponseList = sendNavEndringsmeldinger.execute(personerSomIkkeEksitererITpsMiljoe, environmentsSet);

        return new RsSkdMeldingResponse(null, listTpsResponsene, serviceRoutineResponseList);
    }

    private List<Person> createListPersonerSomAlleredeEksiterer(List<Person> personerIGruppe, List<Person> personerSomIkkeEksisterer) {
        List<Person> personerSomAlleredeEksisterer = new ArrayList<>();
        personerSomAlleredeEksisterer.addAll(personerIGruppe);
        personerSomAlleredeEksisterer.removeAll(personerSomIkkeEksisterer);

        return personerSomAlleredeEksisterer;
    }
}
