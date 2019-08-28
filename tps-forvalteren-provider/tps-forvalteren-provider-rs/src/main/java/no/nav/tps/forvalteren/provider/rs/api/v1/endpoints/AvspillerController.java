package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.time.LocalDateTime.parse;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_AVSPILLER;
import static no.nav.tps.forvalteren.common.java.config.CacheConfig.CACHE_FASIT;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.AJOURHOLDSMELDING;
import static no.nav.tps.forvalteren.domain.rs.Meldingsformat.DISTRIBUSJONSMELDING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.util.Lists.newArrayList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Charsets;

import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
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
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

@RestController
@RequestMapping("api/v1/avspiller")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
@PreAuthorize("hasRole('ROLE_TPSF_AVSPILLER')")
public class AvspillerController {

    private static final String SKD_MELDING = "TPS.ENDRINGS.MELDING";
    private static final String DISTRIBUSJON_MELDING = "TPSDISTRIBUSJON";
    private static final String QUEUE_NOT_FOUND = "avspiller.request.queue.check";

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private AvspillerService avspillerService;

    @Autowired
    private AvspillerDaoService avspillerDaoService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MessageProvider messageProvider;

    @Cacheable(CACHE_AVSPILLER)
    @GetMapping("/meldingstyper")
    public RsTyperOgKilderResponse getTyperOgKilder(@RequestParam("miljoe") String miljoe,
            @ApiParam("yyyy-MM-ddTHH:mm:ss $ yyyy-MM-ddTHH:mm:ss $ timeout")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format) {

        return avspillerService.getTyperOgKilder(RsAvspillerRequest.builder()
                .miljoeFra(miljoe)
                .datoFra(parseDateFrom(periode))
                .datoTil(parseDateTo(periode))
                .timeout(parseTimeout(periode))
                .format(format)
                .build());
    }

    @Cacheable(CACHE_AVSPILLER)
    @GetMapping("/meldinger")
    public RsMeldingerResponse getMeldinger(@RequestParam("miljoe") String miljoe,
            @ApiParam("yyyy-MM-ddTHH:mm:ss $ yyyy-MM-ddTHH:mm:ss $ timeout")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format,
            @RequestParam(value = "typer", required = false) String meldingstyper,
            @RequestParam(value = "kilder", required = false) String kilder,
            @RequestParam(value = "identer", required = false) String identer,
            @ApiParam("bufferNumber $ bufferSize")
            @RequestParam(value = "buffer", required = false) String bufferParams) {

        return avspillerService.getMeldinger(RsAvspillerRequest.builder()
                .miljoeFra(miljoe)
                .datoFra(parseDateFrom(periode))
                .datoTil(parseDateTo(periode))
                .timeout(parseTimeout(periode))
                .format(format)
                .typer(strToList(meldingstyper))
                .kilder(strToList(kilder))
                .identer(strToList(identer))
                .pageNumber(parsePageNumber(bufferParams))
                .bufferSize(parseBufferSize(bufferParams))
                .build());
    }

    @Cacheable(CACHE_AVSPILLER)
    @PostMapping("/meldinger")
    public TpsAvspiller sendTilTps(@RequestBody RsAvspillerRequest request) {

        TpsAvspiller avspillerStatus = avspillerDaoService.save(request);
        if (request.isOwnQueue() && !avspillerService.isValid(request.getMiljoeTil(), request.getQueue())) {
            throw new NotFoundException(messageProvider.get(QUEUE_NOT_FOUND, request.getQueue(), request.getMiljoeTil()));
        }
        avspillerService.sendTilTps(request, avspillerStatus);
        return avspillerStatus;
    }

    @Cacheable(CACHE_FASIT)
    @GetMapping("/meldingskoer")
    public List<String> getMeldingskoer(@RequestParam("miljoe") String miljoe, @RequestParam("format") Meldingsformat format) {

        List<FasitResource> resources =
                fasitApiConsumer.getResourcesByAliasAndTypeAndEnvironment(decideQueueAlias(format),
                        FasitPropertyTypes.QUEUE,
                        decideEnvironment(format, miljoe));
        List<String> queues = new ArrayList<>();
        resources.forEach(resource -> {
            if (!((FasitQueue) resource.getProperties()).getQueueName().toUpperCase().contains("REPLY")) {
                queues.add(((FasitQueue) resource.getProperties()).getQueueName());
            }
        });

        if (format == DISTRIBUSJONSMELDING) {
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

        return format("{\"data\": \"%s\"}", Base64.getEncoder().encodeToString(avspillerService.showRequest(miljoe, format, meldingnr).getBytes(Charsets.UTF_8)));
    }

    @DeleteMapping("/meldinger")
    public RsTpsAvspiller cancelSendTilTps(@RequestParam(value = "bestillingId") Long bestillingId) {

        TpsAvspiller avspiller = avspillerDaoService.cancelRequest(bestillingId);
        return mapperFacade.map(avspiller, RsTpsAvspiller.class);
    }

    private static LocalDateTime parseDateFrom(String periode) {
        String[] times = periode.split("\\$");
        return isNotBlank(periode) && isNotBlank(times[0]) ? parse(times[0]) : null;
    }

    private static LocalDateTime parseDateTo(String periode) {
        String[] times = periode.split("\\$");
        return isNotBlank(periode) && times.length > 1 && isNotBlank(times[1]) ? parse(times[1]) : null;
    }

    private static Long parseTimeout(String periode) {
        String[] times = periode.split("\\$");
        return isNotBlank(periode) && times.length > 2 && isNotBlank(times[2]) ? valueOf(times[2]) : null;
    }

    private static List strToList(String item) {
        return isNotBlank(item) ? newArrayList(item.split(",")) : null;
    }

    private static Long parsePageNumber(String bufferParams) {
        return nonNull(bufferParams) ? valueOf(bufferParams.split("\\$")[0]) : null;
    }

    private static Long parseBufferSize(String bufferParams) {
        return nonNull(bufferParams) && bufferParams.split("\\$").length > 1 ?
                valueOf(bufferParams.split("\\$")[1]) : null;
    }

    private static String decideQueueAlias(Meldingsformat meldingsformat) {
        return meldingsformat == AJOURHOLDSMELDING ? SKD_MELDING : DISTRIBUSJON_MELDING;
    }

    private static String decideEnvironment(Meldingsformat meldingsformat, String environment) {
        return meldingsformat == AJOURHOLDSMELDING && environment.contains("u") ? "u" : environment;
    }
}

