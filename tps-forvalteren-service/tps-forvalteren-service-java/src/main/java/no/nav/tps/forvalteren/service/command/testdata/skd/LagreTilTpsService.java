package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.ServiceRoutineResponseStatus;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LagreTilTpsService {

    private static final String TPS = "TPS";

    private final FindPersonsNotInEnvironments findPersonsNotInEnvironments;
    private final FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;
    private final FindGruppeById findGruppeById;
    private final SendNavEndringsmeldinger sendNavEndringsmeldinger;
    private final SkdMeldingSender skdMeldingSender;
    private final PersonStatusFraMiljoService personStatusFraMiljoService;
    private final PersonService personService;
    private final ForkJoinPool tpsfForkJoinPool;

    public RsSkdMeldingResponse execute(Long gruppeId, Set<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        return sendMeldinger(personerIGruppen, environments);
    }

    public RsSkdMeldingResponse execute(List<Person> personerIGruppen, Set<String> environments) {

        environments = environments.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return sendMeldinger(personerIGruppen, environments);
    }

    private RsSkdMeldingResponse sendMeldinger(List<Person> personerIGruppen, final Set<String> environments) {

        personerIGruppen.forEach(Person::toUppercase);

        List<Person> personerInkludertIdenthistorikk = getPersonUtvidelseForIdenthistorikk(personerIGruppen);
        personerInkludertIdenthistorikk.forEach(Person::toUppercase);
        List<Person> personerSomSkalFoedes = findPersonerSomSkalHaFoedselsmelding.execute(personerIGruppen);

        Future<List<SendSkdMeldingTilTpsResponse>> future = tpsfForkJoinPool.submit(() -> {

            Map<String, SendSkdMeldingTilTpsResponse> innvandringCreateResponse = newHashMap();
            Map<String, SendSkdMeldingTilTpsResponse> innvandringUpdateResponse = newHashMap();
            Map<String, SendSkdMeldingTilTpsResponse> foedselMldResponse = newHashMap();
            Map<String, String> envNotFoundMap = new HashMap();

            environments.parallelStream().map(env -> {

                try {
                    List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerInkludertIdenthistorikk, singleton(env));
                    List<Person> personerSomAlleredeEksitererITpsMiljoe = createListPersonerSomAlleredeEksiterer(personerIGruppen, personerSomIkkeEksitererITpsMiljoe);

                    personerSomIkkeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
                    List<Person> threadPersonerSomSkalFoedes = createListPersonerSomSkalFoedes(personerSomSkalFoedes, personerSomAlleredeEksitererITpsMiljoe);

                    amendStatus(innvandringCreateResponse, skdMeldingSender.sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe, singleton(env)));
                    amendStatus(innvandringUpdateResponse, skdMeldingSender.sendUpdateInnvandringsMeldinger(personerSomAlleredeEksitererITpsMiljoe, singleton(env)));
                    amendStatus(foedselMldResponse, skdMeldingSender.sendFoedselsMeldinger(threadPersonerSomSkalFoedes, singleton(env)));

                    return "found";
                } catch (RuntimeException e) {
                    if (e.getMessage().contains("Unable to find environment")) {
                        envNotFoundMap.put(env, "Miljø finnes ikke");
                    } else {
                        envNotFoundMap.put(env, e.getMessage());
                        log.error("Lagring til miljø feilet, " + e.getMessage(), e);
                    }
                    return env;
                }
            }).collect(Collectors.toSet());

            List threadStatus = new ArrayList<>();
            threadStatus.addAll(innvandringCreateResponse.values());
            threadStatus.addAll(innvandringUpdateResponse.values());
            threadStatus.addAll(foedselMldResponse.values());
            threadStatus.addAll(envNotFoundMap.isEmpty() ? emptyList() : newArrayList(
                    SendSkdMeldingTilTpsResponse.builder()
                            .personId(personerIGruppen.get(0).getIdent())
                            .skdmeldingstype(TPS)
                            .status(envNotFoundMap)
                            .build()));
            return threadStatus;
        });

        try {
            List<SendSkdMeldingTilTpsResponse> skdMldResponse = future.get();
            Set<String> safeEnvironments = Sets.newHashSet(environments);
            Set<String> unsafeEnvironment = new HashSet<>();
            skdMldResponse.stream()
                    .filter(response -> TPS.equals(response.getSkdmeldingstype()))
                    .map(response -> response.getStatus().keySet())
                    .collect(Collectors.toSet()).forEach(unsafeEnvironment::addAll);

            safeEnvironments.removeAll(unsafeEnvironment);

            skdMldResponse.addAll(skdMeldingSender.sendRelasjonsmeldinger(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendSivilstand(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendDoedsmeldinger(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendVergemaalsmeldinger(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendMeldingerOmForsvunnet(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendMeldingerOmDubletter(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendEndringAvStatsborgerskapOgBibehold(personerIGruppen, safeEnvironments));
            skdMldResponse.addAll(skdMeldingSender.sendUtvandringOgNyeInnvandringsmeldinger(personerIGruppen, safeEnvironments));

            personStatusFraMiljoService.hentStatusOgSettPaaPerson(personerIGruppen, safeEnvironments);

            List<ServiceRoutineResponseStatus> serviceRoutineResponser = sendNavEndringsmeldinger.execute(personerIGruppen, safeEnvironments);

            return new RsSkdMeldingResponse(null, skdMldResponse, serviceRoutineResponser);

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new TpsfTechnicalException("Multi-threading feilet mot TPS", e);
        }
    }

    private List<Person> getPersonUtvidelseForIdenthistorikk(List<Person> personerIGruppen) {
        List<Person> gruppePersoner = newArrayList(personerIGruppen);
        gruppePersoner.addAll(personService.getPersonerByIdenter(newArrayList(
                personerIGruppen.stream().map(Person::getIdentHistorikk)
                        .flatMap(historikk -> historikk.stream().map(IdentHistorikk::getAliasPerson))
                        .map(Person::getIdent).collect(Collectors.toSet())))
        );
        return gruppePersoner;
    }

    private List<Person> createListPersonerSomAlleredeEksiterer(List<Person> personerIGruppe, List<Person> personerSomIkkeEksisterer) {

        List<Person> personerSomAlleredeEksisterer = newArrayList(personerIGruppe);
        personerSomAlleredeEksisterer.removeAll(personerSomIkkeEksisterer);

        return personerSomAlleredeEksisterer;
    }

    private List<Person> createListPersonerSomSkalFoedes(List<Person> personerSomSkalFoedes, List<Person> personerAlleredeIMiljoe) {

        List<Person> personerSomskalFoedesCopy = newArrayList(personerSomSkalFoedes);
        personerSomskalFoedesCopy.removeAll(personerAlleredeIMiljoe);

        return personerSomskalFoedesCopy;
    }

    private void amendStatus(Map<String, SendSkdMeldingTilTpsResponse> total, List<SendSkdMeldingTilTpsResponse> partial) {
        partial.forEach(response -> {
            if (total.containsKey(response.getPersonId())) {
                total.get(response.getPersonId()).getStatus().putAll(response.getStatus());
            } else {
                total.put(response.getPersonId(), response);
            }
        });
    }
}