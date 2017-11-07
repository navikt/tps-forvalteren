package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEdnringsmeldingIdListe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateAndSaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingByIdIn;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindAllSkdEndringsmeldingGrupper;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmelding;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingGruppe;

@Transactional
@RestController
@RequestMapping(value = "api/v1/endringsmelding/skd")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class SkdEndringsmeldingController {

    private static final String REST_SERVICE_NAME = "testdata";

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private FindAllSkdEndringsmeldingGrupper findAllSkdEndringsmeldingGrupper;

    @Autowired
    private FindSkdEndringsmeldingGruppeById findSkdEndringsmeldingGruppeById;

    @Autowired
    private SaveSkdEndringsmeldingGruppe saveSkdEndringsmeldingGruppe;

    @Autowired
    private DeleteSkdEndringsmeldingGruppeById deleteSkdEndringsmeldingGruppeById;

    @Autowired
    private DeleteSkdEndringsmeldingByIdIn deleteSkdEndringsmeldingByIdIn;

    @Autowired
    private SaveSkdEndringsmelding saveSkdEndringsmelding;

    @Autowired
    private CreateAndSaveSkdEndringsmeldingerFromText createAndSaveSkdEndringsmeldingerFromText;

    @PreAuthorize("hasRole('ROLE_ACCESS')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGrupper") })
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingGruppe> getGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = findAllSkdEndringsmeldingGrupper.execute();
        return mapper.mapAsList(grupper, RsSkdEndringsmeldingGruppe.class);
    }

    @PreAuthorize("hasRole('ROLE_ACCESS')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppe") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = findSkdEndringsmeldingGruppeById.execute(gruppeId);
        return mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class);
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createGruppe") })
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSkdEndringsmeldingGruppe rsGruppe) {
        SkdEndringsmeldingGruppe gruppe = mapper.map(rsGruppe, SkdEndringsmeldingGruppe.class);
        saveSkdEndringsmeldingGruppe.execute(gruppe);
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteGruppe") })
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        deleteSkdEndringsmeldingGruppeById.execute(gruppeId);
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteMeldinger") })
    @RequestMapping(value = "/deletemeldinger", method = RequestMethod.POST)
    public void deleteSkdEndringsmeldinger(@RequestBody RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe) {
        deleteSkdEndringsmeldingByIdIn.execute(rsSkdEdnringsmeldingIdListe.getIds());
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMelding") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.POST)
    public void createMelding(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsMeldingstype melding) {
        saveSkdEndringsmelding.execute(gruppeId, melding);
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMeldingerFromText") })
    @RequestMapping(value = "/gruppe/{gruppeId}/raw", method = RequestMethod.POST)
    public void createMeldingerFromText(@PathVariable("gruppeId") Long gruppeId, @RequestBody String meldingerAsText) {
        createAndSaveSkdEndringsmeldingerFromText.execute(meldingerAsText, gruppeId);
    }

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updateMeldinger") })
    @RequestMapping(value = "/updatemeldinger", method = RequestMethod.POST)
    public void updateMeldinger(@RequestBody List<RsMeldingstype> meldinger) {

    }

}
