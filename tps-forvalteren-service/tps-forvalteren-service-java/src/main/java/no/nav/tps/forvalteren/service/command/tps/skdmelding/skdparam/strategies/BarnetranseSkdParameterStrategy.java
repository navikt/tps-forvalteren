package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FamilieendringSkdParamtere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForeldreFraRelasjoner;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;

@Service
public class BarnetranseSkdParameterStrategy implements SkdParametersStrategy {

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private FinnBarnTilForeldreFraRelasjoner finnBarnTilForeldreFraRelasjoner;

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof FamilieendringSkdParamtere;
    }

    @Override
    public Map<String, String> execute(Person foreldre) {
        Map<String, String> skdParams = new HashMap<>();
        List<Relasjon> foreldreBarnRelasjoner = relasjonRepository.findByPersonAndRelasjonTypeNavn(foreldre, "BARN");
        List<Person> barn = finnBarnTilForeldreFraRelasjoner.execute(foreldreBarnRelasjoner);
        addSkdParametersExtractedFromForeldre(skdParams, foreldre);
        addSkdParametersExtractedFromBarn(skdParams, barn);
        addDefaultParameters(skdParams);
        return skdParams;
    }

    private void addSkdParametersExtractedFromForeldre(Map<String, String> skdParams, Person foreldre) {
        skdParams.put("T2-FODSELSNR", foreldre.getIdent());
    }

    private void addSkdParametersExtractedFromBarn(Map<String, String> skdParams, List<Person> barn) {
        for(int counter = 0; counter < barn.size(); counter++) {
            Person currentBarn = barn.get(counter);
            skdParams.put("T2-BARN-FODSDATO" + counter, currentBarn.getIdent().substring(0,6));
            skdParams.put("T2-BARN-PERSNR" + counter, currentBarn.getIdent().substring(6));
        }
    }

    private void addDefaultParameters(Map<String, String> skdParams) {
        String maskindato = GetStringVersionOfLocalDateTime.yyyyMMdd(LocalDateTime.now());
        String maskintid = GetStringVersionOfLocalDateTime.hhMMss(LocalDateTime.now());
        skdParams.put("T2-MASKINDATO", maskindato);
        skdParams.put("T2-MASKINTID", maskintid);
        skdParams.put("T2-TRANSTYPE", "2");
        skdParams.put("T2-AARSAKSKODE", "98");
    }

}
