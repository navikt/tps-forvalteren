package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.time.LocalDateTime.parse;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.Ajourholdsmelding;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.Distribusjonsmelding;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsAvspiller;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerDaoService;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerService;

@RestController
@RequestMapping("api/v1/avspiller")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class AvspillerController {

    private static final String SKD_MELDING = "TPS.ENDRINGS.MELDING";
    private static final String DISTRIBUSJON_MELDING = "TPSDISTRIBUSJON";

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private AvspillerService avspillerService;

    @Autowired
    private AvspillerDaoService avspillerDaoService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/meldingstyper")
    public RsTyperOgKilderResponse getTyperOgKilder(@RequestParam("miljoe") String miljoe,
            @ApiParam("yyyy-MM-ddTHH:mm:ss $ yyyy-MM-ddTHH:mm:ss")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format) {

        String[] startStop = isNotBlank(periode) ? periode.split("\\$") : null;
        return avspillerService.getTyperOgKilder(RsAvspillerRequest.builder()
                .miljoeFra(miljoe)
                .datoFra(nonNull(startStop) ? parse(startStop[0]) : null)
                .datoTil(nonNull(startStop) && startStop.length > 1 ? parse(startStop[1]) : null)
                .timeout(nonNull(startStop) && startStop.length > 2 ? valueOf(startStop[2]) : null)
                .format(format)
                .build());
    }

    @GetMapping("/meldinger")
    public RsMeldingerResponse getMeldinger(@RequestParam("miljoe") String miljoe,
            @ApiParam("yyyy-MM-ddTHH:mm:ss $ yyyy-MM-ddTHH:mm:ss")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format,
            @RequestParam(value = "typer", required = false) String meldingstyper,
            @RequestParam(value = "kilder", required = false) String kilder,
            @RequestParam(value = "identer", required = false) String identer,
            @ApiParam("bufferNumber $ bufferSize")
            @RequestParam(value = "buffer", required = false) String buffer) {

        String[] startStop = isNotBlank(periode) ? periode.split("\\$") : null;
        String[] bufferParams = isNotBlank(buffer) ? buffer.split("\\$") : null;
        return avspillerService.getMeldinger(RsAvspillerRequest.builder()
                .miljoeFra(miljoe)
                .datoFra(nonNull(startStop) ? parse(startStop[0]) : null)
                .datoTil(nonNull(startStop) && startStop.length > 1 ? parse(startStop[1]) : null)
                .timeout(nonNull(startStop) && startStop.length > 2 ? valueOf(startStop[2]) : null)
                .format(format)
                .typer(isNotBlank(meldingstyper) ? newArrayList(meldingstyper.split(",")) : null)
                .kilder(isNotBlank(kilder) ? newArrayList(kilder.split(",")) : null)
                .identer(isNotBlank(identer) ? newArrayList(identer.split(",")) : null)
                .pageNumber(nonNull(bufferParams) ? valueOf(bufferParams[0]) : null)
                .bufferSize(nonNull(bufferParams) && bufferParams.length > 1 ? valueOf(bufferParams[1]) : null)
                .build());
    }

    @PostMapping("/meldinger")
    public TpsAvspiller sendTilTps(@RequestBody RsAvspillerRequest request) {

        TpsAvspiller avspillerStatus = avspillerDaoService.save(request);
        avspillerService.sendTilTps(request, avspillerStatus);
        return avspillerStatus;
    }

    @GetMapping("/meldingskoer")
    public List<String> getMeldingskoer(@RequestParam("miljoe") String miljoe, @RequestParam("format") Meldingsformat format) {

        String queueAlias = format == Ajourholdsmelding ? SKD_MELDING : DISTRIBUSJON_MELDING;
        String environment = format == Ajourholdsmelding && miljoe.contains("u") ? "u" : miljoe;
        List<FasitResource> resources = fasitApiConsumer.getResourcesByAliasAndTypeAndEnvironment(queueAlias, FasitPropertyTypes.QUEUE, environment);
        List<String> queues = new ArrayList<>();
        resources.forEach(resource -> {
            if (!((FasitQueue) resource.getProperties()).getQueueName().toUpperCase().contains("REPLY")) {
                queues.add(((FasitQueue) resource.getProperties()).getQueueName());
            }
        });

        if (format == Distribusjonsmelding) {
            queues.add(format("QA.%s_412.TPSDISTRIBUSJON_FS03", miljoe.contains("u") ? "D8" : miljoe.toUpperCase()));
        }

        return queues;
    }

    @GetMapping("/statuser")
    public RsTpsAvspiller getStatuser(@RequestParam(value = "bestilling", required = false) Long bestillingId) {

        TpsAvspiller avspiller = avspillerDaoService.getStatus(bestillingId);
        return mapperFacade.map(avspiller, RsTpsAvspiller.class);
    }

    @GetMapping("/melding")
    public String showRequest(@RequestParam(value = "miljoe") String miljoe,
            @RequestParam(value = "format", required = false) Meldingsformat format,
            @RequestParam(value = "meldingnr", required = false) String meldingnr) {

        return format("{\"data\": \"%s\"}", Base64.getEncoder().encodeToString(avspillerService.showRequest(miljoe, format, meldingnr).getBytes()));
    }

    @DeleteMapping("/meldinger")
    public RsTpsAvspiller cancelSendTilTps(@RequestParam(value = "bestillingId") Long bestillingId) {

        TpsAvspiller avspiller = avspillerDaoService.cancelRequest(bestillingId);
        return mapperFacade.map(avspiller, RsTpsAvspiller.class);
    }
}
