package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.innvandring.AddInnvandringsdatoOgLandTilPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.utils.ExtractErrorStatus;

@Service
public class SkdMeldingSender {

    @Autowired
    private AddInnvandringsdatoOgLandTilPersonerService addInnvandringsdatoOgLandTilPersonerService;

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

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
    private SkdMeldingResolver innvandring;

    public List<SendSkdMeldingTilTpsResponse> sendDoedsmeldinger(List<Person> personer, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> doedsMeldinger = createDoedsmeldinger.execute(personer, true);
        doedsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer(DOEDSMELDING_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendRelasjonsmeldinger(List<Person> personer, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMelding> relasjonsMeldinger = createRelasjoner.execute(personer, true);
        relasjonsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer("Relasjonsmelding", skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendInnvandringsMeldinger(List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        addInnvandringsdatoOgLandTilPersonerService.execute(personerSomIkkeEksitererITpsMiljoe);
        List<SkdMeldingTrans1> innvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, personerSomIkkeEksitererITpsMiljoe, true);
        innvandringsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer(INNVANDRING_CREATE_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendUpdateInnvandringsMeldinger(List<Person> personerSomAlleredeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> updateInnvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_UPDATE_MLD_NAVN, personerSomAlleredeEksitererITpsMiljoe, true);
        updateInnvandringsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer(INNVANDRING_UPDATE_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendUtvandringsmeldinger(List<Person> personerSomAlleredeEksistererITps, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> utvandringsMeldinger = createUtvandring.execute(personerSomAlleredeEksistererITps, true);
        utvandringsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer("Utvandring", skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendVergemaalsmeldinger(List<Person> personerIGruppen, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> vergemaalsMeldinger = createVergemaal.execute(personerIGruppen, true);
        vergemaalsMeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer("Vergemaal", skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }


    public List<SendSkdMeldingTilTpsResponse> sendFoedselsMeldinger(List<Person> personerSomSkalFoedes, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        List<SkdMeldingTrans1> foedselsmeldinger = createFoedselsmeldinger.executeFromPersons(personerSomSkalFoedes, true);
        foedselsmeldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer(FOEDSEL_MLD_NAVN, skdMelding, environmentsSet);
            listTpsResponsene.add(tpsResponse);
        });

        return listTpsResponsene;
    }

    private Map<String, String> mapSkdMeldingStatus(Map<String, String> responseSkdMeldingerPerEnv, Set<String> environmentsSet) {
        responseSkdMeldingerPerEnv.replaceAll((env, status) -> status != null && status.matches("^00.*")  ? "OK" : ExtractErrorStatus.extract(status));
        environmentsSet.forEach(env -> responseSkdMeldingerPerEnv.putIfAbsent(env, "Environment is not deployed"));
        return responseSkdMeldingerPerEnv;
    }

    private SendSkdMeldingTilTpsResponse sendSkdMeldingTilGitteMiljoer(String skdmeldingstype, SkdMelding skdMelding, Set<String> environmentsSet) {
        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        Map<String, String> responseSkdMeldingerPerEnv = sendSkdMeldingTilGitteMiljoer.execute(skdMelding.toString(), skdRequestMeldingDefinition, environmentsSet);

        return SendSkdMeldingTilTpsResponse.builder()
                .personId(skdMelding.getFodselsnummer())
                .skdmeldingstype(skdmeldingstype)
                .status(mapSkdMeldingStatus(responseSkdMeldingerPerEnv, environmentsSet))
                .build();
    }
}
