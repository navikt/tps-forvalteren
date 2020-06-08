package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.GATE;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.PBOX;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.STED;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.UTAD;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigGateAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigPboxAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigStedAdresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.Kontonummer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.NavAdresse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.NorskKontonummer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.NyTelefon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.NyttSprak;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.Spraak;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.Telefon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.TiadAdresse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest.UtadAdresse;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;

@Service
public class KontaktopplysningerService {

    public List<TpsNavEndringsMelding> execute(Person person, Set<String> environmentSet) {

        return environmentSet.stream().map(environment -> TpsNavEndringsMelding.builder()
                .melding(TpsEndreKontaktopplysningerRequest.builder()
                        .endringAvNAVadresse(NavAdresse.builder()
                                .nyAdresseNavNorge(getTiadadresse(person))
                                .nyAdresseNavUtland(getUtadadresse(person))
                                .build())
                        .endringAvTelefon(getTelefonnumre(person))
                        .endringAvSprak(getSpraak(person))
                        .endringAvKontonr(getKontonumre(person))
                        .build())
                .miljo(environment)
                .build())
                .collect(Collectors.toList());
    }

    private Kontonummer getKontonumre(Person person) {
        if (isNull(person.getBankkontonr())) {
            return null;
        }
        return Kontonummer.builder()
                .endreKontonrNorsk(NorskKontonummer.builder()
                        .giroNrNorsk(person.getBankkontonr().replace(".", ""))
                        .build())
                .build();
    }

    private static Spraak getSpraak(Person person) {
        return Spraak.builder()
                .endreSprak(NyttSprak.builder()
                        .sprakKode(person.getSprakKode())
                        .datoSprak(nonNull(person.getDatoSprak()) ? person.getDatoSprak().toLocalDate() : LocalDate.now())
                        .build())
                .build();
    }

    private static Telefon getTelefonnumre(Person person) {
        if (isBlank(person.getTelefonnummer_1())) {
            return null;
        }
        List<NyTelefon> telefonnumre = Lists.newArrayList(NyTelefon.builder()
                .telefonLandkode(person.getTelefonLandskode_1())
                .telefonNr(person.getTelefonnummer_1())
                .typeTelefon("MOBI")
                .build());

        if (isNotBlank(person.getTelefonnummer_2())) {
            telefonnumre.add(NyTelefon.builder()
                    .telefonLandkode(person.getTelefonLandskode_2())
                    .telefonNr(person.getTelefonnummer_2())
                    .typeTelefon("HJET")
                    .build());
        }
        return Telefon.builder()
                .nyTelefon(telefonnumre)
                .build();
    }

    private static UtadAdresse getUtadadresse(Person person) {
        if (person.getMidlertidigAdresse().isEmpty() || !UTAD.name().equals(person.getMidlertidigAdresse().get(0).getAdressetype())) {
            return null;
        }
        return UtadAdresse.builder()
                .adresse1(((MidlertidigAdresse.MidlertidigUtadAdresse) person.getMidlertidigAdresse().get(0)).getPostLinje1())
                .adresse2(((MidlertidigAdresse.MidlertidigUtadAdresse) person.getMidlertidigAdresse().get(0)).getPostLinje2())
                .adresse3(((MidlertidigAdresse.MidlertidigUtadAdresse) person.getMidlertidigAdresse().get(0)).getPostLinje3())
                .kodeLand(((MidlertidigAdresse.MidlertidigUtadAdresse) person.getMidlertidigAdresse().get(0)).getPostLand())
                .datoTom(person.getMidlertidigAdresse().get(0).getGyldigTom().toLocalDate())
                .build();
    }

    private static TiadAdresse getTiadadresse(Person person) {

        if (person.getMidlertidigAdresse().isEmpty() || UTAD.name().equals(person.getMidlertidigAdresse().get(0).getAdressetype())) {
            return null;
        }
        switch (MidlertidigAdressetype.valueOf(person.getMidlertidigAdresse().get(0).getAdressetype())) {
        case PBOX:
            return TiadAdresse.builder()
                    .postboksnr(((MidlertidigPboxAdresse) person.getMidlertidigAdresse().get(0)).getPostboksnr())
                    .postboksAnlegg(((MidlertidigPboxAdresse) person.getMidlertidigAdresse().get(0)).getPostboksAnlegg())
                    .postnr(person.getMidlertidigAdresse().get(0).getPostnr())
                    .datoTom(person.getMidlertidigAdresse().get(0).getGyldigTom().toLocalDate())
                    .typeAdresseNavNorge(PBOX.name())
                    .build();
        case STED:
            return TiadAdresse.builder()
                    .typeTilleggsLinje(getTypeTilleggslinje(person.getMidlertidigAdresse().get(0).getTilleggsadresse()))
                    .tilleggsLinje(getTilleggslinje(person.getMidlertidigAdresse().get(0).getTilleggsadresse()))
                    .eiendomsnavn(((MidlertidigStedAdresse) person.getMidlertidigAdresse().get(0)).getEiendomsnavn())
                    .postnr(person.getMidlertidigAdresse().get(0).getPostnr())
                    .datoTom(person.getMidlertidigAdresse().get(0).getGyldigTom().toLocalDate())
                    .typeAdresseNavNorge(STED.name())
                    .build();
        case GATE:
        default:
            return TiadAdresse.builder()
                    .typeTilleggsLinje(getTypeTilleggslinje(person.getMidlertidigAdresse().get(0).getTilleggsadresse()))
                    .tilleggsLinje(getTilleggslinje(person.getMidlertidigAdresse().get(0).getTilleggsadresse()))
                    .eiendomsnavn(((MidlertidigGateAdresse) person.getMidlertidigAdresse().get(0)).getGatenavn())
                    .gatekode(((MidlertidigGateAdresse) person.getMidlertidigAdresse().get(0)).getGatekode())
                    .husnr(((MidlertidigGateAdresse) person.getMidlertidigAdresse().get(0)).getHusnr())
                    .postnr(person.getMidlertidigAdresse().get(0).getPostnr())
                    .datoTom(person.getMidlertidigAdresse().get(0).getGyldigTom().toLocalDate())
                    .typeAdresseNavNorge(GATE.name())
                    .build();
        }
    }

    private static String getTypeTilleggslinje(String tilleggsadresse) {
        return isNotBlank(tilleggsadresse) ? tilleggsadresse.split(" ")[0] : null;
    }

    private static String getTilleggslinje(String tillegssadresse) {
        return isNotBlank(tillegssadresse) ? tillegssadresse.substring(tillegssadresse.indexOf(" ")) : null;
    }
}
