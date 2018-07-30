package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsInnvandringReq;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsVigselPartner;
import no.nav.tps.forvalteren.service.command.exceptions.TpsServiceRutineException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingSender;

import java.util.ArrayList;
import java.util.Arrays;
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

@RestController()
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

        if(response != null && response.size() >0){
            return response.get(0);
        }

        throw new TpsServiceRutineException("Noe gikk feil med innvandringsmeldingen");
    }

    @PutMapping("/innvandring")
    public SendSkdMeldingTilTpsResponse innvandreUpdatePerson(@RequestBody RsInnvandringReq req){
        List<Person> person = Arrays.asList(mapperFacade.map(req.getPerson(), Person.class));
        List<SendSkdMeldingTilTpsResponse> response = skdMeldingSender.sendUpdateInnvandringsMeldinger(person , req.getEnvironments());

        if(response != null && response.size() >0){
            return response.get(0);
        }

        throw new TpsServiceRutineException("Noe gikk feil med innvandring-oppdateringgsmeldingen");
    }

    @PutMapping("/vigsel-og-partnerskap")
    public List<Map> vigselOgPartnerskap(@RequestBody RsVigselPartner req){
        List<Map> mapper = new ArrayList<>();

//        skdMeldingSender.sendRelasjonsmeldinger()
//        req.getEnvironments().forEach(e -> {
//            try{
//                Map<String, Object> map  = tpsRestApiController.fetchKjerneinfoPaaPersonS610(req.getPersonIdent(), "A0", e);
//                mapper.add(map);
//            } catch (Exception ex){}
//        });

        return mapper;
    }
}
