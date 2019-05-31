package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
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
import no.nav.tps.forvalteren.domain.rs.RsMelding;
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

        System.out.println("SendTilTps: miljoFra=" + request.getMiljoeFra() +
                ", datoFra=" + request.getDatoFra() +
                (isNotBlank(request.getTidFra()) ? ", tidFra=" + request.getTidFra() : "") +
                ", datoTil=" + request.getDatoTil() +
                (isNotBlank(request.getTidTil()) ? ", tidTil=" + request.getTidTil() : "") +
                ", format=" + request.getFormat() +
                ", meldingstyper=" + request.getTyper() +
                ", kilder=" + request.getKilder() +
                ", identer=" + request.getIdenter() +
                ", miljoeTil=" + request.getMiljoeTil() +
                ", queue=" + request.getQueue() +
                ", queueManager=" + request.getQueueManger()
        );
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

    private List<RsMelding> buildMeldinger(Long buffersize, Long buffernumber, Long total) {

        String[] meldingstyper = { "foedselsmelding", "innvandringsmelding", "relasjonsmelding" };
        String[] kilder = { "TPSF", "SKD" };

        List<RsMelding> meldinger = new ArrayList<>();

        for (int i = 0; i < buffersize && (buffernumber * buffersize) + i + 1 <= total; i++) {
            meldinger.add(RsMelding.builder().index((buffernumber * buffersize) + i + 1)
                    .meldingNummer(Long.toString(999L * ((buffernumber * buffersize) + i + 1)))
                    .hendelseType(meldingstyper[i % meldingstyper.length]).systemkilde(kilder[i % kilder.length]).tidspunkt(LocalDateTime.now()).ident(
                            format("123456%05d", ((buffersize * buffernumber) + i + 1) % 100000)).build());
        }
        return meldinger;
    }
}
