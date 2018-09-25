package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.ListExtractorKommaSeperated;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FetchPersonByIdent;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonerBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTps;

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

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

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
    private LagreTilTps lagreTilTps;

    @Autowired
    private ListExtractorKommaSeperated listExtractorKommaSeperated;

    @Autowired
    private FetchPersonByIdent fetchPersonByIdent;

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
    public RsSkdMeldingResponse sendFlerePersonerTilTps(@RequestParam("environments") String environments, @RequestBody List<String> personIdentListe) {
        List<Person> personer = findPersonerByIdIn.execute(personIdentListe);
        List<String> envs = listExtractorKommaSeperated.extractEnvironments(environments);
        return lagreTilTps.execute(personer, envs);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "personerdata") })
    @RequestMapping(value = "/personerdata", method = RequestMethod.GET)
    public List<RsPerson> getPersons(@RequestParam("identer") String personer) {
        List<String> identer = listExtractorKommaSeperated.extractIdenter(personer);
        List<Person> personList = personRepository.findByIdentIn(identer);
        return mapper.mapAsList(personList, RsPerson.class);
    }

}