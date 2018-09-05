package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsDollyPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.FetchPersonByIdent;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.StatusPaaIdenterITps;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.ExtractOpprettKritereFromDollyKriterier;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDummyAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTps;

/**
 * Dolly trenger eget grensesnitt. Det skal opprettes testpersoner utenfor testgruppe i TPSF, for å slippe synkronisering mellom testgruppene i Dolly og i TPSF.
 */
@RestController
@RequestMapping(value = "api/v1/dolly/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class TestdataControllerForDolly {

    private static final String REST_SERVICE_NAME = "dolly_testdata";

    @Autowired
    private SetNameOnPersonsService setNameOnPersonsService;

    @Autowired
    private OpprettPersoner opprettPersonerFraIdenter;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private SavePersonListService savePersonListService;
    
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DeletePersonerByIdIn deletePersonerByIdIn;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private LagreTilTps lagreTilTps;

    @Autowired
    private SetDummyAdresseOnPersons setDummyAdresseOnPersons;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Autowired
    private StatusPaaIdenterITps statusPaaIdenterITps;

    @Autowired
    private SavePersonBulk savePersonBulk;

    @Autowired
    private ExtractOpprettKritereFromDollyKriterier extractOpprettKritereFromDollyKriterier;

    @Autowired
    private ListExtractorKommaSeperated listExtractorKommaSeperated;

    @Autowired
    private FetchPersonByIdent fetchPersonByIdent;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersonsFromKriterier") })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/personer", method = RequestMethod.POST)
    public List<String> createNewPersonsFromDollyKriterier(@RequestBody RsDollyPersonKriteriumRequest personKriteriumRequest) {
        RsPersonKriteriumRequest personKriterierListe = extractOpprettKritereFromDollyKriterier.execute(personKriteriumRequest);

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(identer);

//        if (personKriterierListe.isWithAdresse()) {
//            setDummyAdresseOnPersons.execute(personerSomSkalPersisteres);
//        }

        setNameOnPersonsService.execute(personerSomSkalPersisteres);

        List<Person> tpsfPersoner = extractOpprettKritereFromDollyKriterier.addDollyKriterumValuesToPersonAndSave(personKriteriumRequest, personerSomSkalPersisteres);
        List<Person> personer = savePersonBulk.execute(tpsfPersoner);

        return personer.stream().map(person -> person.getIdent()).collect(Collectors.toList());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons") })
    @RequestMapping(value = "/tilTps", method = RequestMethod.POST)
    public RsSkdMeldingResponse sendPersonTilTps(@RequestParam("environments") String environments, @RequestBody IdentBody ident) {
        Person person = fetchPersonByIdent.execute(ident.getIdent());
        List<String> envs = listExtractorKommaSeperated.extractEnvironments(environments);
        return lagreTilTps.execute(Arrays.asList(person), envs);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons") })
    @RequestMapping(value = "/personerdata", method = RequestMethod.GET)
    public List<RsPerson> getPersons(@RequestParam("identer") String personer) {
        List<String> identer = listExtractorKommaSeperated.extractIdenter(personer);
        List<Person> personList = personRepository.findByIdentIn(identer);
        return mapper.mapAsList(personList, RsPerson.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons") })
    @RequestMapping(value = "/getpersoner", method = RequestMethod.POST)
    public RsPerson fetchPersondata(@RequestBody PersonIdentListe personIdentListe) {
        List<Person> personList = personRepository.findByIdentIn(personIdentListe.getIdenter());
        return mapper.map(personList, RsPerson.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createPersoner") })
    @RequestMapping(value = "/createpersoner/fraidentliste", method = RequestMethod.POST)
    public void createPersonerFraIdentliste(@RequestBody List<String> personIdentListe) {
        List<Person> personer = opprettPersonerFraIdenter.execute(personIdentListe);
        setNameOnPersonsService.execute(personer);
        
        savePersonListService.execute(personer);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "saveTPS") })
    @RequestMapping(value = "/tps/{gruppeId}", method = RequestMethod.POST)
    public RsSkdMeldingResponse lagreTilTPS(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> environments) {
        //TODO: uavh av gruppeId
        return lagreTilTps.execute(gruppeId, environments);
    }

    @Transactional
    @LogExceptions
    @GetMapping(value = "/tpsStatus")
    public RsTpsStatusPaaIdenterResponse getTestdataStatusFromTpsInAllEnvironments(@RequestParam("identer") List<String> identer) {
        return statusPaaIdenterITps.hentStatusPaaIdenterIAlleMiljoer(identer);
    }

}