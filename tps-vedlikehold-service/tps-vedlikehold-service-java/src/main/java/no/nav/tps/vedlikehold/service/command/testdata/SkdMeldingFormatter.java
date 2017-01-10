package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.SkdFeltDefinisjon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Peter Fløgstad on 09.01.2017.
 */

@Component
public class SkdMeldingFormatter {

    @Autowired
    private SkdFelterContainer skdFelterContainer;

    public String convertToSkdMelding(Map<String, String> skdInputMap){
        List<SkdFeltDefinisjon> skdFelter = skdFelterContainer.hentSkdFelter();
        skdFelter.forEach(skdFeltDefinisjon -> {
            skdFeltDefinisjon.setVerdi(skdInputMap.getOrDefault(skdFeltDefinisjon.getNokkelNavn(),null));
            addDefaultValueToEndOfString(skdFeltDefinisjon);
        });
        return skdFelter.stream()
                .sorted(Comparator.comparingInt(SkdFeltDefinisjon::getIdRekkefolge))
                .map(skdFeltDefinisjon -> skdFeltDefinisjon.getVerdi() == null ? skdFeltDefinisjon.getDefaultVerdi() : skdFeltDefinisjon.getVerdi())
                .collect(Collectors.joining());
    }

    private void addDefaultValueToEndOfString(SkdFeltDefinisjon skdFeltDefinisjon){
        if(skdFeltDefinisjon.getVerdi() == null) return;
        skdFeltDefinisjon.setVerdi(skdFeltDefinisjon.getVerdi() + skdFeltDefinisjon.getDefaultVerdi().substring(skdFeltDefinisjon.getVerdi().length()));
    }
}

