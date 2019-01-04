package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Sets.newHashSet;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.dolly.RsIdenterMiljoer;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.ListExtractorKommaSeperated;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonerBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;

@RestController
@RequestMapping(value = "api/v1/dolly/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class TestdataBestillingsController {

    private static final String REST_SERVICE_NAME = "dolly_testdata";

    @Autowired
    private PersonerBestillingService personerBestillingService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private LagreTilTpsService lagreTilTps;

    @Autowired
    private ListExtractorKommaSeperated listExtractorKommaSeperated;

    @Autowired
    private FindPersonerByIdIn findPersonerByIdIn;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersonsFromKriterier") })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/personer", method = RequestMethod.POST)
    public List<String> createPersonerFraBestillingskriterier(@RequestBody RsPersonBestillingKriteriumRequest personKriteriumRequest) {
        List<Person> personer = personerBestillingService.createTpsfPersonFromRestRequest(personKriteriumRequest);
        return personer.stream().map(person -> person.getIdent()).collect(Collectors.toList());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "flereTilTps") })
    @RequestMapping(value = "/tilTpsFlere", method = RequestMethod.POST)
    public RsSkdMeldingResponse sendFlerePersonerTilTps(@RequestBody RsIdenterMiljoer tpsRequest) {
        List<Person> personer = findPersonerByIdIn.execute(tpsRequest.getIdenter());
        return lagreTilTps.execute(personer, newHashSet(tpsRequest.getMiljoer()));
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "personerdata") })
    @RequestMapping(value = "/personerdata", method = RequestMethod.GET)
    public List<RsPerson> getPersons(@RequestParam("identer") String personer) {
        List<String> identer = listExtractorKommaSeperated.extractIdenter(personer);
        List<Person> personList = personRepository.findByIdentIn(identer);
        return mapper.mapAsList(personList, RsPerson.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "hentpersoner") })
    @RequestMapping(value = "/hentpersoner", method = RequestMethod.POST)
    public List<RsPerson> hentPersoner(@RequestBody List<String> identer) {
        //Begrenser maks antall identer i SQL spørring
        List<List<String>> identLists = Lists.partition(identer, 1000);
        List<Person> resultat = new ArrayList<>(identer.size());
        for (List<String> subset : identLists) {
            resultat.addAll(personRepository.findByIdentIn(subset));
        }
        return mapper.mapAsList(resultat, RsPerson.class);
    }
}