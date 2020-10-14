package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsAliasRequest;
import no.nav.tps.forvalteren.domain.rs.RsAliasResponse;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonnavn;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonLagreRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.ImporterPersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsIdenterMiljoer;
import no.nav.tps.forvalteren.domain.rs.dolly.RsOppdaterPersonResponse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonMiljoe;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly.ListExtractorKommaSeperated;
import no.nav.tps.forvalteren.service.command.excel.ExcelService;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenterService;
import no.nav.tps.forvalteren.service.command.testdata.response.CheckIdentResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.restreq.EndrePersonBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.restreq.ImporterPersonService;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonIdenthistorikkService;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonerBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.restreq.RelasjonEksisterendePersonerBestillingService;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/dolly/testdata")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class TestdataBestillingsController {

    private static final String EXCEL_FEILMELDING = "Feil ved pakking av Excel-fil";

    private final PersonerBestillingService personerBestillingService;
    private final MapperFacade mapperFacade;
    private final LagreTilTpsService lagreTilTps;
    private final ListExtractorKommaSeperated listExtractorKommaSeperated;
    private final SjekkIdenterService sjekkIdenterService;
    private final ExcelService excelService;
    private final PersonService personService;
    private final PersonIdenthistorikkService personIdenthistorikkService;
    private final EndrePersonBestillingService endrePersonBestillingService;
    private final RelasjonEksisterendePersonerBestillingService relasjonEksisterendePersonerBestillingService;
    private final ImporterPersonService importerPersonService;

    @Transactional
    @LogExceptions
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/personer", method = RequestMethod.POST)
    public List<String> createPersonerFraBestillingskriterier(@RequestBody RsPersonBestillingKriteriumRequest personKriteriumRequest) {
        List<Person> personer = personerBestillingService.createTpsfPersonFromRequest(personKriteriumRequest);
        return personer.stream().map(Person::getIdent).collect(toList());
    }

    @Transactional
    @LogExceptions
    @RequestMapping(value = "/tilTpsFlere", method = RequestMethod.POST)
    public RsSkdMeldingResponse sendFlerePersonerTilTps(@RequestBody RsIdenterMiljoer tpsRequest) {
        List<Person> personer = personService.getPersonerByIdenter(tpsRequest.getIdenter());
        return lagreTilTps.execute(personer, newHashSet(tpsRequest.getMiljoer()));
    }

    @LogExceptions
    @RequestMapping(value = "/personerdata", method = RequestMethod.GET)
    public List<RsPerson> getPersons(@RequestParam("identer") String personer) {
        List<String> identer = listExtractorKommaSeperated.extractIdenter(personer);
        return mapperFacade.mapAsList(personService.getPersonerByIdenter(identer), RsPerson.class);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/hentpersoner", method = RequestMethod.POST)
    public List<RsPerson> hentPersoner(@RequestBody List<String> identer) {

        return mapperFacade.mapAsList(personService.getPersonerByIdenter(identer), RsPerson.class);
    }

    @LogExceptions
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.POST)
    public CheckIdentResponse checkIdentList(@RequestBody List<String> identer) {
        return sjekkIdenterService.finnLedigeIdenter(identer);
    }

    @LogExceptions
    @RequestMapping(value = "/excel", method = RequestMethod.POST)
    public ResponseEntity<Resource> getExcelForIdenter(@RequestBody List<String> identer) {

        Resource resource = excelService.getPersonFile(identer);
        try {
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (IOException e) {
            log.error(EXCEL_FEILMELDING, e);
            throw new TpsfFunctionalException(EXCEL_FEILMELDING, e);
        }
    }

    @LogExceptions
    @RequestMapping(value = "/personer", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "deletePersons")
    public void slettPersoner(@RequestParam(required = false, defaultValue = "") List<String> miljoer, @RequestParam List<String> identer) {
        personService.deletePersons(miljoer, identer);
    }

    @LogExceptions
    @RequestMapping(value = "/aliaser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RsAliasResponse opprettAliaser(@RequestBody RsAliasRequest request) {

        return personIdenthistorikkService.prepareAliases(request);
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/leggtilpaaperson", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RsOppdaterPersonResponse oppdaterPerson(@RequestParam String ident, @RequestBody RsPersonBestillingKriteriumRequest request) {

        return endrePersonBestillingService.execute(ident, request);
    }

    @LogExceptions
    @RequestMapping(value = "/relasjonperson", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<String> relasjonPerson(@RequestParam String ident, @RequestBody RsPersonBestillingRelasjonRequest request) {

        return relasjonEksisterendePersonerBestillingService.makeRelasjon(ident, request);
    }

    @LogExceptions
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<RsPersonMiljoe> hentPersonFraTps(@RequestBody ImporterPersonRequest request) {

        Map<String, Person> miljoePerson = importerPersonService.importFraTps(request);
        return miljoePerson.entrySet().parallelStream()
                .map(entry -> RsPersonMiljoe.builder()
                        .environment(entry.getKey())
                        .person(mapperFacade.map(entry.getValue(), RsPerson.class))
                        .build())
                .collect(Collectors.toList());
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/import/lagre", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public RsPerson importerPerson(@RequestBody ImporterPersonLagreRequest request) {

        return mapperFacade.map(importerPersonService.importFraTpsOgLagre(request), RsPerson.class);
    }

    @LogExceptions
    @Transactional(Transactional.TxType.NEVER)
    @GetMapping(value = "/soekperson")
    @Operation(method = "Søk/let etter personer", description = "Søk basert på fragment av ident og/eller en eller to navn")
    @ResponseStatus(HttpStatus.OK)
    public List<RsPersonnavn> soekperson(@Parameter(description = "Partiell ident og/eller flere navn",
            examples = @ExampleObject(value = "nat 324 bær")) String fragment) {

        return mapperFacade.mapAsList(personService.searchPerson(fragment), RsPersonnavn.class);
    }
}