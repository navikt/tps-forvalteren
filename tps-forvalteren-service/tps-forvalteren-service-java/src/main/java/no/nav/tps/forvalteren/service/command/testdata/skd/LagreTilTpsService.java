package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import lombok.extern.log4j.Log4j;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.UppercaseDataInPerson;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.ServiceRoutineResponseStatus;

@Log4j
@Service
public class LagreTilTpsService {

    @Autowired
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Autowired
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    @Autowired
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Autowired
    private SkdMeldingSender skdMeldingSender;

    @Autowired
    private PersonStatusFraMiljoService personStatusFraMiljoService;

    public RsSkdMeldingResponse execute(Long gruppeId, Set<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        return sendMeldinger(personerIGruppen, environments);
    }

    public RsSkdMeldingResponse execute(List<Person> personerIGruppen, Set<String> environments) {
        return sendMeldinger(personerIGruppen, environments);
    }

    private RsSkdMeldingResponse sendMeldinger(List<Person> personerIGruppen, Set<String> environments) {

        environments = environments.stream().map(String::toLowerCase).collect(toSet());
        personerIGruppen.forEach(person -> uppercaseDataInPerson.execute(person));

        Map<String, SendSkdMeldingTilTpsResponse> innvandringCreateResponse = newHashMap();
        Map<String, SendSkdMeldingTilTpsResponse> innvandringUpdateResponse = newHashMap();
        Map<String, SendSkdMeldingTilTpsResponse> foedselMldResponse = newHashMap();
        Map<String, SendSkdMeldingTilTpsResponse> utvandringMldResponse = newHashMap();
        Map<String, SendSkdMeldingTilTpsResponse> envNotFound = newHashMap();
        Map<String, String> envNotFoundMap = new HashMap();

        Set<String> safeEnvironments = Sets.newHashSet(environments);
        Iterator<String> it = safeEnvironments.iterator();
        while (it.hasNext()) {

            String environment = it.next();
            try {
                List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerIGruppen, singleton(environment));
                List<Person> personerSomAlleredeEksitererITpsMiljoe = createListPersonerSomAlleredeEksiterer(personerIGruppen, personerSomIkkeEksitererITpsMiljoe);
                List<Person> personerSomSkalFoedes = findPersonerSomSkalHaFoedselsmelding.execute(personerIGruppen);

                personerSomIkkeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
                personerSomSkalFoedes.removeAll(personerSomAlleredeEksitererITpsMiljoe);

                amendStatus(innvandringCreateResponse, skdMeldingSender.sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe, singleton(environment)));
                amendStatus(innvandringUpdateResponse, skdMeldingSender.sendUpdateInnvandringsMeldinger(personerSomAlleredeEksitererITpsMiljoe, singleton(environment)));
                amendStatus(foedselMldResponse, skdMeldingSender.sendFoedselsMeldinger(personerSomSkalFoedes, singleton(environment)));

            } catch (RuntimeException e) {
                if (e.getMessage().contains("Unable to find environment")) {
                    envNotFoundMap.put(environment, "Miljø finnes ikke");
                } else {
                    envNotFoundMap.put(environment, e.getMessage());
                    log.error("Lagring til miljø feilet, " + e.getMessage(), e);
                }
                it.remove();
            }
        }

        if (!envNotFoundMap.isEmpty()) {
            envNotFound.put(personerIGruppen.get(0).getIdent(), SendSkdMeldingTilTpsResponse.builder()
                    .personId(personerIGruppen.get(0).getIdent())
                    .skdmeldingstype("TPS")
                    .status(envNotFoundMap)
                    .build());
        }


        List skdMldResponse = new ArrayList();

        skdMldResponse.addAll(envNotFound.values());
        skdMldResponse.addAll(innvandringCreateResponse.values());
        skdMldResponse.addAll(innvandringUpdateResponse.values());
        skdMldResponse.addAll(foedselMldResponse.values());
        skdMldResponse.addAll(utvandringMldResponse.values());

        skdMldResponse.addAll(skdMeldingSender.sendRelasjonsmeldinger(personerIGruppen, safeEnvironments));
        skdMldResponse.addAll(skdMeldingSender.sendDoedsmeldinger(personerIGruppen, safeEnvironments));
        skdMldResponse.addAll(skdMeldingSender.sendVergemaalsmeldinger(personerIGruppen, safeEnvironments));
        skdMldResponse.addAll(skdMeldingSender.sendUtvandringsmeldinger(personerIGruppen, safeEnvironments));
        skdMldResponse.addAll(skdMeldingSender.sendMeldingerOmForsvunnet(personerIGruppen, safeEnvironments));
        skdMldResponse.addAll(skdMeldingSender.sendMeldingerOmDubletter(personerIGruppen, safeEnvironments));

        personStatusFraMiljoService.hentStatusOgSettPaaPerson(personerIGruppen, safeEnvironments);

        List<ServiceRoutineResponseStatus> serviceRoutineResponser = sendNavEndringsmeldinger.execute(personerIGruppen, safeEnvironments);

        return new RsSkdMeldingResponse(null, skdMldResponse, serviceRoutineResponser);
    }

    private List<Person> createListPersonerSomAlleredeEksiterer(List<Person> personerIGruppe, List<Person> personerSomIkkeEksisterer) {
        List<Person> personerSomAlleredeEksisterer = new ArrayList<>();
        personerSomAlleredeEksisterer.addAll(personerIGruppe);
        personerSomAlleredeEksisterer.removeAll(personerSomIkkeEksisterer);

        return personerSomAlleredeEksisterer;
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