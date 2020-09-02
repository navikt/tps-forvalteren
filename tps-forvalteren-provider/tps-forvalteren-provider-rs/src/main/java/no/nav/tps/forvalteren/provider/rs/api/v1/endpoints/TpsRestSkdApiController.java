package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsInnvandringReq;
import no.nav.tps.forvalteren.service.command.exceptions.TpsServiceRutineException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingSender;

@RestController
@RequestMapping(value = "/api/tps/skd")
public class TpsRestSkdApiController {

    @Autowired
    private SkdMeldingSender skdMeldingSender;

    @Autowired
    private MapperFacade mapperFacade;

    @LogExceptions
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/innvandring")
    public SendSkdMeldingTilTpsResponse innvandrePerson(@RequestBody RsInnvandringReq req) {
        List<Person> person = newArrayList(mapperFacade.map(req.getPerson(), Person.class));
        List<SendSkdMeldingTilTpsResponse> response = skdMeldingSender.sendInnvandringsMeldinger(person, req.getEnvironments());

        if (response == null || !response.isEmpty()) {
            throw new TpsServiceRutineException("Noe gikk feil med innvandringsmeldingen");
        }

        return response.get(0);
    }

    @LogExceptions
    @PutMapping("/innvandring")
    public SendSkdMeldingTilTpsResponse innvandreUpdatePerson(@RequestBody RsInnvandringReq req) {
        List<Person> person = newArrayList(mapperFacade.map(req.getPerson(), Person.class));
        List<SendSkdMeldingTilTpsResponse> response = skdMeldingSender.sendUpdateInnvandringsMeldinger(person, req.getEnvironments());

        if (response == null || !response.isEmpty()) {
            throw new TpsServiceRutineException("Noe gikk feil med innvandring-oppdateringgsmeldingen");
        }

        return response.get(0);
    }
}
