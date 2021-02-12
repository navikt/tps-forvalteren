package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.PBOX;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.STED;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.UTAD;
import static no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggType.CO_NAVN;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomEtternavn;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomFornavn;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigGateAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigPboxAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigStedAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigUtadAdresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsMidlertidigAdresseRequest;
import no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggAdressetype;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@Service
@RequiredArgsConstructor
public class MidlertidigAdresseMappingService {

    private final RandomAdresseService randomAdresseService;

    public void mapAdresse(RsPersonBestillingKriteriumRequest req,
            Person hovedPerson, List<Person> partnere, List<Person> barn) {

        mapPersonMidlertidigAdresse(hovedPerson, req.getMidlertidigAdresse());

        for (int i = 0; i < partnere.size(); i++) {
            mapPersonMidlertidigAdresse(partnere.get(i), req.getRelasjoner().getPartnere()
                    .get(i % req.getRelasjoner().getPartnere().size()).getMidlertidigAdresse());
        }
        for (int i = 0; i < barn.size(); i++) {
            mapPersonMidlertidigAdresse(barn.get(i), req.getRelasjoner().getBarn()
                    .get(i % req.getRelasjoner().getBarn().size()).getMidlertidigAdresse());
        }
    }

    private void mapPersonMidlertidigAdresse(Person person, RsMidlertidigAdresseRequest midlertidigAdresse) {

        if (nonNull(midlertidigAdresse)) {
            MidlertidigAdresse adresse;

            if (STED == midlertidigAdresse.getAdressetype()) {
                adresse = mapStedsadresse(midlertidigAdresse);

            } else if (PBOX == midlertidigAdresse.getAdressetype()) {
                adresse = mapPboxAadresse(midlertidigAdresse);

            } else if (UTAD == midlertidigAdresse.getAdressetype()) {
                adresse = mapUtadAdresse(midlertidigAdresse);

            } else {
                adresse = mapGateadresse(midlertidigAdresse);
            }
            adresse.setPerson(person);
            person.getMidlertidigAdresse().add(adresse);
        }
    }

    private static MidlertidigAdresse mapUtadAdresse(RsMidlertidigAdresseRequest midlertidigAdresse) {
        MidlertidigUtadAdresse adresse = MidlertidigUtadAdresse.builder()
                .postLinje1(midlertidigAdresse.getUtenlandskAdresse().getPostLinje1())
                .postLinje2(midlertidigAdresse.getUtenlandskAdresse().getPostLinje2())
                .postLinje3(midlertidigAdresse.getUtenlandskAdresse().getPostLinje3())
                .postLand(midlertidigAdresse.getUtenlandskAdresse().getPostLand())
                .build();
        adresse.setGyldigTom(getGyldigTom(midlertidigAdresse.getGyldigTom()));
        return adresse;
    }

    private static MidlertidigAdresse mapPboxAadresse(RsMidlertidigAdresseRequest midlertidigAdresse) {

        MidlertidigPboxAdresse adresse = MidlertidigPboxAdresse.builder()
                .postboksnr(midlertidigAdresse.getNorskAdresse().getPostboksnr())
                .postboksAnlegg(midlertidigAdresse.getNorskAdresse().getPostboksAnlegg())
                .build();
        adresse.setPostnr(midlertidigAdresse.getNorskAdresse().getPostnr());
        adresse.setGyldigTom(getGyldigTom(midlertidigAdresse.getGyldigTom()));
        return adresse;
    }

    private static MidlertidigAdresse mapStedsadresse(RsMidlertidigAdresseRequest midlertidigAdresse) {

        MidlertidigStedAdresse adresse = MidlertidigStedAdresse.builder()
                .eiendomsnavn(midlertidigAdresse.getNorskAdresse().getEiendomsnavn())
                .build();
        adresse.setPostnr(midlertidigAdresse.getNorskAdresse().getPostnr());
        adresse.setGyldigTom(getGyldigTom(midlertidigAdresse.getGyldigTom()));
        adresse.setTilleggsadresse(getTilleggAdresse(midlertidigAdresse.getNorskAdresse().getTilleggsadresse()));
        return adresse;
    }

    private MidlertidigAdresse mapGateadresse(RsMidlertidigAdresseRequest midlertidigAdresse) {

        MidlertidigGateAdresse adresse;
        if (nonNull(midlertidigAdresse.getNorskAdresse()) &&
                isNotBlank(midlertidigAdresse.getNorskAdresse().getGatekode())) {
            adresse = MidlertidigGateAdresse.builder()
                    .gatenavn(midlertidigAdresse.getNorskAdresse().getGatenavn())
                    .gatekode(midlertidigAdresse.getNorskAdresse().getGatekode())
                    .husnr(midlertidigAdresse.getNorskAdresse().getHusnr())
                    .build();

            adresse.setPostnr(midlertidigAdresse.getNorskAdresse().getPostnr());

        } else {
            List<Adresse> adresser = randomAdresseService.hentRandomAdresse(1, midlertidigAdresse.getGateadresseNrInfo());
            adresse = MidlertidigGateAdresse.builder()
                    .gatenavn(((Gateadresse) adresser.get(0)).getAdresse())
                    .gatekode(((Gateadresse) adresser.get(0)).getGatekode())
                    .husnr(((Gateadresse) adresser.get(0)).getHusnummer())
                    .build();
            adresse.setPostnr(adresser.get(0).getPostnr());
        }

        adresse.setGyldigTom(getGyldigTom(midlertidigAdresse.getGyldigTom()));
        adresse.setTilleggsadresse(getTilleggAdresse(nonNull(midlertidigAdresse.getNorskAdresse()) ?
                midlertidigAdresse.getNorskAdresse().getTilleggsadresse() : null));
        return adresse;
    }

    private static LocalDateTime getGyldigTom(LocalDateTime gyldigTom) {

        return nonNull(gyldigTom) ? gyldigTom : LocalDateTime.now().plusYears(1);
    }

    private static String getTilleggAdresse(TilleggAdressetype tilleggAdressetype) {

        if (isNull(tilleggAdressetype)) {
            return null;
        }
        return CO_NAVN == tilleggAdressetype.getTilleggType() ?
                format("C/O %s %s", getRandomFornavn(), getRandomEtternavn()).toUpperCase() :
                format("%s: %s", tilleggAdressetype.getTilleggType(), tilleggAdressetype.getNummer())
                        .replace('_', '-');
    }
}
