package no.nav.tps.forvalteren.skdavspilleren.provider.rs;

import java.time.LocalDateTime;
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
import no.nav.tps.forvalteren.skdavspilleren.service.requests.OpprettAvspillergruppeRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@RestController
@RequestMapping("avspilleren/skd")
public class SkdAvspillerController {
    
    @Autowired
    private SkdAvspillerService avspillerService;
    
    @Autowired
    private MapperFacade mapper;
    
    @PostMapping("start")
    public void startAvspillingAvSkdmeldingTilMiljoe(@RequestBody StartAvspillingRequest startAvspillingRequest) {
        avspillerService.start(startAvspillingRequest);
    }
    
    @GetMapping("grupper")
    public List<AvspillergruppeResponse> getAllAvspillergrupper() {
        return mapper.mapAsList(avspillerService.getAllAvspillergrupper(), AvspillergruppeResponse.class);
    }
    
    @PostMapping("gruppe")
    public void opprettGruppe(@RequestBody OpprettAvspillergruppeRequest request) {
        Avspillergruppe avspillergruppe = Avspillergruppe.builder().navn(request.getNavn()).beskrivelse(request.getBeskrivelse()).build();
        avspillergruppe.setOpprettetAv(request.getOpprettetAv());
        avspillergruppe.setEndretAv(request.getOpprettetAv());
        avspillergruppe.setEndretDato(LocalDateTime.now());
        avspillergruppe.setOpprettetDato(LocalDateTime.now());
        
        avspillerService.opprettGruppe(avspillergruppe);
    }
}
