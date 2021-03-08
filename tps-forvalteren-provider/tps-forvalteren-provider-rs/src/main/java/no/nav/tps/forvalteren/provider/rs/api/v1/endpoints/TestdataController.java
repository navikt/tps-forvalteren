package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

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

import io.swagger.v3.oas.annotations.Operation;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
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
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.DeleteGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.FindAlleGrupperOrderByIdAsc;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.SaveGruppe;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenterService;
import no.nav.tps.forvalteren.service.command.testdata.StatusPaaIdenterITps;
import no.nav.tps.forvalteren.service.command.testdata.TestdataGruppeToSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;
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
    private RandomAdresseService randomAdresseService;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Autowired
    private StatusPaaIdenterITps statusPaaIdenterITps;

    @Autowired
    private CreateTestdataPerson createTestdataPerson;

    @LogExceptions
    @Operation(method = "createDummyAdresse new persons from criteria", description = "En tilfeldig gyldig adresse blir hentet fra TPS for hver person når man har satt withAdresse=true. "
            + "Det er valgfritt å sende med ENTEN postnummer ELLER kommunenummer.")
    @RequestMapping(value = "/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromKriterier(@PathVariable("gruppeId") Long gruppeId, @RequestBody @Valid RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerServiceFraIdenter.execute(identer);

        randomAdresseService.execute(personerSomSkalPersisteres, personKriterierListe.getAdresseNrInfo());

        personNameService.execute(personerSomSkalPersisteres);
        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/deletepersoner", method = RequestMethod.POST)
    public void deletePersons(@RequestBody RsPersonIdListe personIdListe) {
        deletePersonerByIdIn.execute(personIdListe.getIds());
    }

    @LogExceptions
    @RequestMapping(value = "/updatepersoner", method = RequestMethod.POST)
    public void updatePersons(@RequestBody List<RsPerson> personListe) {
        List<Person> personer = mapper.mapAsList(personListe, Person.class);
        savePersonListService.execute(personer);
    }

    @LogExceptions
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.POST)
    public Set<IdentMedStatus> checkIdentList(@RequestBody List<String> personIdentListe) {
        return sjekkIdenterService.finnGyldigeOgLedigeIdenter(personIdentListe);
    }

    @LogExceptions
    @RequestMapping(value = "/createpersoner/{gruppeId}", method = RequestMethod.POST)
    public void createPersonerFraIdentliste(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> personIdentListe) {
        List<Person> personer = opprettPersonerServiceFraIdenter.execute(personIdentListe);
        personNameService.execute(personer);
        setGruppeIdOnPersons.setGruppeId(personer, gruppeId);
        savePersonListService.execute(personer);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/tps/{gruppeId}", method = RequestMethod.POST)
    public RsSkdMeldingResponse lagreTilTPS(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> environments) {
        return lagreTilTpsService.execute(gruppeId, Sets.newHashSet(environments));
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSimpleGruppe> getGrupper() {
        List<Gruppe> grupper = findAlleGrupperOrderByIdAsc.execute();
        return mapper.mapAsList(grupper, RsSimpleGruppe.class);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        return mapper.map(gruppe, RsGruppe.class);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSimpleGruppe rsGruppe) {
        Gruppe gruppe = mapper.map(rsGruppe, Gruppe.class);
        saveGruppe.execute(gruppe);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        deleteGruppeById.execute(gruppeId);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/skd/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe(@PathVariable("gruppeId") Long gruppeId) {
        SkdEndringsmeldingGruppe newSkdEndringsmeldingGruppe = testdataGruppeToSkdEndringsmeldingGruppe.execute(gruppeId);
        return mapper.map(newSkdEndringsmeldingGruppe, RsSkdEndringsmeldingGruppe.class);
    }

    @LogExceptions
    @Transactional
    @GetMapping(value = "/tpsStatus")
    public RsTpsStatusPaaIdenterResponse getTestdataStatusFromTpsInAllEnvironments(@RequestParam("identer") List<String> identer,
            @RequestParam(value = "includeProd", required = false) Boolean includeProd) {
        return statusPaaIdenterITps.hentStatusPaaIdenterIAlleMiljoer(identer, includeProd);
    }

    @LogExceptions
    @RequestMapping(value = "/mal/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromMal(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsPersonMalRequest inputPersonRequest) {

        createTestdataPerson.execute(gruppeId, inputPersonRequest);
    }
}