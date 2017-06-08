package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonsByIdService;
import no.nav.tps.forvalteren.service.command.testdata.FindAllPersonService;
import no.nav.tps.forvalteren.service.command.testdata.OpprettTestdataPersoner;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SetNameOnPersonsService;
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
    private OpprettTestdataPersoner opprettTestdataPersoner;

    @Autowired
    private SetNameOnPersonsService setNameOnPersonsService;

    @Autowired
    private SavePersonListService savePersonListService;

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getAllPersons")})
    @RequestMapping(value = "/personer", method = RequestMethod.GET)
    public List<Person> getAllPersons(){
        return findAllPersonService.execute();
    }

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewPersons")})
    @RequestMapping(value = "/personer", method = RequestMethod.POST)
    public void createNewPersons(@RequestBody RsPersonKriterieRequest personKriterierListe) {
        List<Person> personerSomSkalPersisteres = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(personKriterierListe);
        setNameOnPersonsService.execute(personerSomSkalPersisteres);
        savePersonListService.save(personerSomSkalPersisteres);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deletePersons")})
    @RequestMapping(value = "/deletePersoner", method = RequestMethod.POST)
    public void deletePersons(@RequestBody RsPersonIdListe personIdListe) {

        deletePersonsByIdService.execute(personIdListe.getIds());
    }
}
