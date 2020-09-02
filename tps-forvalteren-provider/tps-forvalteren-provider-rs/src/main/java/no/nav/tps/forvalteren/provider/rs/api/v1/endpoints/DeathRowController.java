package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;
import no.nav.tps.forvalteren.domain.rs.RsDeathRowCheckIdent;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.CreateDodsmelding;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.FindAllDeathRows;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.SendDodsmeldingTilTpsService;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.UpdateDeathRow;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenterForDodsmelding;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Transactional
@RestController
@RequestMapping(value = "api/v1/doedsmelding")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class DeathRowController {

    private static final String REST_SERVICE_NAME = "testdata";

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private CreateDodsmelding createDodsmelding;

    @Autowired
    private SjekkIdenterForDodsmelding sjekkIdenterForDodsmelding;

    @Autowired
    private FindAllDeathRows findAllDeathRows;

    @Autowired
    private UpdateDeathRow updateDeathRow;

    @Autowired
    private SendDodsmeldingTilTpsService sendDodsmeldingTilTpsService;

    @Autowired
    private UserContextHolder userContextHolder;

    @LogExceptions
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.POST)
    public Set<IdentMedStatus> checkIdList(@RequestBody RsDeathRowCheckIdent rsDeathRowCheckIdent) {
        return sjekkIdenterForDodsmelding.finnGyldigeOgLedigeIdenterForDoedsmeldinger(rsDeathRowCheckIdent.getIdenter(), rsDeathRowCheckIdent.getMiljoe());
    }

    @LogExceptions
    @RequestMapping(value = "/opprett", method = RequestMethod.POST)
    public void createMelding(@RequestBody RsDeathRowBulk rsDeathRowBulk) {
        List<DeathRow> deathRowList = mapper.map(rsDeathRowBulk, List.class);
        for (DeathRow deathrow : deathRowList) {
            deathrow.setEndretAv(userContextHolder.getUser().getUsername());
            createDodsmelding.execute(deathrow);
        }
    }

    @LogExceptions
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void deleteMelding(@PathVariable("id") Long id) {
        deathRowRepository.deleteById(id);
    }

    @LogExceptions
    @RequestMapping(value = "/meldinger", method = RequestMethod.GET)
    public List<RsDeathRow> getMeldingLogg() {
        List<DeathRow> deathRowList = findAllDeathRows.execute();
        return mapper.mapAsList(deathRowList, RsDeathRow.class);
    }

    @LogExceptions
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RsDeathRow updateMelding(@RequestBody RsDeathRow rsDeathRow) {
        DeathRow mappedDeathRow = mapper.map(rsDeathRow, DeathRow.class);
        mappedDeathRow.setStatus("Ikke sendt");
        DeathRow updatedDeathRow = updateDeathRow.execute(mappedDeathRow);
        return mapper.map(updatedDeathRow, RsDeathRow.class);
    }

    @LogExceptions
    @Transactional(dontRollbackOn = TpsfFunctionalException.class)
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void sendToTps() {
        sendDodsmeldingTilTpsService.execute();
    }

    @LogExceptions
    @RequestMapping(value = "/clearskjema/{miljoe}", method = RequestMethod.POST)
    public void tomSkjema(@PathVariable("miljoe") String miljoe) {
        if (!"undefined".equals(miljoe)) {
            deathRowRepository.deleteAllByMiljoe(miljoe);
        } else {
            deathRowRepository.deleteAll();
        }
    }
}
