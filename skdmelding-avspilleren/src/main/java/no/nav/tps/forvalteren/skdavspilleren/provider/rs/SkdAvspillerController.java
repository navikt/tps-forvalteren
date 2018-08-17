package no.nav.tps.forvalteren.skdavspilleren.provider.rs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.provider.response.AvspillergruppeResponse;
import no.nav.tps.forvalteren.skdavspilleren.service.SkdAvspillerService;
import no.nav.tps.forvalteren.skdavspilleren.service.SkdAvspillergruppeService;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.OpprettAvspillergruppeRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.response.StartAvspillingResponse;

@RestController
@RequestMapping("api/v1/avspilleren/skd")
public class SkdAvspillerController {
    
    @Autowired
    private SkdAvspillerService avspillerService;
    
    @Autowired
    private SkdAvspillergruppeService avspillergruppeService;
    
    @Autowired
    private MapperFacade mapper;
    
    @PostMapping("start")
    public StartAvspillingResponse startAvspillingAvSkdmeldingTilMiljoe(@RequestBody StartAvspillingRequest startAvspillingRequest) {
        return avspillerService.start(startAvspillingRequest);
    }
    
    @GetMapping("grupper")
    public List<AvspillergruppeResponse> getAllAvspillergrupper() {
        return mapper.mapAsList(avspillergruppeService.getAllAvspillergrupper(), AvspillergruppeResponse.class);
    }
    
    @PostMapping("gruppe")
    public void opprettGruppe(@RequestBody OpprettAvspillergruppeRequest request) {
        Avspillergruppe avspillergruppe = Avspillergruppe.builder().navn(request.getNavn()).beskrivelse(request.getBeskrivelse()).build();
        avspillergruppeService.opprettGruppe(avspillergruppe);
    }
}
