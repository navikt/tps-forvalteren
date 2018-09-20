package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsRestPersonKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.IdentBody;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.ListExtractorKommaSeperated;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FetchPersonByIdent;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.restreq.RestPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTps;

import javax.transaction.Transactional;
import java.util.Arrays;
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

/**
 * Dolly trenger eget grensesnitt. Det skal opprettes testpersoner utenfor testgruppe i TPSF, for å slippe synkronisering mellom testgruppene i Dolly og i TPSF.
 */
@RestController
@RequestMapping(value = "api/v1/dolly/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class RestApiTestdatacontroller {

    private static final String REST_SERVICE_NAME = "dolly_testdata";

    @Autowired
    private RestPersonerService restPersonerService;

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
    public List<String> createNewPersonsFromDollyKriterier(@RequestBody RsRestPersonKriteriumRequest personKriteriumRequest) {
        List<Person> personer = restPersonerService.createTpsfPersonFromRestRequest(personKriteriumRequest);
        return personer.stream().map(person -> person.getIdent()).collect(Collectors.toList());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "tilTps") })
    @RequestMapping(value = "/tilTps", method = RequestMethod.POST)
    public RsSkdMeldingResponse sendPersonTilTps(@RequestParam("environments") String environments, @RequestBody IdentBody ident) {
        Person person = fetchPersonByIdent.execute(ident.getIdent());
        List<String> envs = listExtractorKommaSeperated.extractEnvironments(environments);
        return lagreTilTps.execute(Arrays.asList(person), envs);
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