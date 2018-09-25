package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalFoedselsMeldingException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@Service
public class FoedselsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_FOEDSELSMELDING = "01";
    private static final String TILDELINGS_KO_DE_FOEDSELSMELDING = "0";

    @Autowired
    private SetAdresseService setAdresseService;

    @Autowired
    private HentKjoennFraIdentService hentKjoennFraIdentService;

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
        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(barn.getRegdato()));

        skdMeldingTrans1.setFoedekommLand("0301");
        skdMeldingTrans1.setFoedested("Sykehus");

        skdMeldingTrans1.setFornavn(barn.getFornavn());
        skdMeldingTrans1.setSlektsnavn(barn.getEtternavn());
        skdMeldingTrans1.setKjonn(hentKjoennFraIdentService.execute(barn.getIdent()));

        skdMeldingTrans1.setRegdatoAdr(ConvertDateToString.yyyyMMdd(barn.getRegdato()));
        skdMeldingTrans1.setPersonkode("3");
        skdMeldingTrans1.setLevendeDoed("1");

        skdMeldingTrans1.setSpesRegType(barn.getSpesreg());
        skdMeldingTrans1.setDatoSpesRegType(ConvertDateToString.yyyyMMdd(barn.getSpesregDato()));
    }

    private void addSkdParametersExtractedFromForeldre(SkdMeldingTrans1 skdMeldingTrans1, Person barn) {
        Person forelderMor = null;
        Person forelderFar = null;
        List<Relasjon> relasjoner = barn.getRelasjoner();
        for (Relasjon relasjon : relasjoner) {
            if (RelasjonType.MOR.getName().equals(relasjon.getRelasjonTypeNavn())) {
                forelderMor = relasjon.getPersonRelasjonMed();
                continue;
            }
            if (RelasjonType.FAR.getName().equals(relasjon.getRelasjonTypeNavn())) {
                forelderFar = relasjon.getPersonRelasjonMed();
            }
        }
        if (forelderMor == null) {
            throw new IllegalFoedselsMeldingException(barn.getFornavn() + " " + barn.getEtternavn() + " mangler en mor");
        }

        skdMeldingTrans1.setMorsFodselsdato(getDato(forelderMor));
        skdMeldingTrans1.setMorsPersonnummer(getPersonnr(forelderMor));
        skdMeldingTrans1.setSlektsnavn(forelderMor.getEtternavn());
        skdMeldingTrans1.setFamilienummer(forelderMor.getIdent());
        skdMeldingTrans1.setRegdatoFamnr(ConvertDateToString.yyyyMMdd(barn.getRegdato()));

        if (forelderFar != null) {
            skdMeldingTrans1.setForeldreansvar("D");
            skdMeldingTrans1.setFarsFodselsdato(getDato(forelderFar));
            skdMeldingTrans1.setFarsPersonnummer(getPersonnr(forelderFar));
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
