package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.DiskresjonskoderType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalFoedselsMeldingException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ForeldreStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@Service
public class FoedselsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_FOEDSELSMELDING = "01";
    private static final String TILDELINGS_KO_DE_FOEDSELSMELDING = "0";

    @Autowired
    private SetAdresseService setAdresseService;

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Override
    public SkdMeldingTrans1 execute(Person barn) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, barn);
        addSkdParametersExtractedFromForeldre(skdMeldingTrans1, barn);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGS_KO_DE_FOEDSELSMELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof FoedselsmeldingSkdParametere;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person barn) {

        skdMeldingTrans1.setFodselsdato(getDato(barn));
        skdMeldingTrans1.setPersonnummer(getPersonnr(barn));

        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(barn.getRegdato()));
        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(barn.getRegdato()));
        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(hentDatoFraIdentService.extract(barn.getIdent())));

        skdMeldingTrans1.setFoedekommLand("0301");
        skdMeldingTrans1.setFoedested("Sykehus");

        skdMeldingTrans1.setFornavn(barn.getFornavn());
        skdMeldingTrans1.setSlektsnavn(barn.getEtternavn());
        skdMeldingTrans1.setKjoenn(hentKjoennFraIdentService.execute(barn.getIdent()));

        skdMeldingTrans1.setRegdatoAdr(ConvertDateToString.yyyyMMdd(barn.getRegdato()));
        skdMeldingTrans1.setPersonkode("3");
        skdMeldingTrans1.setLevendeDoed("1");

        skdMeldingTrans1.setSpesRegType(barn.getSpesreg() != null ? DiskresjonskoderType.valueOf(barn.getSpesreg()).getKodeverdi() : null);
        skdMeldingTrans1.setDatoSpesRegType(ConvertDateToString.yyyyMMdd(barn.getSpesregDato()));
    }

    private void addSkdParametersExtractedFromForeldre(SkdMeldingTrans1 skdMeldingTrans1, Person barn) {

        List<Person> foreldre = ForeldreStrategy.getEntydigeForeldre(barn.getRelasjoner());

        if (foreldre.isEmpty()) {
            throw new IllegalFoedselsMeldingException(barn.getFornavn() + " " + barn.getEtternavn() + " mangler en mor i fødselsmeldingen.");
        }
        if (foreldre.size() > 2) {
            throw new IllegalFoedselsMeldingException(barn.getFornavn() + " " + barn.getEtternavn() + " kan ikke ha mer enn en forelder hver av MOR og FAR i fødselsmeldingen.");
        }

        skdMeldingTrans1.setMorsFodselsdato(getDato(foreldre.get(0)));
        skdMeldingTrans1.setMorsPersonnummer(getPersonnr(foreldre.get(0)));
        skdMeldingTrans1.setSlektsnavn(barn.getEtternavn());
        skdMeldingTrans1.setFamilienummer(foreldre.get(0).getIdent());
        skdMeldingTrans1.setRegdatoFamnr(ConvertDateToString.yyyyMMdd(barn.getRegdato()));

        if (foreldre.size() > 1) {
            skdMeldingTrans1.setForeldreansvar("D");
            skdMeldingTrans1.setFarsFodselsdato(getDato(foreldre.get(1)));
            skdMeldingTrans1.setFarsPersonnummer(getPersonnr(foreldre.get(1)));
        } else {
            skdMeldingTrans1.setForeldreansvar("M");
        }
        skdMeldingTrans1.setDatoForeldreansvar(ConvertDateToString.yyyyMMdd(barn.getRegdato()));

        setAdresseService.execute(skdMeldingTrans1, barn);
    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setSivilstand("1");
        skdMeldingTrans1.setTranstype("1");
        skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_FOEDSELSMELDING);
        skdMeldingTrans1.setStatuskode("1");
    }

    private String getPersonnr(Person person) {
        return person.getIdent().substring(6, 11);
    }

    private String getDato(Person person) {
        return person.getIdent().substring(0, 6);
    }
}
