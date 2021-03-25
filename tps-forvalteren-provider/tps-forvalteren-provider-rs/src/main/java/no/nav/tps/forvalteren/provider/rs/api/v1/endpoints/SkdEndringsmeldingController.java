package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.util.Objects.nonNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
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
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/endringsmelding/skd")
@PreAuthorize("hasRole('ROLE_TPSF_SKDMELDING')")
public class SkdEndringsmeldingController {

    private static final String REST_SERVICE_NAME = "testdata";
    private static final int MAX_ANTALL_MELDINGER_UTEN_PAGINERING = 30000;

    private final MapperFacade mapper;
    private final SkdEndringsmeldingsgruppeService skdEndringsmeldingsgruppeService;
    private final UpdateSkdEndringsmeldingService updateSkdEndringsmeldingService;
    private final CreateSkdEndringsmeldingFromTypeService createSkdEndringsmeldingFromTypeService;
    private final CreateAndSaveSkdEndringsmeldingerFromTextService createAndSaveSkdEndringsmeldingerFromTextService;
    private final ConvertMeldingFromJsonToText convertMeldingFromJsonToText;
    private final SendEndringsmeldingToTpsService sendEndringsmeldingToTpsService;
    private final GetLoggForGruppeService getLoggForGruppeService;
    private final SkdEndringsmeldingService skdEndringsmeldingService;
    private final SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;
    private final TpsPersonService tpsPersonService;
    private final IdentpoolService identpoolService;
final
    @LogExceptions
    @RequestMapping(value = "/grupper", method = RequestMethod.GET)
    public List<RsSkdEndringsmeldingGruppe> getGrupper() {
        return skdEndringsmeldingsgruppeService.findAllGrupper();
    }

    @LogExceptions
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.GET)
    public RsSkdEndringsmeldingGruppe getGruppe(@PathVariable("gruppeId") Long gruppeId) {
        int antallMeldingerIGruppe = skdEndringsmeldingService.countMeldingerInGruppe(gruppeId);
        if (antallMeldingerIGruppe > MAX_ANTALL_MELDINGER_UTEN_PAGINERING) {
            throw new SkdEndringsmeldingGruppeTooLargeException("Kan ikke hente gruppe med flere enn " + MAX_ANTALL_MELDINGER_UTEN_PAGINERING + " meldinger på " +
                    "grunn av minnebegrensninger. Denne gruppen inneholder " + antallMeldingerIGruppe + " meldinger. Vennligst bruk endepunkt " +
                    "'/gruppe/meldinger/{gruppeId}/{pageNumber}' for å hente meldinger i denne gruppen. Frontend foreløpig ikke implementert for dette endepunktet.");
        } else {
            SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingsgruppeService.findGruppeById(gruppeId);
            return mapper.map(gruppe, RsSkdEndringsmeldingGruppe.class);
        }
    }

    @LogExceptions
    @RequestMapping(value = "/gruppe/meldinger/{gruppeId}/{pageNumber}", method = RequestMethod.GET)
    public List<RsMeldingstype> getGruppePaginert(@PathVariable("gruppeId") Long gruppeId, @PathVariable("pageNumber") int pageNumber) throws IOException {
        List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingService.findSkdEndringsmeldingerOnPage(gruppeId, pageNumber);
        return skdEndringsmeldingService.convertSkdEndringsmeldingerToRsMeldingstyper(skdEndringsmeldinger);
    }

    @LogExceptions
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
    @RequestMapping(value = "/gruppe", method = RequestMethod.POST)
    public void createGruppe(@RequestBody RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe) {
        SkdEndringsmeldingGruppe gruppe = mapper.map(rsSkdEndringsmeldingGruppe, SkdEndringsmeldingGruppe.class);
        skdEndringsmeldingsgruppeService.save(gruppe);
    }

    @LogExceptions
    @RequestMapping(value = "/deletegruppe/{gruppeId}", method = RequestMethod.POST)
    public void deleteGruppe(@PathVariable("gruppeId") Long gruppeId) {
        skdEndringsmeldingsgruppeService.deleteGruppeById(gruppeId);
    }

    @LogExceptions
    @RequestMapping(value = "/gruppe/{gruppeId}", method = RequestMethod.POST)
    public void createMeldingFromMeldingstype(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsNewSkdEndringsmelding rsNewSkdEndringsmelding) {
        createSkdEndringsmeldingFromTypeService.execute(gruppeId, rsNewSkdEndringsmelding);
    }

    @LogExceptions
    @RequestMapping(value = "/gruppe/{gruppeId}/raw", method = RequestMethod.POST)
    public void createMeldingerFromText(@PathVariable("gruppeId") Long gruppeId, @RequestBody RsRawMeldinger meldingerAsText) {
        createAndSaveSkdEndringsmeldingerFromTextService.execute(gruppeId, meldingerAsText);
    }

    @LogExceptions
    @RequestMapping(value = "/deletemeldinger", method = RequestMethod.POST)
    public void deleteSkdEndringsmeldinger(@RequestBody RsSkdEdnringsmeldingIdListe rsSkdEdnringsmeldingIdListe) {
        skdEndringsmeldingService.deleteById(rsSkdEdnringsmeldingIdListe.getIds());
    }

    @LogExceptions
    @RequestMapping(value = "/updatemeldinger", method = RequestMethod.POST)
    public void updateMeldinger(@RequestBody List<RsMeldingstype> meldinger) {
        updateSkdEndringsmeldingService.update(meldinger);
    }

    @LogExceptions
    @RequestMapping(value = "/convertmelding", method = RequestMethod.POST)
    public RsMeldingAsText convertMelding(@RequestBody RsMeldingstype rsMelding) {
        String melding = convertMeldingFromJsonToText.execute(rsMelding);
        return new RsMeldingAsText(melding);
    }

    @LogExceptions
    @RequestMapping(value = "/send/{skdMeldingGruppeId}", method = RequestMethod.POST)
    @Operation(description = "Send partiell eller hel gruppe (når ids = null) til TPS")
    public AvspillingResponse sendToTps(@PathVariable Long skdMeldingGruppeId, @RequestBody RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps) {
    
        return nonNull(skdEndringsmeldingIdListToTps.getIds()) ?
                sendEndringsmeldingToTpsService.execute(skdMeldingGruppeId, skdEndringsmeldingIdListToTps) :
                sendEndringsmeldingToTpsService.sendHeleGruppen(skdMeldingGruppeId, skdEndringsmeldingIdListToTps);
    }

    @LogExceptions
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/deleteFromTps")
    public void deleteIdentsFromTps(@RequestParam(required = false, defaultValue = "") List<String> miljoer, @RequestParam List<String> identer) {
        tpsPersonService.sendDeletePersonMeldinger(miljoer, new HashSet<>(identer));
        identpoolService.recycleIdents(new HashSet<>(identer));
    }

    @LogExceptions
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
    @RequestMapping(value = "/melding/{id}", method = RequestMethod.GET)
    public RsMeldingstype getMeldingMedId(@PathVariable("id") Long id) throws JsonProcessingException {
        return skdEndringsmeldingService.findEndringsmeldingById(id);
    }

    @LogExceptions
    @RequestMapping(value = "/meldinger", method = RequestMethod.GET)
    public List<RsMeldingstype> getMeldingerMedId(@RequestParam(value = "ids") List<Long> ids) throws JsonProcessingException {
        return skdEndringsmeldingService.findAllEndringsmeldingerByIds(ids);
    }

    @LogExceptions
    @RequestMapping(value = "/meldinger/{gruppeId}", method = RequestMethod.GET)
    public List<Long> getMeldingIder(@PathVariable("gruppeId") Long gruppeId) {
        return skdEndringsmeldingService.findAllMeldingIdsInGruppe(gruppeId);
    }

    @LogExceptions
    @RequestMapping(value = "/meldinger/{gruppeId}", method = RequestMethod.POST)
    public List<Long> getMeldingIderMedFnr(@PathVariable("gruppeId") Long gruppeId, @RequestBody List<String> identer) {
        return skdEndringsmeldingService.finnAlleMeldingIderMedFoedselsnummer(gruppeId, identer);
    }

    @LogExceptions
    @Operation(method = "Lagrer Skd-endringsmeldingene i TPSF databasen.")
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