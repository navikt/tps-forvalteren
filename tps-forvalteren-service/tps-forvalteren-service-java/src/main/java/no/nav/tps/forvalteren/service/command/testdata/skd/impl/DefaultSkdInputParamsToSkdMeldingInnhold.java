package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFeltDefinisjon;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdInputParamsToSkdMeldingInnhold;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultSkdInputParamsToSkdMeldingInnhold implements SkdInputParamsToSkdMeldingInnhold { //TODO bytte ut denne med SkdMeldingTrans1.toString.

    public StringBuilder execute(Map<String, String> skdInputMap, SkdFelterContainer skdFelterContainer) {
        StringBuilder skdMelding = new StringBuilder();
        List<SkdFeltDefinisjon> skdFelter = skdFelterContainer.hentSkdFelter();
        skdFelter.forEach(skdFeltDefinisjon -> {
            skdFeltDefinisjon.setVerdi(skdInputMap.get(skdFeltDefinisjon.getNokkelNavn()));
            addDefaultValueToEndOfString(skdFeltDefinisjon);
        });
        skdMelding.append(
                skdFelter.stream()
                        .sorted(Comparator.comparingInt(SkdFeltDefinisjon::getIdRekkefolge))
                        .map(skdFeltDefinisjon -> skdFeltDefinisjon.getVerdi() != null ? skdFeltDefinisjon.getVerdi() : skdFeltDefinisjon.getDefaultVerdi())
                        .collect(Collectors.joining())
        );
        return skdMelding;
    }

    private void addDefaultValueToEndOfString(SkdFeltDefinisjon skdFeltDefinisjon) {
        if (skdFeltDefinisjon.getVerdi() != null) {
            if(!skdFeltDefinisjon.isValueLastInSkdField()){
                skdFeltDefinisjon.setVerdi(skdFeltDefinisjon.getVerdi() + skdFeltDefinisjon.getDefaultVerdi().substring(skdFeltDefinisjon.getVerdi().length()));
            } else {
                skdFeltDefinisjon.setVerdi(skdFeltDefinisjon.getDefaultVerdi().substring(0,
                        (skdFeltDefinisjon.getDefaultVerdi().length() - skdFeltDefinisjon.getVerdi().length())) +
                        skdFeltDefinisjon.getVerdi());
            }
        }
    }
}
