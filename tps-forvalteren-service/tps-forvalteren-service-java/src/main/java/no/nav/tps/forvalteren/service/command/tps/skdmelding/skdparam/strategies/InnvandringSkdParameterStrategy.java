package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE_1;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.time.LocalDateTime;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.service.DiskresjonskoderType;
import no.nav.tps.forvalteren.domain.service.Sivilstand;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

public abstract class InnvandringSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAK_KO_DE_FOR_INNVANDRING = "02";

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    @Autowired
    private SetAdresseService setAdresseService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Override
    public SkdMeldingTrans1 execute(Person person) {

        SkdMeldingTrans1 skdMeldingTrans1 = SkdMeldingTrans1.builder().tildelingskode(hentTildelingskode()).build();

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));
        skdMeldingTrans1.setFornavn(person.getFornavn());
        skdMeldingTrans1.setMellomnavn(person.getMellomnavn());
        skdMeldingTrans1.setSlektsnavn(person.getEtternavn());
        skdMeldingTrans1.setStatsborgerskap(landkodeEncoder.encode(getFirstStatsborgerskap(person)));
        skdMeldingTrans1.setRegdatoStatsb(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(getFirstStatsborgerskapRegdato(person))));
        skdMeldingTrans1.setFamilienummer(person.getIdent());

        skdMeldingTrans1.setSivilstand(Sivilstand.lookup(person.getSivilstand()).getRelasjonTypeKode());
        skdMeldingTrans1.setInnvandretFraLand(landkodeEncoder.encode(
                nullcheckSetDefaultValue(person.getInnvandretFraLand(), "???")));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(enforceValidTpsDate(person.getRegdato()));
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);
        skdMeldingTrans1.setRegDato(yyyyMMdd);
        skdMeldingTrans1.setRegdatoAdr(yyyyMMdd);

        skdMeldingTrans1.setFraLandRegdato(yyyyMMdd);
        skdMeldingTrans1.setFraLandFlyttedato(ConvertDateToString.yyyyMMdd(
                enforceValidTpsDate(nullcheckSetDefaultValue(person.getInnvandretFraLandFlyttedato(), hentDatoFraIdentService.extract(person.getIdent())))));
        skdMeldingTrans1.setRegdatoFamnr(yyyyMMdd);

        setAdresseService.execute(skdMeldingTrans1, person);

        skdMeldingTrans1.setSpesRegType(person.getSpesreg() != null ? DiskresjonskoderType.valueOf(person.getSpesreg()).getKodeverdi() : null);
        skdMeldingTrans1.setDatoSpesRegType(ConvertDateToString.yyyyMMdd(person.getSpesregDato()));
    }

    private static String getFirstStatsborgerskap(Person person) {

        if (person.getStatsborgerskap().isEmpty()) {
            return null;
        }
        person.getStatsborgerskap().sort(Comparator.comparing(Statsborgerskap::getId));
        return person.getStatsborgerskap().get(0).getStatsborgerskap();
    }

    private static LocalDateTime getFirstStatsborgerskapRegdato(Person person) {

        if (person.getStatsborgerskap().isEmpty()) {
            return null;
        }
        person.getStatsborgerskap().sort(Comparator.comparing(Statsborgerskap::getId));
        return person.getStatsborgerskap().get(0).getStatsborgerskapRegdato();
    }


    private static void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {

        skdMeldingTrans1.setAarsakskode(AARSAK_KO_DE_FOR_INNVANDRING);
        skdMeldingTrans1.setTranstype(TRANSTYPE_1);

        skdMeldingTrans1.setPersonkode("1");
        skdMeldingTrans1.setStatuskode("1");
    }
}
