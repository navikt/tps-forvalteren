package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
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

    @Cacheable
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

        return RsMeldingerResponse.builder().antallTotalt(325L)
                .meldinger(newArrayList(
                        RsMelding.builder().meldingNummer(123L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678910").build(),
                        RsMelding.builder().meldingNummer(124L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678901").build(),
                        RsMelding.builder().meldingNummer(125L).meldingsType("innvandringsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678902").build(),
                        RsMelding.builder().meldingNummer(126L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678903").build(),
                        RsMelding.builder().meldingNummer(127L).meldingsType("innvandringsmelding").systemkilde("SKD").tidspunkt(LocalDateTime.now()).ident("12345678904").build(),
                        RsMelding.builder().meldingNummer(128L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678905").build(),
                        RsMelding.builder().meldingNummer(129L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678906").build(),
                        RsMelding.builder().meldingNummer(130L).meldingsType("innvandringsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12345678907").build(),
                        RsMelding.builder().meldingNummer(131L).meldingsType("foedselsmelding").systemkilde("TPSF").tidspunkt(LocalDateTime.now()).ident("12344678908").build()))
                .build();
    }
}
