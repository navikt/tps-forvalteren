package no.nav.tps.forvalteren.service.command.testdata;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreNorskGironummer.ENDRE_KONTONUMMER;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreNorskGironummerRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class EndreNorskGironummer {

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {
        List<TpsNavEndringsMelding> navMeldinger = new ArrayList<>();

        if (nonNull(person.getSprakKode())) {
            environmentSet.forEach(environment -> {

                if (isNotBlank(person.getBankkontonr())) {
                    navMeldinger.add(new TpsNavEndringsMelding(TpsEndreNorskGironummerRequest.builder()
                            .serviceRutinenavn(ENDRE_KONTONUMMER)
                            .offentligIdent(person.getIdent())
                            .giroNrNorsk(person.getBankkontonr().replace(".", ""))
                            .datoGiroNrNorsk(ConvertDateToString.yyyysMMsdd(enforceValidTpsDate(
                                    nullcheckSetDefaultValue(person.getBankkontonrRegdato(), now()))))
                            .build(), environment));
                }
            });
        }

        return navMeldinger;
    }
}
