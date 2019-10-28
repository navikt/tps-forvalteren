package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
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
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.ConvertMeldingFromJsonToText;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateAndSaveSkdEndringsmeldingerFromTextService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.CreateSkdEndringsmeldingFromTypeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.GetLoggForGruppeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SaveSkdEndringsmeldingerService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SendEndringsmeldingToTpsService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SkdEndringsmeldingService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.SkdEndringsmeldingsgruppeService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.UpdateSkdEndringsmeldingService;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeTooLargeException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.TpsPersonService;

@Transactional
@RestController
@RequestMapping(value = "api/v1/endringsmelding/skd")
@PreAuthorize("hasRole('ROLE_TPSF_SKDMELDING')")
public class SkdEndringsmeldingController {

    private static final String REST_SERVICE_NAME = "testdata";
    private static final int MAX_ANTALL_MELDINGER_UTEN_PAGINERING = 30000;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;

    @Autowired
    private UpdateSkdEndringsmeldingService updateSkdEndringsmeldingService;

    @Autowired
    private CreateSkdEndringsmeldingFromTypeService createSkdEndringsmeldingFromTypeService;

    @Autowired
    private CreateAndSaveSkdEndringsmeldingerFromTextService createAndSaveSkdEndringsmeldingerFromTextService;

    @Autowired
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Autowired
    private SendEndringsmeldingToTpsService sendEndringsmeldingToTpsService;

    @Autowired
    private GetLoggForGruppeService getLoggForGruppeService;

    @Autowired
    private SkdEndringsmeldingService skdEndringsmeldingService;

    @Autowired
    private SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;

    @Autowired
    private TpsPersonService tpsPersonService;

    @Autowired
    private IdentpoolService identpoolService;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGrupper") })
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingGruppe> getGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = skdEndringsmeldingsgruppeService.findAllGrupper();
        return mapper.mapAsList(grupper, RsSkdEndringsmeldingGruppe.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppe") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingsgruppeService.findGruppeById(gruppeId);
        return mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getGruppePaginert") })
    @RequestMapping(value = "/gruppe/meldinger/{gruppeId}/{pageNumber}", method = RequestMethod.GET)
    public List<RsMeldingstype> getGruppePaginert(@PathVariable("gruppeId") Long gruppeId, @PathVariable("pageNumber") int pageNumber) throws IOException {
        List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(gruppeId, pageNumber);
        return skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "klonAvspillergruppe") })
    @RequestMapping(value = "/gruppe/kloning/{originalGruppeId}", method = RequestMethod.POST)
    public void klonAvspillergruppe(@PathVariable("originalGruppeId") Long originalGruppeId, @RequestBody String nyttNavn) throws IOException {
        SkdEndringsmeldingGruppe originalGruppe = skdEndringsmeldingsgruppeService.findGruppeById(originalGruppeId);

        RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe = skdEndringsmeldingsgruppeService.konfigurerKlonAvGruppe(originalGruppe, nyttNavn);
        SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = mapper.map(rsSkdEndringsmeldingGruppe, SkdEndringsmeldingGruppe.class);

        skdEndringsmeldingsgruppeService.save(skdEndringsmeldingGruppe);

        int antallMeldingerIAvspillergruppe = skdEndringsmeldingService.countMeldingerInGruppe(originalGruppeId);
        int antallSiderIAvspillergruppe = skdEndringsmeldingService.getAntallSiderIGruppe(antallMeldingerIAvspillergruppe);

        for (int i = 0; i < antallSiderIAvspillergruppe; i++) {
            List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(originalGruppeId, i);

            List<RsMeldingstype> meldinger = skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger);
            meldinger.forEach(rsMeldingstype -> rsMeldingstype.setId(null));

            saveSkdEndringsmeldingerService.save(meldinger, skdEndringsmeldingGruppe.getId());
        }
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createGruppe") })
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe) {
        SkdEndringsmeldingGruppe gruppe = mapper.map(rsSkdEndringsmeldingGruppe, SkdEndringsmeldingGruppe.class);
        skdEndringsmeldingsgruppeService.save(gruppe);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteGruppe") })
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        skdEndringsmeldingsgruppeService.deleteGruppeById(gruppeId);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMelding") })
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.POST)
    public void createMeldingFromMeldingstype(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsNewSkdEndringsmelding rsNewSkdEndringsmelding) {
        createSkdEndringsmeldingFromTypeService.execute(gruppeId, rsNewSkdEndringsmelding);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createMeldingerFromText") })
    @RequestMapping(value = "/gruppe/{gruppeId}/raw", method = RequestMethod.POST)
    public void createMeldingerFromText(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsRawMeldinger meldingerAsText) {
        createAndSaveSkdEndringsmeldingerFromTextService.execute(gruppeId, meldingerAsText);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteMeldinger") })
    @RequestMapping(value = "/deletemeldinger", method = RequestMethod.POST)
    public void deleteSkdEndringsmeldinger(@RequestBody RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe) {
        skdEndringsmeldingService.deleteById(rsSkdEdnringsmeldingIdListe.getIds());
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updateMeldinger") })
    @RequestMapping(value = "/updatemeldinger", method = RequestMethod.POST)
    public void updateMeldinger(@RequestBody List<RsMeldingstype> meldinger) {
        updateSkdEndringsmeldingService.update(meldinger);
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
        return sendEndringsmeldingToTpsService.execute(skdMeldingGruppeId, skdEndringsmeldingIdListToTps);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteFromTps") })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/deleteFromTps")
    public void deleteIdentsFromTps(@RequestParam(required = false, defaultValue = "") List<String> miljoer, @RequestParam List<String> identer) {
        tpsPersonService.sendDeletePersonMeldinger(miljoer, new HashSet<>(identer));
        identpoolService.recycleIdents(new HashSet<>(identer));
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getLog") })
    @RequestMapping(value = "/gruppe/{gruppeId}/tpslogg", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingLogg> getLogg(@PathVariable("gruppeId") Long gruppeId) {
        int antallMeldingerIGruppe = skdEndringsmeldingService.countMeldingerInGruppe(gruppeId);
        if (antallMeldingerIGruppe > MAX_ANTALL_MELDINGER_UTEN_PAGINERING) {
            throw new SkdEndringsmeldingGruppeTooLargeException("Kan ikke hente innsendingslogg på gruppe med flere enn " + MAX_ANTALL_MELDINGER_UTEN_PAGINERING + " meldinger på " +
                    "grunn av minnebegrensninger. Denne gruppen inneholder " + antallMeldingerIGruppe + " meldinger.");
        } else {
            List<SkdEndringsmeldingLogg> log = getLoggForGruppeService.execute(gruppeId);
            return mapper.mapAsList(log, RsSkdEndringsmeldingLogg.class);
        }
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getLog") })
    @RequestMapping(value = "/meldinger/{gruppeId}", method = RequestMethod.GET)
    public List<Long> getMeldingIder(@PathVariable("gruppeId") Long gruppeId) {
        return skdEndringsmeldingService.findAllMeldingIdsInGruppe(gruppeId);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getLog") })
    @RequestMapping(value = "/meldinger/{gruppeId}", method = RequestMethod.POST)
    public List<Long> getMeldingIderMedFnr(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> identer) {
        return skdEndringsmeldingService.finnAlleMeldingIderMedFoedselsnummer(gruppeId, identer);
    }

    @ApiOperation("Lagrer Skd-endringsmeldingene i TPSF databasen.")
    @LogExceptions
    @PostMapping("save/{gruppeId}")
    public List<Long> saveSkdEndringsmeldingerInTPSF(@PathVariable Long gruppeId, @RequestBody @Valid List<RsMeldingstype> rsSkdMeldinger) {
        return saveSkdEndringsmeldingerService.save(rsSkdMeldinger, gruppeId);
    }

    @LogExceptions
    @GetMapping("identer/{gruppeId}")
    public Set<String> filtrerIdenterPaaAarsakskodeOgTransaksjonstype(@PathVariable Long gruppeId, @RequestParam List<String> aarsakskode, @RequestParam String transaksjonstype) {
        return skdEndringsmeldingService.filtrerIdenterPaaAarsakskodeOgTransaksjonstype(gruppeId, aarsakskode, transaksjonstype);
    }
}