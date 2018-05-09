package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;

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
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SkdMeldingResolver innvandring;

    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    public RsSkdMeldingResponse execute(Long gruppeId, List<String> environments) {
        skdRequestMeldingDefinition = innvandring.resolve();

        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerIGruppen, environments);
        List<Person> personerSomAlleredeEksitererITpsMiljoe = createListPersonerSomAlleredeEksiterer(personerIGruppen, personerSomIkkeEksitererITpsMiljoe);
        List<Person> personerSomSkalFoedes = findPersonerSomSkalHaFoedselsmelding.execute(personerIGruppen);
        Set<String> environmentsSet = new HashSet<>(environments);

        personerSomIkkeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
        personerSomSkalFoedes.removeAll(personerSomAlleredeEksitererITpsMiljoe);
        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);

        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        listTpsResponsene.addAll(sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(sendUpdateInnvandringsMeldinger(personerSomAlleredeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(sendFoedselsMeldinger(personerSomSkalFoedes, environmentsSet));
        listTpsResponsene.addAll(sendRelasjonsmeldinger(personerSomIkkeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(sendDoedsmeldinger(gruppeId, environmentsSet));

        return new RsSkdMeldingResponse(gruppeId, listTpsResponsene);
    }

    //TODO ALle disse private metodene bør ut egen felles metode som ligger i egen klasse. Få sendDoedsmelding til å ta inn liste av personer i steden for gruppe.
    private List<SendSkdMeldingTilTpsResponse> sendDoedsmeldinger(Long gruppeId, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> doedsMeldinger = createDoedsmeldinger.execute(gruppeId, true);
        doedsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Doedsmelding", skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    private List<SendSkdMeldingTilTpsResponse> sendRelasjonsmeldinger(List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMelding> relasjonsMeldinger = createRelasjoner.execute(personerSomIkkeEksitererITpsMiljoe, true);
        relasjonsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer("Relasjonsmelding", skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    private List<SendSkdMeldingTilTpsResponse> sendInnvandringsMeldinger( List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> innvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, personerSomIkkeEksitererITpsMiljoe, true);
        innvandringsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer(INNVANDRING_CREATE_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    private List<SendSkdMeldingTilTpsResponse> sendUpdateInnvandringsMeldinger( List<Person> personerSomAlleredeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> updateInnvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_UPDATE_MLD_NAVN, personerSomAlleredeEksitererITpsMiljoe, true);
        updateInnvandringsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse= sendSkdMeldingTilGitteMiljoer(INNVANDRING_UPDATE_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    private List<SendSkdMeldingTilTpsResponse> sendFoedselsMeldinger(List<Person> personerSomSkalFoedes, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        List<String> foedselsmeldinger = createFoedselsmeldinger.execute(personerSomSkalFoedes, true);
        foedselsmeldinger.stream().forEach(skdmelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer("Foedselsmelding", skdmelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });

        return listTpsResponsene;
    }

    private Map<String, String> mapStatus(Map<String, String> responseSkdMeldingerPerEnv, Set<String> environmentsSet) {
        responseSkdMeldingerPerEnv.replaceAll((env,status)->"00".equals(status) ? "OK" : status);
        environmentsSet.forEach(env -> responseSkdMeldingerPerEnv.putIfAbsent(env, "Environment is not deployed"));
        return responseSkdMeldingerPerEnv;
    }

    private SendSkdMeldingTilTpsResponse sendSkdMeldingTilGitteMiljoer(String skdmeldingstype, SkdMelding skdMelding, Set<String> environmentsSet) {
        Map<String, String> responseSkdMeldingerPerEnv = sendSkdMeldingTilGitteMiljoer.execute(skdMelding.toString(), skdRequestMeldingDefinition, environmentsSet);

        return SendSkdMeldingTilTpsResponse.builder()
                .personId(skdMelding.getFodselsnummer())
                .skdmeldingstype(skdmeldingstype)
                .status(mapStatus(responseSkdMeldingerPerEnv, environmentsSet))
                .build();
    }

    private List<Person> createListPersonerSomAlleredeEksiterer(List<Person> personerIGruppe, List<Person> personerSomIkkeEksisterer) {
        List<Person> personerSomAlleredeEksisterer = new ArrayList<>();
        personerSomAlleredeEksisterer.addAll(personerIGruppe);
        personerSomAlleredeEksisterer.removeAll(personerSomIkkeEksisterer);

        return personerSomAlleredeEksisterer;
    }
}
