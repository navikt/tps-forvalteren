package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;
import no.nav.tps.vedlikehold.service.command.tps.endringsmeldinger.GetTpsEndringsmeldingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by F148888 on 24.10.2016.
 */

@RestController
@RequestMapping(value = "api/v1")
public class GetEndringsmeldingerController {

    @Autowired
    private GetTpsEndringsmeldingerService getTpsEndringsmeldingerService;

    @LogExceptions
    @RequestMapping(value = "/endringsmeldinger", method = RequestMethod.GET)
    public List<TpsEndringsmelding> getTpsEndringsmeldinger(){
        return getTpsEndringsmeldingerService.execute();
    }
}
