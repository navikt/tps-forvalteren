package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.Ajourholdsmelding;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.Distribusjonsmelding;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.Meldingskoe;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerDaoService;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerService;

@RestController
@RequestMapping("api/v1/avspiller")
public class AvspillerController {

    private static final String SKD_MELDING = "TPS.ENDRINGS.MELDING";
    private static final String DISTRIBUSJON_MELDING = "TPSDISTRIBUSJON";

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private AvspillerService avspillerService;

    @Autowired
    private AvspillerDaoService avspillerDaoService;

    @GetMapping("/meldingstyper")
    public RsTyperOgKilderResponse getTyperOgKilder(@RequestParam("miljoe") String miljoe,
            @ApiParam("YYYY-MM-DD HH:MM $ YYYY-MM-DD HH:MM")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format) {

        return avspillerService.getTyperOgKilder(format, periode, miljoe);
    }

    @GetMapping("/meldinger")
    public RsMeldingerResponse getMeldinger(@RequestParam("miljoe") String miljoe,
            @ApiParam("YYYY-MM-DD HH:MM $ YYYY-MM-DD HH:MM")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format,
            @RequestParam(value = "typer", required = false) String meldingstyper,
            @RequestParam(value = "kilder", required = false) String kilder,
            @RequestParam(value = "identer", required = false) String identer,
            @ApiParam("bufferNumber $ bufferSize")
            @RequestParam(value = "buffer", required = false) String buffer) {

        return avspillerService.getMeldinger(miljoe, periode, format, meldingstyper, kilder, identer, buffer);
    }

    @PostMapping("/meldinger")
    public TpsAvspiller sendTilTps(@RequestBody RsAvspillerRequest request) {

        TpsAvspiller avspillerStatus = avspillerDaoService.save(request);
        avspillerService.sendTilTps(request, avspillerStatus);
        return avspillerStatus;
    }

    @GetMapping("/meldingskoer")
    public List<Meldingskoe> getMeldingskoer(@RequestParam("miljoe") String miljoe, @RequestParam("format") Meldingsformat format) {

        String queueAlias = format == Ajourholdsmelding ? SKD_MELDING : DISTRIBUSJON_MELDING;
        String environment = format == Ajourholdsmelding && miljoe.contains("u") ? "u" : miljoe;
        List<FasitResource> resources = fasitApiConsumer.getResourcesByAliasAndTypeAndEnvironment(queueAlias, FasitPropertyTypes.QUEUE, environment);
        List<Meldingskoe> queues = new ArrayList<>();
        resources.forEach(resource -> {
            if (!((FasitQueue) resource.getProperties()).getQueueName().toUpperCase().contains("REPLY")) {
                queues.add(Meldingskoe.builder()
                        .koenavn(((FasitQueue) resource.getProperties()).getQueueName())
                        .koemanager(((FasitQueue) resource.getProperties()).getQueueManager())
                        .fasitAlias(resource.getAlias())
                        .build());
            }
        });

        if (format == Distribusjonsmelding) {
            queues.add(Meldingskoe.builder()
                    .koenavn(String.format("QA.%s_412.TPSDISTRIBUSJON_FS03", miljoe.contains("u") ? "D8" : miljoe.toUpperCase()))
                    .build());
        }

        return queues;
    }

    @GetMapping("/statuser")
    public TpsAvspiller getStatuser(@RequestParam(value = "bestilling", required = false) Long bestillingId) {

        return avspillerDaoService.getStatus(bestillingId);
    }
}
