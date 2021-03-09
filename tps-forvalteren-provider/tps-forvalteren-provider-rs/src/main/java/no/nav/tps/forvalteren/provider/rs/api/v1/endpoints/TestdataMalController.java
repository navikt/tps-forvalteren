package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonmalRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdatamal.CreateTestdataPerson;

@RestController
@RequestMapping(value = "api/v1/testdatamal")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class TestdataMalController {

    private static final String REST_SERVICE_NAME = "testdatamal";

    @Autowired
    private PersonmalRepository personmalRepository;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private CreateTestdataPerson createTestdataPerson;

    @LogExceptions
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createTestdataMal(@RequestBody RsPersonMal rsPersonMal) {
        Personmal personmal = mapper.map(rsPersonMal, Personmal.class);

        personmalRepository.save(personmal);
    }

    @LogExceptions
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateTestdataMal() {

        throw new TpsfFunctionalException("Funksjonen er ikke implementert");
    }

    @LogExceptions
    @Transactional
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void deleteTestdataMal(@PathVariable("id") Long id) {

        personmalRepository.deleteById(id);
    }

    @LogExceptions
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public RsPersonMal getTestdataMal(@PathVariable("id") Long id) {

        return mapper.map(personmalRepository.findById(id), RsPersonMal.class);
    }

    @LogExceptions
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public List getAllTestdataMal() {
        List<Personmal> personmalList = personmalRepository.findAll();
        List<RsPersonMal> rsPersonMalList = Lists.newArrayListWithExpectedSize(personmalList.size());

        for (Personmal personmal : personmalList) {
            rsPersonMalList.add(mapper.map(personmal, RsPersonMal.class));
        }
        return rsPersonMalList;
    }

    @LogExceptions
    @RequestMapping(value = "/mal/personer/{gruppeId}", method = RequestMethod.POST)
    public void createNewPersonsFromMal(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsPersonMalRequest inputPersonRequest) {

        createTestdataPerson.execute(gruppeId, inputPersonRequest);
    }
}
