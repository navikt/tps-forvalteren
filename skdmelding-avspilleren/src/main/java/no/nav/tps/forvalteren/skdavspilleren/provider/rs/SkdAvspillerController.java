package no.nav.tps.forvalteren.skdavspilleren.provider.rs;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.service.SkdAvspillerService;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@RestController
@RequestMapping("avspilleren/skd")
public class SkdAvspillerController {
    
    @Autowired
    private SkdAvspillerService avspillerService;
    
    @PostMapping("start")
    public void startAvspillingAvSkdmeldingTilMiljoe(@RequestBody StartAvspillingRequest startAvspillingRequest) {
        avspillerService.start(startAvspillingRequest);
    }
    
    @GetMapping("grupper")
    public Iterable<Avspillergruppe> getAllAvspillergrupper() {
        return avspillerService.getAllAvspillergrupper();
    }
}
