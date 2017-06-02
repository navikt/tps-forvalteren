package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterierListe;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonsByIdService;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;
import no.nav.tps.forvalteren.service.command.testdata.FilterPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.FindAllPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

@RestController
@RequestMapping(value = "api/v1/testdata")
public class TestdataController {

    private static final String REST_SERVICE_NAME = "testdata";


    @Autowired
    private FindAllPersonService findAllPersonService;

    @Autowired
    private DeletePersonsByIdService deletePersonsByIdService;

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    @Autowired
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getAllPersons")})
    @RequestMapping(value = "/personer", method = RequestMethod.GET)
    public List<Person> getAllPersons(){
        return findAllPersonService.execute();
    }

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersons")})
    @RequestMapping(value = "/personer", method = RequestMethod.POST)
    public void createNewPersons(@RequestBody RsPersonKriterierListe personKriterierListe) {
        return;
    }

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons")})
    @RequestMapping(value = "/personer", method = RequestMethod.DELETE)
    public void deletePersons(@RequestBody List<Long> personIdListe) {

        deletePersonsByIdService.execute((Long[]) personIdListe.toArray());

    }
}