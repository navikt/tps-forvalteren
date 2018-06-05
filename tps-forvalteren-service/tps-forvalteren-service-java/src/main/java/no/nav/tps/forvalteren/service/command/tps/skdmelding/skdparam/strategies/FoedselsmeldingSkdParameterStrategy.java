package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalFoedselsMeldingException;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoedselsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_FOEDSELSMELDING = "01";
    private static final String TILDELINGSKODE_FOEDSELSMELDING = "0";
    String yyyyMMdd;
    String hhMMss;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private SavePersonListService savePersonListService;

    @Override
    public SkdMeldingTrans1 execute(Person barn) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, barn);
        addSkdParametersExtractedFromForeldre(skdMeldingTrans1, barn);
        addDefaultParam(skdMeldingTrans1);
        personRepository.save(barn);

        return skdMeldingTrans1;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_FOEDSELSMELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof FoedselsmeldingSkdParametere;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person barn) {

        skdMeldingTrans1.setFodselsdato(barn.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(barn.getIdent().substring(6, 11));

        yyyyMMdd = ConvertDateToString.yyyyMMdd(barn.getRegdato());
        hhMMss = ConvertDateToString.hhMMss(barn.getRegdato());

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);
        skdMeldingTrans1.setRegDato(yyyyMMdd);

        skdMeldingTrans1.setFoedekommLand("0301");
        skdMeldingTrans1.setFoedested("Sykehus");

        skdMeldingTrans1.setFornavn(barn.getFornavn());
        skdMeldingTrans1.setKjonn(Integer.parseInt(barn.getIdent().substring(8, 9)) % 2 == 0 ? "K" : "M");

        skdMeldingTrans1.setRegdatoAdr(yyyyMMdd);
        skdMeldingTrans1.setPersonkode("3");
        skdMeldingTrans1.setLevendeDoed("1");
    }

    private void addSkdParametersExtractedFromForeldre(SkdMeldingTrans1 skdMeldingTrans1, Person barn) {
        Person forelderMor = null;
        Person forelderFar = null;
        List<Relasjon> personRelasjon = relasjonRepository.findByPersonId(barn.getId());
        for (Relasjon relasjon : personRelasjon) {
            if (RelasjonType.MOR.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
                forelderMor = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
                continue;
            }
            if (RelasjonType.FAR.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
                forelderFar = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
            }
        }
        if (forelderMor == null) {
            throw new IllegalFoedselsMeldingException(barn.getFornavn() + " " + barn.getEtternavn() + " mangler en mor");
        }

        skdMeldingTrans1.setMorsFodselsdato(forelderMor.getIdent().substring(0, 6));
        skdMeldingTrans1.setMorsPersonnummer(forelderMor.getIdent().substring(6, 11));
        skdMeldingTrans1.setSlektsnavn(forelderMor.getEtternavn());
        skdMeldingTrans1.setFamilienummer(forelderMor.getIdent());
        skdMeldingTrans1.setRegdatoFamnr(yyyyMMdd);

        if (forelderFar != null) {
            skdMeldingTrans1.setForeldreansvar("D");
        } else {
            skdMeldingTrans1.setForeldreansvar("M");
        }
        skdMeldingTrans1.setDatoForeldreansvar(yyyyMMdd);
        if (forelderFar != null) {
            skdMeldingTrans1.setFarsFodselsdato(forelderFar.getIdent().substring(0, 6));
            skdMeldingTrans1.setFarsPersonnummer(forelderFar.getIdent().substring(6, 11));

        }
        barn.setEtternavn(forelderMor.getEtternavn());
        addAdresseParamsExtractedFromForelder(skdMeldingTrans1, forelderMor, barn);

    }

    private void addAdresseParamsExtractedFromForelder(SkdMeldingTrans1 skdMeldingTrans1, Person forelder, Person barn) {
        Gateadresse adresse = adresseRepository.getAdresseByPersonId(forelder.getId());

        if (adresse != null) {

            skdMeldingTrans1.setKommunenummer(adresse.getKommunenr());
            skdMeldingTrans1.setAdressenavn(adresse.getAdresse());
            skdMeldingTrans1.setPostnummer(adresse.getPostnr());
            skdMeldingTrans1.setHusBruk(adresse.getHusnummer());
            skdMeldingTrans1.setGateGaard(adresse.getGatekode());

            updateAdresse(adresse, barn);

            skdMeldingTrans1.setAdressetype("O");
        } else {
            skdMeldingTrans1.setAdressetype("M");
        }

    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setSivilstand("1");
        skdMeldingTrans1.setTranstype("1");
        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_FOEDSELSMELDING);
        skdMeldingTrans1.setStatuskode("1");
    }


    private void updateAdresse(Gateadresse adresse, Person barn) {
        barn.setBoadresse(adresse);

        savePersonListService.execute(Arrays.asList(barn));
    }
}
