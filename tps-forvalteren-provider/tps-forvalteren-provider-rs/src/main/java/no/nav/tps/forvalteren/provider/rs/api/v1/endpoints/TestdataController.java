package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Sets;

import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;
import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.RsVergemaal;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.DeleteGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.FindAlleGrupperOrderByIdAsc;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.OpprettVergemaal;
import no.nav.tps.forvalteren.service.command.testdata.SaveGruppe;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenterService;
import no.nav.tps.forvalteren.service.command.testdata.StatusPaaIdenterITps;
import no.nav.tps.forvalteren.service.command.testdata.TestdataGruppeToSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;
import no.nav.tps.forvalteren.service.command.testdatamal.CreateTestdataPerson;

@RestController
@RequestMapping(value = "api/v1/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class TestdataController {

    private static final String REST_SERVICE_NAME = "testdata";

    @Autowired
    private PersonNameService personNameService;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    @Autowired
    private OpprettPersonerService opprettPersonerServiceFraIdenter;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private SavePersonListService savePersonListService;

    @Autowired
    private SjekkIdenterService sjekkIdenterService;

    @Autowired
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Autowired
    private OpprettVergemaal opprettVergemaal;

    @Autowired
    private FindAlleGrupperOrderByIdAsc findAlleGrupperOrderByIdAsc;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private DeletePersonerByIdIn deletePersonerByIdIn;

    @Autowired
    private DeleteGruppeById deleteGruppeById;

    @Autowired
    private SaveGruppe saveGruppe;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private LagreTilTpsService lagreTilTpsService;

    @Autowired
    private TestdataGruppeToSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe;

    @Autowired
    private SetRandomAdresseOnPersons setRandomAdresseOnPersons;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Autowired
    private StatusPaaIdenterITps statusPaaIdenterITps;

    @Autowired
    private CreateTestdataPerson createTestdataPerson;

    @ApiOperation(value = "create new persons from criteria", notes = "En tilfeldig gyldig adresse blir hentet fra TPS for hver person når man har satt withAdresse=true. "
            + "Det er valgfritt å sende med ENTEN postnummer ELLER kommunenummer.")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersonsFromMal") })
    @RequestMapping(value = "/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromKriterier(@PathVariable("gruppeId") Long gruppeId, @RequestBody @Valid RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerServiceFraIdenter.execute(identer);

        setRandomAdresseOnPersons.execute(personerSomSkalPersisteres, personKriterierListe.getAdresseNrInfo());

        personNameService.execute(personerSomSkalPersisteres);
        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons") })
    @RequestMapping(value = "/deletepersoner", method = RequestMethod.POST)
    public void deletePersons(@RequestBody RsPersonIdListe personIdListe) {
        deletePersonerByIdIn.execute(personIdListe.getIds());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updatePersons") })
    @RequestMapping(value = "/updatepersoner", method = RequestMethod.POST)
    public void updatePersons(@RequestBody List<RsPerson> personListe) {
        List<Person> personer = mapper.mapAsList(personListe, Person.class);
        savePersonListService.execute(personer);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "checkIdentList") })
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.POST)
    public Set<IdentMedStatus> checkIdentList(@RequestBody List<String> personIdentListe) {
        return sjekkIdenterService.finnGyldigeOgLedigeIdenter(personIdentListe);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createPersoner") })
    @RequestMapping(value = "/createpersoner/{gruppeId}", method = RequestMethod.POST)
    public void createPersonerFraIdentliste(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> personIdentListe) {
        List<Person> personer = opprettPersonerServiceFraIdenter.execute(personIdentListe);
        personNameService.execute(personer);
        setGruppeIdOnPersons.setGruppeId(personer, gruppeId);
        savePersonListService.execute(personer);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "saveTPS") })
    @RequestMapping(value = "/tps/{gruppeId}", method = RequestMethod.POST)
    public RsSkdMeldingResponse lagreTilTPS(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> environments) {
        return lagreTilTpsService.execute(gruppeId, Sets.newHashSet(environments));
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGrupper") })
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSimpleGruppe> getGrupper() {
        List<Gruppe> grupper = findAlleGrupperOrderByIdAsc.execute();
        return mapper.mapAsList(grupper, RsSimpleGruppe.class);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppe") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        return mapper.map(gruppe, RsGruppe.class);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createGruppe") })
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSimpleGruppe rsGruppe) {
        Gruppe gruppe = mapper.map(rsGruppe, Gruppe.class);
        saveGruppe.execute(gruppe);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteGruppe") })
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        deleteGruppeById.execute(gruppeId);
    }

    @Transactional
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "testdataGruppeToSkdEndringsmeldingGruppe") })
    @RequestMapping(value = "/skd/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe(@PathVariable("gruppeId") Long gruppeId) {
        SkdEndringsmeldingGruppe newSkdEndringsmeldingGruppe = testdataGruppeToSkdEndringsmeldingGruppe.execute(gruppeId);
        return mapper.map(newSkdEndringsmeldingGruppe, RsSkdEndringsmeldingGruppe.class);
    }

    @Transactional
    @LogExceptions
    @GetMapping(value = "/tpsStatus")
    public RsTpsStatusPaaIdenterResponse getTestdataStatusFromTpsInAllEnvironments(@RequestParam("identer") List<String> identer) {
        return statusPaaIdenterITps.hentStatusPaaIdenterIAlleMiljoer(identer);
    }

    @Transactional
    @LogExceptions
    @RequestMapping(value = "/vergemaal", method = RequestMethod.POST)
    public void createVergemaal(@RequestBody RsVergemaal rsVergemaal) {
        opprettVergemaal.execute(rsVergemaal);
    }

    @Transactional
    @LogExceptions
    @RequestMapping(value = "/vergemaal/{vergemaalId}", method = RequestMethod.DELETE)
    public void deleteVergemaal(@PathVariable("vergemaalId") Long vergemaalId) {
        vergemaalRepository.deleteById(vergemaalId);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersonsFromMal") })
    @RequestMapping(value = "/mal/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromMal(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsPersonMalRequest inputPersonRequest) {

        createTestdataPerson.execute(gruppeId, inputPersonRequest);
    }
}