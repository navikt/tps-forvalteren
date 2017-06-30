package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Tag;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenter;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

@Transactional
@RestController
@RequestMapping(value = "api/v1/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class TestdataController {

    private static final String REST_SERVICE_NAME = "testdata";

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
    private SjekkIdenter sjekkIdenter;

    @Autowired
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private MapperFacade mapper;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersonsFromKriterier") })
    @RequestMapping(value = "/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromKriterier(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);
        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(ekstraherIdenterFraTestdataRequests.execute(testdataRequests));

        setNameOnPersonsService.execute(personerSomSkalPersisteres);
        setGruppeIdOnPersons.setGruppeId(personerSomSkalPersisteres, gruppeId);
        savePersonListService.save(personerSomSkalPersisteres);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons") })
    @RequestMapping(value = "/deletepersoner", method = RequestMethod.POST)
    public void deletePersons(@RequestBody RsPersonIdListe personIdListe) {
        personRepository.deleteByIdIn(personIdListe.getIds());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updatePersons") })
    @RequestMapping(value = "/updatepersoner", method = RequestMethod.POST)
    public void updatePersons(@RequestBody List<RsPerson> personListe) {
        List<Person> personer = mapper.mapAsList(personListe, Person.class);
        savePersonListService.save(personer);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "checkIdentList") })
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.POST)
    public Set<IdentMedStatus> checkIdentList(@RequestBody List<String> personIdentListe) {
        return sjekkIdenter.finnGyldigeOgLedigeIdenter(personIdentListe);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createPersoner") })
    @RequestMapping(value = "/createpersoner/{gruppeId}", method = RequestMethod.POST)
    public void createPersonerFraIdentliste(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> personIdentListe) {
        List<Person> personer = opprettPersonerFraIdenter.execute(personIdentListe);
        setNameOnPersonsService.execute(personer);
        setGruppeIdOnPersons.setGruppeId(personer, gruppeId);
        savePersonListService.save(personer);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGrupper") })
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSimpleGruppe> getGrupper() {
        List<Gruppe>  grupper = gruppeRepository.findAllByOrderByIdAsc();
        return mapper.mapAsList(grupper, RsSimpleGruppe.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppe") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        Gruppe gruppe = gruppeRepository.findById(gruppeId);
        return mapper.map(gruppe, RsGruppe.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createGruppe") })
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSimpleGruppe rsGruppe) {
        Gruppe gruppe = mapper.map(rsGruppe, Gruppe.class);
        gruppeRepository.save(gruppe);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteGruppe") })
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        gruppeRepository.deleteById(gruppeId);
    }

    @RequestMapping(value = "/tempdata", method = RequestMethod.GET)
    public void saveGrupper() {
        Gruppe gruppe = Gruppe.builder().beskrivelse("NORG2 testidenter HL3 som skal brukes til bla bla bla").navn("NORG2 testidenter HL3").build();
        Gruppe gruppe2 = Gruppe.builder().beskrivelse("MEDL2 nye testidenter for T som skal brukes til bla bla bla").navn("MEDL2 nye testidenter for T").build();

        Person.PersonBuilder personBuilder = Person.builder()
                .identtype("FNR")
                .kjonn('M')
                .fornavn("Ola")
                .mellomnavn("O")
                .etternavn("Nordmann")
                .statsborgerskap("000")
                .regdato(LocalDateTime.now());

        Person person = personBuilder
                .ident("12345678910")
                .gruppe(gruppe)
                .build();

        Person person2 = personBuilder
                .ident("12345678000")
                .fornavn("Kari")
                .gruppe(gruppe)
                .build();

        Person person3 = personBuilder
                .ident("12345670000")
                .fornavn("Per")
                .gruppe(gruppe2)
                .build();

        Person person4 = personBuilder
                .ident("12345600000")
                .fornavn("Pål")
                .gruppe(gruppe2)
                .build();

        List<Person> personer = new ArrayList<>(Arrays.asList(person, person2));
        List<Person> personer2 = new ArrayList<>(Arrays.asList(person3, person4));

        Tag tag = new Tag();
        tag.setNavn("NORG2");

        Tag tag2 = new Tag();
        tag2.setNavn("MEDL2");

        gruppe.setTags(Arrays.asList(tag));
        gruppe2.setTags(Arrays.asList(tag2));

        tag.setGrupper(Arrays.asList(gruppe));
        tag2.setGrupper(Arrays.asList(gruppe2));

        gruppe.setPersoner(personer);
        gruppe2.setPersoner(personer2);

        gruppeRepository.save(gruppe);
        gruppeRepository.save(gruppe2);

    }

}
