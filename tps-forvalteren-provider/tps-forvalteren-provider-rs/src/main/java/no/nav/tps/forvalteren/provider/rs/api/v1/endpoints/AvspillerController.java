package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.Meldingskoe;
import no.nav.tps.forvalteren.domain.rs.RsMelding;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.forvalteren.domain.rs.RsTyperOgKilderResponse;
import no.nav.tps.forvalteren.domain.rs.TypeOgAntall;

@RestController
@RequestMapping("api/v1/avspiller")
public class AvspillerController {

    @GetMapping("/meldingstyper")
    public RsTyperOgKilderResponse getTyperOgKilder(@RequestParam("miljoe") String miljoe,
            @ApiParam("YYYY-MM-DD HH:MM $ YYYY-MM-DD HH:MM")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format) {

        System.out.println("miljo=" + miljoe +
                ", datoFra=" + (isNotBlank(periode) ? periode.split("\\$")[0] : null) +
                ", datoTil=" + (isNotBlank(periode) ? periode.split("\\$")[1] : null) +
                ", format=" + format);

        return RsTyperOgKilderResponse.builder()
                .typer(newArrayList(TypeOgAntall.builder().type(format.getMeldingFormat() + "-innvandringsmelding").antall(isNotBlank(periode) ? "100" : null).build(),
                        TypeOgAntall.builder().type(format.getMeldingFormat() + "-fødselsmelding").antall(isNotBlank(periode) ? "50" : null).build(),
                        TypeOgAntall.builder().type(format.getMeldingFormat() + "-relasjonsmelding").antall(isNotBlank(periode) ? "50" : null).build()))
                .kilder(newArrayList(TypeOgAntall.builder().type("SKD").antall(isNotBlank(periode) ? "100" : null).build(),
                        TypeOgAntall.builder().type("TPSF").antall(isNotBlank(periode) ? "50" : null).build()))
                .build();
    }

    @GetMapping("/meldinger")
    public RsMeldingerResponse getMeldinger(@RequestParam("miljoe") String miljoe,
            @ApiParam("YYYY-MM-DD HH:MM $ YYYY-MM-DD HH:MM")
            @RequestParam(value = "periode", required = false) String periode,
            @RequestParam(value = "format") Meldingsformat format,
            @RequestParam(value = "typer", required = false) String meldingstyper,
            @RequestParam(value = "kilder", required = false) String kilder,
            @RequestParam(value = "identer", required = false) String identer,
            @ApiParam("bufferSize $ bufferNumber")
            @RequestParam(value = "buffer", required = false) String buffer) {

        System.out.println("miljo=" + miljoe +
                ", datoFra=" + (isNotBlank(periode) ? periode.split("\\$")[0] : null) +
                ", datoTil=" + (isNotBlank(periode) ? periode.split("\\$")[1] : null) +
                ", format=" + format +
                ", meldingstyper=" + meldingstyper +
                ", kilder=" + kilder +
                ", identer=" + identer +
                ", buffer=" + buffer);

        Long buffersize = nonNull(buffer) ? Long.valueOf(buffer.split("\\$")[0]) : 300;
        Long buffernumber = nonNull(buffer) ? Long.valueOf(buffer.split("\\$")[1]) : 0;

        return RsMeldingerResponse.builder().antallTotalt(25389L)
                .meldinger(buildMeldinger(buffersize, buffernumber))
                .buffersize(buffersize)
                .buffernumber(buffernumber)
                .build();
    }

    @GetMapping("/meldingskoer")
    public List<Meldingskoe> getMeldingskøer(@RequestParam("miljoe") String miljoe, @RequestParam("format") Meldingsformat format) {

        return newArrayList(Meldingskoe.builder().koenavn("QA.T5_AKTOERREGISTER.TPSDISTRIBUSJON").koemanager("mq://d26apvl126.test.local:1412/MTLCLIENT01").build(),
                Meldingskoe.builder().koenavn("QA.T4_AKTOERREGISTER.TPSDISTRIBUSJON").koemanager("mq://d26apvl126.test.local:1412/MTLCLIENT01").build(),
                Meldingskoe.builder().koenavn("QA.T7_AKTOERREGISTER.TPSDISTRIBUSJON").koemanager("mq://d26apvl126.test.local:1412/MTLCLIENT01").build(),
                Meldingskoe.builder().koenavn("QA.T8_AKTOERREGISTER.TPSDISTRIBUSJON").koemanager("mq://d26apvl126.test.local:1412/MTLCLIENT01").build(),
                Meldingskoe.builder().koenavn("QA.T9_AKTOERREGISTER.TPSDISTRIBUSJON").koemanager("mq://d26apvl126.test.local:1412/MTLCLIENT01").build()
        );
    }

    private List<RsMelding> buildMeldinger(Long buffersize, Long buffernumber) {

        String[] meldingstyper = { "foedselsmelding", "innvandringsmelding", "relasjonsmelding" };
        String[] kilder = { "TPSF", "SKD" };

        List<RsMelding> meldinger = new ArrayList<>();

        for (int i = 0; i < buffersize; i++) {
            meldinger.add(RsMelding.builder().meldingNummer((buffernumber * buffersize) + i + 1)
                    .meldingsType(meldingstyper[i % meldingstyper.length]).systemkilde(kilder[i % kilder.length]).tidspunkt(LocalDateTime.now()).ident(
                            format("123456%05d", ((buffersize * buffernumber) + i + 1) % 100000)).build());
        }
        return meldinger;
    }
}
