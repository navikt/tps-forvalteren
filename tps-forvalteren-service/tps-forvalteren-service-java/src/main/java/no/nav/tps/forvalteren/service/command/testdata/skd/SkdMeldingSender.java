package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.fetchSivilstand;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.FoedselsmeldingAarsakskode01.FOEDSEL_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02Tildelingskode2Update.INNVANDRING_UPDATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmDubletter.MELDING_OM_DUBLETTER;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmForsvunnetAarsakskode82.MELDING_OM_FORSVUNNET;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmStatsborgerskap.ENDRING_AV_STATSBORGERSKAP;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.NavneEndringsmeldingAarsakskode06.NAVN_ENDRING_MLD;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.UtvandringAarsakskode32.UTVANDRING_INNVANDRING_MLD_NAVN;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.utils.ExtractErrorStatus;

@Service
@RequiredArgsConstructor
public class SkdMeldingSender {

    private final SkdMessageCreatorTrans1 skdMessageCreatorTrans1;
    private final CreateRelasjoner createRelasjoner;
    private final CreateDoedsmeldinger createDoedsmeldinger;
    private final CreateFoedselsmeldinger createFoedselsmeldinger;
    private final CreateNavnEndringsmeldinger createNavnEndringsmeldinger;
    private final CreateVergemaal createVergemaal;
    private final UtvandringOgInnvandring utvandringOgInnvandring;
    private final CreateMeldingerOmForsvunnet createMeldingerOmForsvunnet;
    private final SivilstandMeldinger sivilstandMeldinger;
    private final CreateMeldingerOmDubletter createMeldingerOmDubletter;
    private final StatsborgerskapOgBibehold statsborgerskapOgBibehold;
    private final SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
    private final SkdMeldingResolver innvandring;

    public List<SendSkdMeldingTilTpsResponse> sendDoedsmeldinger(List<Person> personer, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> doedsMeldinger = createDoedsmeldinger.execute(personer, true);
        doedsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(DOEDSMELDING_MLD_NAVN, skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendRelasjonsmeldinger(List<Person> personer, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMelding> relasjonsMeldinger = createRelasjoner.execute(personer, true);
        relasjonsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer("Relasjonsmelding", skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public Collection sendSivilstand(List<Person> personer, Set<String> environments) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMelding> meldinger = sivilstandMeldinger.createMeldinger(personer, true);
        meldinger.forEach(skdMelding -> {
            SendSkdMeldingTilTpsResponse tpsResponse = sendSkdMeldingTilGitteMiljoer(
                    format("Sivilstand-%s-melding", fetchSivilstand(((SkdMeldingTrans1) skdMelding).getSivilstand()).name()), skdMelding, environments);
            listTpsResponsene.add(tpsResponse);
        });
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendInnvandringsMeldinger(List<Person> personerSomIkkeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> innvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, personerSomIkkeEksitererITpsMiljoe, true);
        innvandringsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(INNVANDRING_CREATE_MLD_NAVN, skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendUpdateInnvandringsMeldinger(List<Person> personerSomAlleredeEksitererITpsMiljoe, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> updateInnvandringsMeldinger = skdMessageCreatorTrans1.execute(INNVANDRING_UPDATE_MLD_NAVN, personerSomAlleredeEksitererITpsMiljoe, true);
        updateInnvandringsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(INNVANDRING_UPDATE_MLD_NAVN, skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendUtvandringOgNyeInnvandringsmeldinger(List<Person> personerIGruppen, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> utvandringsMeldinger = utvandringOgInnvandring.createMeldinger(personerIGruppen, true);
        utvandringsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(UTVANDRING_INNVANDRING_MLD_NAVN, skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public Collection sendMeldingerOmForsvunnet(List<Person> personerIGruppen, Set<String> environments) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> meldingerOmForsvunnet = createMeldingerOmForsvunnet.filterForsvunnet(personerIGruppen, true);
        meldingerOmForsvunnet.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(MELDING_OM_FORSVUNNET, skdMelding, environments))
        );
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendVergemaalsmeldinger(List<Person> personerIGruppen, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> vergemaalsMeldinger = createVergemaal.execute(personerIGruppen, true);
        vergemaalsMeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer("Vergemaal", skdMelding, environmentsSet))
        );
        return listTpsResponsene;
    }

    public List<SendSkdMeldingTilTpsResponse> sendFoedselsMeldinger(List<Person> personerSomSkalFoedes, Set<String> environmentsSet) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        List<SkdMeldingTrans1> foedselsmeldinger = createFoedselsmeldinger.executeFromPersons(personerSomSkalFoedes, true);
        foedselsmeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(FOEDSEL_MLD_NAVN, skdMelding, environmentsSet))
        );

        List<SkdMeldingTrans1> navnEndringsmeldinger = createNavnEndringsmeldinger.executeFromPersons(
                personerSomSkalFoedes.stream().filter(person -> isNotBlank(person.getMellomnavn())).collect(toList()), true);
        navnEndringsmeldinger.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(NAVN_ENDRING_MLD, skdMelding, environmentsSet))
        );

        return listTpsResponsene;
    }

    public Collection sendMeldingerOmDubletter(List<Person> personerIGruppen, Set<String> environments) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMeldingTrans1> meldingerOmDubletter = createMeldingerOmDubletter.filterDubletter(personerIGruppen, true);
        meldingerOmDubletter.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(MELDING_OM_DUBLETTER, skdMelding, environments))
        );
        return listTpsResponsene;
    }

    public Collection sendEndringAvStatsborgerskapOgBibehold(List<Person> personerIGruppen, Set<String> environments) {
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();
        List<SkdMelding> endringOmStatsborgerskap = statsborgerskapOgBibehold.createMeldinger(personerIGruppen, true);
        endringOmStatsborgerskap.forEach(skdMelding ->
                listTpsResponsene.add(sendSkdMeldingTilGitteMiljoer(ENDRING_AV_STATSBORGERSKAP, skdMelding, environments))
        );
        return listTpsResponsene;
    }

    private Map<String, String> mapSkdMeldingStatus(Map<String, String> responseSkdMeldingerPerEnv, Set<String> environmentsSet) {
        responseSkdMeldingerPerEnv.replaceAll((env, status) -> prepareStatus(status));
        environmentsSet.forEach(env -> responseSkdMeldingerPerEnv.putIfAbsent(env, "Environment is not deployed"));
        return responseSkdMeldingerPerEnv;
    }

    private String prepareStatus(String status) {
        return nonNull(status) && status.matches("^00.*") ? "OK" : ExtractErrorStatus.extract(status);
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
