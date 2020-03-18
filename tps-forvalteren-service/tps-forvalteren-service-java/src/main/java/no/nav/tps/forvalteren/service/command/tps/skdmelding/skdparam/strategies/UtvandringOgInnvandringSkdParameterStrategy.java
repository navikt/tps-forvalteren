package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static java.util.Collections.emptyList;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE_1;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.service.DiskresjonskoderType;
import no.nav.tps.forvalteren.domain.jpa.Sivilstatus;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@Service
@RequiredArgsConstructor
public class UtvandringOgInnvandringSkdParameterStrategy {

    private static final String AARSAKSKODE_FOR_UTVANDRING = "32";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    private final LandkodeEncoder landkodeEncoder;
    private final SetAdresseService setAdresseService;

    public List<SkdMeldingTrans1> execute(Person person) {

        if (person.getInnvandretUtvandret().size() > 1) {
            person.getInnvandretUtvandret().sort(Comparator.comparing(InnvandretUtvandret::getFlyttedato));
            person.getStatsborgerskap().sort(Comparator.comparing(Statsborgerskap::getId));
            List<SkdMeldingTrans1> messages = new ArrayList(person.getInnvandretUtvandret().size() - 1);

            for (int i = 1; i < person.getInnvandretUtvandret().size(); i++) {
                messages.add(buildSkdTrans1InnvandringUtvandring(person, person.getInnvandretUtvandret().get(i)));
            }
            return messages;

        } else {
            return emptyList();
        }
    }

    private SkdMeldingTrans1 buildSkdTrans1InnvandringUtvandring(Person person, InnvandretUtvandret innvandretUtvandret) {

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(enforceValidTpsDate(person.getRegdato()));

        SkdMeldingTrans1 skdMeldingTrans1 = SkdMeldingTrans1.builder()
                .fodselsdato(person.getIdent().substring(0, 6))
                .personnummer(person.getIdent().substring(6, 11))
                .maskindato(yyyyMMdd)
                .maskintid(ConvertDateToString.hhMMss(person.getRegdato()))
                .transtype(TRANSTYPE_1)
                .regDato(yyyyMMdd)
                .slektsnavn(person.getEtternavn())
                .fornavn(person.getFornavn())
                .mellomnavn(person.getMellomnavn())
                .statsborgerskap(landkodeEncoder.encode(person.getStatsborgerskap().get(0).getStatsborgerskap()))
                .regdatoStatsb(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(person.getStatsborgerskap().get(0).getStatsborgerskapRegdato())))
                .familienummer(person.getIdent())
                .regdatoFamnr(yyyyMMdd)
                .personkode("1")
                .spesRegType(isNotBlank(person.getSpesreg()) ? DiskresjonskoderType.valueOf(person.getSpesreg()).getKodeverdi() : null)
                .datoSpesRegType(ConvertDateToString.yyyyMMdd(person.getSpesregDato()))
                .sivilstand(Sivilstatus.lookup(person.getSivilstand()).getRelasjonTypeKode())
                .regdatoSivilstand(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(person.getSivilstandRegdato())))
                .build();

        if (INNVANDRET == innvandretUtvandret.getInnutvandret()) {
            setAdresseService.execute(skdMeldingTrans1, person);
            skdMeldingTrans1.setRegdatoAdr(yyyyMMdd);
            skdMeldingTrans1.setInnvandretFraLand(landkodeEncoder.encode(innvandretUtvandret.getLandkode()));
            skdMeldingTrans1.setFraLandFlyttedato(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(innvandretUtvandret.getFlyttedato())));
            skdMeldingTrans1.setFraLandRegdato(yyyyMMdd);
            skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_INNVANDRING);
            skdMeldingTrans1.setStatuskode("1");
            skdMeldingTrans1.setTildelingskode("2");
        } else {
            skdMeldingTrans1.setUtvandretTilLand(landkodeEncoder.encode(innvandretUtvandret.getLandkode()));
            skdMeldingTrans1.setTilLandFlyttedato(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(innvandretUtvandret.getFlyttedato())));
            skdMeldingTrans1.setTilLandRegdato(yyyyMMdd);
            skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_UTVANDRING);
            skdMeldingTrans1.setStatuskode("3");
            skdMeldingTrans1.setTildelingskode("0");
        }

        return skdMeldingTrans1;
    }
}
