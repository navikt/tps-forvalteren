package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;

@Service
public class DetectMeldingstype {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private SkdFelterContainerTrans1 skdFelterContainerTrans1;

    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    private static final int TRANSTYPE_START_POSITION = 25;
    private static final int TRANSTYPE_END_POSITION = 26;

    public RsMeldingstype execute(String melding) {
        String meldingstype = melding.substring(TRANSTYPE_START_POSITION, TRANSTYPE_END_POSITION);
        switch (meldingstype.charAt(0)) {
        case '1':
            return createT1(melding);
        case '2':
        case '3':
        case '4':
            return createT2(melding);
        default:
            throw new IllegalMeldingstypeException(messageProvider.get(SKD_ILLEGAL_MELDINGSTYPE, meldingstype));
        }
    }

    private RsMeldingstype createT1(String melding) {
        List<SkdFeltDefinisjon> felter = skdFelterContainerTrans1.hentSkdFelter();
        Map<String, String> meldingFelter = new HashMap<>();
        populateMapWithFelter(melding, meldingFelter, felter);
        meldingFelter.put("meldingstype", "t1");
        String beskrivelse = "IDENT: " + meldingFelter.get("fodselsdato") + meldingFelter.get("personnummer") + " - AARSAKSKODE: " + meldingFelter.get("aarsakskode");
        meldingFelter.put("beskrivelse", beskrivelse);
        return objectMapper.convertValue(meldingFelter, RsMeldingstype1Felter.class);
    }

    private RsMeldingstype createT2(String melding) {
        List<SkdFeltDefinisjon> felter = skdFelterContainerTrans2.hentSkdFelter();
        Map<String, String> meldingFelter = new HashMap<>();
        populateMapWithFelter(melding, meldingFelter, felter);
        meldingFelter.put("meldingstype", "t2");
        String beskrivelse = "IDENT: " + meldingFelter.get("fodselsnr") + " - AARSAKSKODE: " + meldingFelter.get("aarsakskode");
        meldingFelter.put("beskrivelse", beskrivelse);
        return objectMapper.convertValue(meldingFelter, RsMeldingstype2Felter.class);
    }

    private void populateMapWithFelter(String melding, Map<String, String> meldingFelter, List<SkdFeltDefinisjon> felter) {
        for (SkdFeltDefinisjon felt : felter) {
            if (felt.getFraByte() != 0 && felt.getTilByte() != 0) {
                String extractedValue = melding.substring(felt.getFraByte() - 1, felt.getTilByte());
                String trimmedValue = extractedValue.trim();
                meldingFelter.put(felt.getNokkelNavn(), trimmedValue);
            }
        }
    }

}