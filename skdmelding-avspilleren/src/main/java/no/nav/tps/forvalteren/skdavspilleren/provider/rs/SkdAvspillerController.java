package no.nav.tps.forvalteren.skdavspilleren.provider.rs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.skdavspilleren.provider.rs.request.StartAvspillingRequest;

@RestController
@RequestMapping("avspilleren/skd")
public class SkdAvspillerController {
    
    @PostMapping("start")
    public void startAvspillingAvSkdmeldingTilMiljoe(@RequestBody StartAvspillingRequest startAvspillingRequest) {
        System.out.println(startAvspillingRequest.getGruppeId());
    }
}
