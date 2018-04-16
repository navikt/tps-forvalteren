package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdGetHeaderForSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DefaultSkdGetHeaderForSkdMelding implements SkdGetHeaderForSkdMelding {

    private static final String WHITESPACE_20_STK = "                    ";

    private static final String KJORE_NUMMER = "000004421";
    private static final String KODE_SYSTEM = "TPSF";
    private static final String MQ_HANDLE = "000000000";
    private static final String SKD_REFERANSE = WHITESPACE_20_STK;
    
    public String execute(SkdMeldingTrans1 skdMelding){
        StringBuilder headerSkdMelding = new StringBuilder();
        headerSkdMelding.append(MQ_HANDLE)
                .append(KODE_SYSTEM)
                .append(KJORE_NUMMER);

        headerSkdMelding.append(skdMelding.getAarsakskode())
                .append(skdMelding.getTranstype())
                .append(skdMelding.getTildelingskode()==null?"0":skdMelding.getTildelingskode())
                .append(SKD_REFERANSE);

        return headerSkdMelding.toString();
    }
}
