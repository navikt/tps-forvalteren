package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

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
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.Meldingskoe;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.skd.RsAvspillerProgress;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerService;

@RestController
@RequestMapping("api/v1/avspiller")
public class AvspillerController {

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private AvspillerService avspillerService;

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

        return avspillerService.getMeldinger(miljoe, periode, format, meldingstyper, kilder,  identer, buffer);
    }

    @PostMapping("/meldinger")
    public void sendTilTps(@RequestBody RsAvspillerRequest request) {

        avspillerService.sendTilTps(request);
    }

    @GetMapping("/meldingskoer")
    public List<Meldingskoe> getMeldingskoer(@RequestParam("miljoe") String miljoe, @RequestParam("format") Meldingsformat format) {

        List<FasitResource> ressurser = fasitApiConsumer.getResourcesByAliasAndTypeAndEnvironment("TPSDISTRIBUSJON", FasitPropertyTypes.QUEUE, miljoe);
        List<Meldingskoe> queues = new ArrayList<>();
        ressurser.forEach(ressurs -> queues.add(Meldingskoe.builder()
                .koenavn(((FasitQueue) ressurs.getProperties()).getQueueName())
                .koemanager(((FasitQueue) ressurs.getProperties()).getQueueManager())
                .build()));
        return queues;
    }

    @GetMapping("/statuser")
    public List<RsAvspillerProgress> getStatuser(@RequestParam(value = "bestilling", required = false) Long bestillingId) {

        return avspillerService.getStatuser(bestillingId);
    }
}
