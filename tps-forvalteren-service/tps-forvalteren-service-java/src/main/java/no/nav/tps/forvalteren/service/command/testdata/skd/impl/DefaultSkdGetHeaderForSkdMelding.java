package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import static java.util.Objects.isNull;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdGetHeaderForSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;

@Service
public class DefaultSkdGetHeaderForSkdMelding implements SkdGetHeaderForSkdMelding {

    private static final String WHITESPACE_20_STK = "                    ";

    private static final String KJORE_NUMMER = "000004421";
    private static final String KODE_SYSTEM = "TPSF";
    private static final String MQ_HANDLE = "000000000";
    private static final String SKD_REFERANSE = WHITESPACE_20_STK;

    public String execute(SkdMeldingTrans1 skdMelding) {
        return new StringBuilder()
                .append(MQ_HANDLE)
                .append(KODE_SYSTEM)
                .append(KJORE_NUMMER)
                .append(skdMelding.getAarsakskode())
                .append(skdMelding.getTranstype())
                .append(isNull(skdMelding.getTildelingskode()) ? '0' : skdMelding.getTildelingskode())
                .append(SKD_REFERANSE)
                .toString();
    }

    public String prependHeader(String skdMelding) {
        return new StringBuilder()
                .append(MQ_HANDLE)
                .append(KODE_SYSTEM)
                .append(KJORE_NUMMER)
                .append(skdMelding.substring(26, 28))
                .append(skdMelding.substring(25, 26))
                .append(" ".equals(skdMelding.substring(873, 874)) ? '0' : skdMelding.subSequence(873, 874))
                .append(SKD_REFERANSE)
                .append(skdMelding)
                .toString();
    }
}
