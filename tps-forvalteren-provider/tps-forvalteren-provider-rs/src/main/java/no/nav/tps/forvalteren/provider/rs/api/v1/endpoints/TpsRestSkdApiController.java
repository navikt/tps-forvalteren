package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsSkdFodseslmelding;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsInnvandringReq;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsVigselPartner;
import no.nav.tps.forvalteren.service.command.exceptions.TpsServiceRutineException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/tps/skd")
public class TpsRestSkdApiController {

    @Autowired
    private SkdMeldingSender skdMeldingSender;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private TpsRestApiController tpsRestApiController;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/innvandring")
    public SendSkdMeldingTilTpsResponse innvandrePerson(@RequestBody RsInnvandringReq req){
        List<Person> person = Arrays.asList(mapperFacade.map(req.getPerson(), Person.class));
        List<SendSkdMeldingTilTpsResponse> response = skdMeldingSender.sendInnvandringsMeldinger(person ,req.getEnvironments());

        if(response == null || !response.isEmpty()){
            throw new TpsServiceRutineException("Noe gikk feil med innvandringsmeldingen");
        }

        return response.get(0);
    }

    @PutMapping("/innvandring")
    public SendSkdMeldingTilTpsResponse innvandreUpdatePerson(@RequestBody RsInnvandringReq req){
        List<Person> person = Arrays.asList(mapperFacade.map(req.getPerson(), Person.class));
        List<SendSkdMeldingTilTpsResponse> response = skdMeldingSender.sendUpdateInnvandringsMeldinger(person , req.getEnvironments());

        if(response == null || !response.isEmpty()){
            throw new TpsServiceRutineException("Noe gikk feil med innvandring-oppdateringgsmeldingen");
        }

        return response.get(0);
    }
//
//    @PostMapping("/fodsel")
//    public SendSkdMeldingTilTpsResponse sendFodselsmelding(@RequestBody RsSkdFodseslmelding fodselRequest){
//        Map responseMother = tpsRestApiController.fetchPersonopplysningerS004(fodselRequest.getMorFodselsnumemr(), "A0", "2018-09-09", fodselRequest.getEnvironment());
//        Person mother =null;
//        skdMeldingSender.sendFoedselsMeldinger( , new HashSet<>(Arrays.asList(fodselRequest.getEnvironment())));
//        return null;
//    }
}
