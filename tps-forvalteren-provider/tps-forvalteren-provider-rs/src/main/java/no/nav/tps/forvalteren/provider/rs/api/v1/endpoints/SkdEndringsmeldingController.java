package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ma.glasnost.orika.MapperFacade;
import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingAsText;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEdnringsmeldingIdListe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingIdListToTps;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateAndSaveSkdEndringsmeldingerFromText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateSkdEndringsmeldingFromType;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingByIdIn;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.DeleteSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindAllSkdEndringsmeldingGrupper;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.FindSkdEndringsmeldingGruppeById;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.GetLoggForGruppe;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SendEndringsmeldingGruppeToTps;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SyntetiserteSkdEndringsmeldingerService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.UpdateSkdEndringsmelding;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;

@Transactional
@RestController
@RequestMapping(value = "api/v1/endringsmelding/skd")
//@PreAuthorize("hasRole('ROLE_TPSF_SKDMELDING')")
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
    private UpdateSkdEndringsmelding updateSkdEndringsmelding;
    
    @Autowired
    private CreateSkdEndringsmeldingFromType createSkdEndringsmeldingFromType;
    
    @Autowired
    private CreateAndSaveSkdEndringsmeldingerFromText createAndSaveSkdEndringsmeldingerFromText;
    
    @Autowired
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;
    
    @Autowired
    private SendEndringsmeldingGruppeToTps sendEndringsmeldingGruppeToTps;
    
    @Autowired
    private GetLoggForGruppe getLoggForGruppe;
    
    @Autowired
    private SyntetiserteSkdEndringsmeldingerService syntetiserteSkdService;
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGrupper") })
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingGruppe> getGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = findAllSkdEndringsmeldingGrupper.execute();
        return mapper.mapAsList(grupper, RsSkdEndringsmeldingGruppe.class);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppe") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = findSkdEndringsmeldingGruppeById.execute(gruppeId);
        return mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createGruppe") })
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe) {
        SkdEndringsmeldingGruppe gruppe = mapper.map(rsSkdEndringsmeldingGruppe, SkdEndringsmeldingGruppe.class);
        saveSkdEndringsmeldingGruppe.execute(gruppe);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteGruppe") })
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        deleteSkdEndringsmeldingGruppeById.execute(gruppeId);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMelding") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.POST)
    public void createMeldingFromMeldingstype(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsNewSkdEndringsmelding rsNewSkdEndringsmelding) {
        createSkdEndringsmeldingFromType.execute(gruppeId, rsNewSkdEndringsmelding);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMeldingerFromText") })
    @RequestMapping(value = "/gruppe/{gruppeId}/raw", method = RequestMethod.POST)
    public void createMeldingerFromText(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsRawMeldinger meldingerAsText) {
        createAndSaveSkdEndringsmeldingerFromText.execute(gruppeId, meldingerAsText);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteMeldinger") })
    @RequestMapping(value = "/deletemeldinger", method = RequestMethod.POST)
    public void deleteSkdEndringsmeldinger(@RequestBody RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe) {
        deleteSkdEndringsmeldingByIdIn.execute(rsSkdEdnringsmeldingIdListe.getIds());
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updateMeldinger") })
    @RequestMapping(value = "/updatemeldinger", method = RequestMethod.POST)
    public void updateMeldinger(@RequestBody List<RsMeldingstype> meldinger) {
        updateSkdEndringsmelding.execute(meldinger);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "convertMelding") })
    @RequestMapping(value = "/convertmelding", method = RequestMethod.POST)
    public RsMeldingAsText convertMelding(@RequestBody RsMeldingstype rsMelding) {
        String melding = convertMeldingFromJsonToText.execute(rsMelding);
        return new RsMeldingAsText(melding);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendToTps") })
    @RequestMapping(value = "/send/{skdMeldingGruppeId}", method = RequestMethod.POST)
    public AvspillingResponse sendToTps(@PathVariable Long skdMeldingGruppeId, @RequestBody RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps) {
        return sendEndringsmeldingGruppeToTps.execute(skdMeldingGruppeId, skdEndringsmeldingIdListToTps);
    }
    
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getLog") })
    @RequestMapping(value = "/gruppe/{gruppeId}/tpslogg", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingLogg> getLogg(@PathVariable("gruppeId") Long gruppeId) {
        List<SkdEndringsmeldingLogg> log = getLoggForGruppe.execute(gruppeId);
        return mapper.mapAsList(log, RsSkdEndringsmeldingLogg.class);
    }
    
    @PostMapping("syntetiserte/{avspillergruppeId}")
    public void swapLoepenrMedIdentogLagre(@PathVariable Long avspillergruppeId, @RequestBody @Valid List<RsMeldingstype> skdmeldingerMedLoepenummer) {
        syntetiserteSkdService.swapLoepenrMedIdentogLagre(avspillergruppeId, skdmeldingerMedLoepenummer);
    }
}