package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.ctg.s610.domain.BankkontoNorgeType;
import no.nav.tps.ctg.s610.domain.BoAdresseType;
import no.nav.tps.ctg.s610.domain.PostAdresseType;
import no.nav.tps.ctg.s610.domain.S610BrukerType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.ctg.s610.domain.TelefonType;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;

@Component
public class S610PersonMappingStrategy implements MappingStrategy {

    private static final String GATE_ADRESSE = "OFFA";
    private static final String MATR_ADRESSE = "MATR";
    private static final String BOLIGNR = "BOLIGNR: ";
    private static final String MOBIL = "MOBI";
    private static final String HJEM = "HJET";
    private static final String POST_UTLAND = "PUTL";
    private static final String POST_NORGE = "POST";
    private static final String NORGE = "NOR";

    @Override public void register(MapperFactory factory) {
        factory.classMap(S610PersonType.class, Person.class)
                .customize(new CustomMapper<S610PersonType, Person>() {
                    @Override public void mapAtoB(S610PersonType tpsPerson,
                            Person person, MappingContext context) {

                        person.setIdent(tpsPerson.getFodselsnummer());
                        person.setIdenttype(tpsPerson.getIdentType());
                        person.setFornavn(tpsPerson.getPersonnavn().getAllePersonnavn().getFornavn());
                        person.setMellomnavn(tpsPerson.getPersonnavn().getAllePersonnavn().getMellomnavn());
                        person.setEtternavn(tpsPerson.getPersonnavn().getAllePersonnavn().getEtternavn());
                        person.setForkortetNavn(tpsPerson.getPersonnavn().getAllePersonnavn().getKortnavn());
                        person.getStatsborgerskap().add(Statsborgerskap.builder()
                                .statsborgerskap(tpsPerson.getStatsborgerskapDetalj().getKodeStatsborgerskap())
                                .statsborgerskapRegdato(getTimestamp(tpsPerson.getStatsborgerskapDetalj().getDatoStatsborgerskap()))
                                .build());
                        person.setDoedsdato(getTimestamp(tpsPerson.getDatoDo()));
                        person.setTknr(getTknr(tpsPerson.getBruker().getNAVenhetDetalj()));
                        person.setTknavn(getTknavn(tpsPerson.getBruker().getNAVenhetDetalj()));
                        person.setGtType(getGtType(tpsPerson.getBruker().getGeografiskTilknytning()));
                        person.setGtVerdi(getGtVerdi(tpsPerson.getBruker().getGeografiskTilknytning()));
                        person.setGtRegel(tpsPerson.getBruker().getRegelForGeografiskTilknytning());
                        person.setSprakKode(tpsPerson.getBruker().getPreferanser().getSprak());
                        person.setBankkontonr(getBankkontnr(tpsPerson.getBankkontoNorge()));
                        person.setBankkontonrRegdato(getBankkontoRegdato(tpsPerson.getBankkontoNorge()));
                        person.setBankkontonrRegdato(getBankkontoRegdato(tpsPerson.getBankkontoNorge()));
                        person.setTelefonLandskode_1(getTlfnrLandskode(tpsPerson.getBruker().getTelefoner(), MOBIL));
                        person.setTelefonnummer_1(getTelefonnr(tpsPerson.getBruker().getTelefoner(), MOBIL));
                        person.setTelefonLandskode_2(getTlfnrLandskode(tpsPerson.getBruker().getTelefoner(), HJEM));
                        person.setTelefonnummer_2(getTelefonnr(tpsPerson.getBruker().getTelefoner(), HJEM));
                        fixTelefonnr(person);
                        person.setPersonStatus(tpsPerson.getPersonstatusDetalj().getKodePersonstatus().name());
                        person.setBeskrSikkerhetsTiltak(tpsPerson.getBruker().getSikkerhetsTiltak().getBeskrSikkerhetsTiltak());
                        person.setTypeSikkerhetsTiltak(tpsPerson.getBruker().getSikkerhetsTiltak().getTypeSikkerhetsTiltak());
                        person.setSikkerhetsTiltakDatoFom(getTimestamp(tpsPerson.getBruker().getSikkerhetsTiltak().getSikrFom()));
                        person.setSikkerhetsTiltakDatoTom(getTimestamp(tpsPerson.getBruker().getSikkerhetsTiltak().getSikrTom()));
                        mapBoadresse(tpsPerson, person);
                        mapPostadresse(tpsPerson, person);
                    }

                })
                .exclude("statsborgerskap")
                .byDefault()
                .register();
    }

    private static void mapPostadresse(S610PersonType tpsPerson, Person person) {

        PostAdresseType tpsPostadresse = tpsPerson.getPostAdresse().getFullPostAdresse();

        Postadresse postadresse = null;
        if (isNotBlank(tpsPostadresse.getAdresseType())) {
            postadresse = Postadresse.builder()
                    .postLinje1(tpsPostadresse.getAdresse1())
                    .postLinje2(tpsPostadresse.getAdresse2())
                    .postLinje3(tpsPostadresse.getAdresse3())
                    .build();

            if (POST_NORGE.equals(tpsPostadresse.getAdresseType())) {

                String poststed = format("%s %s", tpsPostadresse.getPostnr(), tpsPostadresse.getPostnr());
                if (isBlank(postadresse.getPostLinje1())) {
                    postadresse.setPostLinje1(poststed);
                } else if (isBlank(postadresse.getPostLinje2())) {
                    postadresse.setPostLinje2(poststed);
                } else if (isBlank(postadresse.getPostLinje3())) {
                    postadresse.setPostLinje3(poststed);
                }
                postadresse.setPostLand(NORGE);

            } else if (POST_UTLAND.equals(tpsPostadresse.getAdresseType())) {

                postadresse.setPostLand(tpsPostadresse.getLandKode());
            }
            person.getPostadresse().add(postadresse);
        }
    }

    private static void mapBoadresse(S610PersonType tpsPerson, Person person) {

        BoAdresseType tpsBoadresse = tpsPerson.getBostedsAdresse().getFullBostedsAdresse();

        Adresse adresse = null;
        if (GATE_ADRESSE.equals(tpsBoadresse.getAdresseType())) {

            adresse = Gateadresse.builder()
                    .adresse(tpsBoadresse.getOffAdresse().getGateNavn())
                    .husnummer(tpsBoadresse.getOffAdresse().getHusnr())
                    .build();

        } else if (MATR_ADRESSE.equals(tpsBoadresse.getAdresseType())) {

            adresse = Matrikkeladresse.builder()
                    .mellomnavn(tpsBoadresse.getMatrAdresse().getMellomAdresse())
                    .gardsnr(tpsBoadresse.getMatrAdresse().getGardsnr())
                    .bruksnr(tpsBoadresse.getMatrAdresse().getBruksnr())
                    .festenr(tpsBoadresse.getMatrAdresse().getFestenr())
                    .undernr(tpsBoadresse.getMatrAdresse().getUndernr())
                    .build();
        }

        if (nonNull(adresse)) {

            if (isNotBlank(tpsBoadresse.getTilleggsAdresseSKD())) {
                adresse.setTilleggsadresse(tpsBoadresse.getTilleggsAdresseSKD());
            } else if (isNotBlank(tpsBoadresse.getBolignr())) {
                adresse.setTilleggsadresse(BOLIGNR + tpsBoadresse.getBolignr());
            }
            adresse.setPostnr(tpsBoadresse.getPostnr());
            adresse.setKommunenr(tpsBoadresse.getKommunenr());
            person.getBoadresse().add(adresse);
        }
    }

    private static void fixTelefonnr(Person person) {

        if (isBlank(person.getTelefonnummer_1()) && isNotBlank(person.getTelefonnummer_2())) {
            person.setTelefonnummer_1(person.getTelefonnummer_2());
            person.setTelefonLandskode_1(person.getTelefonLandskode_2());
            person.setTelefonnummer_2(null);
            person.setTelefonLandskode_2(null);
        }
    }

    private static LocalDateTime getBankkontoRegdato(BankkontoNorgeType bankkontoNorge) {

        return nonNull(bankkontoNorge) && isNotBlank(bankkontoNorge.getRegTidspunkt()) ?
                LocalDate.parse(bankkontoNorge.getRegTidspunkt()).atStartOfDay() : null;
    }

    private static String getBankkontnr(BankkontoNorgeType bankkontoNorge) {

        return nonNull(bankkontoNorge) && isNotBlank(bankkontoNorge.getKontoNummer()) ?
                format("%s.%s.%s", bankkontoNorge.getKontoNummer().substring(0, 4),
                        bankkontoNorge.getKontoNummer().substring(4, 6), bankkontoNorge.getKontoNummer().substring(6)) : null;
    }

    private static String getTlfnrLandskode(S610BrukerType.Telefoner telefoner, String telefontype) {

        return nonNull(telefoner) ? telefoner.getTelefon().stream()
                .filter(telefon -> telefontype.equals(telefon.getTlfType()))
                .map(TelefonType::getTlfLandkode)
                .findFirst().orElse(null) : null;
    }

    private String getTelefonnr(S610BrukerType.Telefoner telefoner, String telefontype) {

        return nonNull(telefoner) ? telefoner.getTelefon().stream()
                .filter(telefon -> telefontype.equals(telefon.getTlfType()))
                .map(TelefonType::getTlfNummer)
                .findFirst().orElse(null) : null;
    }

    private static LocalDateTime getTimestamp(String dato) {

        return isNotBlank(dato) ? LocalDate.parse(dato).atStartOfDay() : null;
    }

    private String getTknavn(S610BrukerType.NAVenhetDetalj naVenhetDetalj) {

        return nonNull(naVenhetDetalj) ? naVenhetDetalj.getKodeNAVenhetBeskr() : null;
    }

    private static String getTknr(S610BrukerType.NAVenhetDetalj naVenhetDetalj) {

        return nonNull(naVenhetDetalj) ? naVenhetDetalj.getKodeNAVenhet() : null;
    }

    private static String getGtType(S610BrukerType.GeografiskTilknytning geoTilknytning) {

        if (nonNull(geoTilknytning)) {
            if (isNotBlank(geoTilknytning.getKommunenr())) {
                return "KNR";
            } else if (isNotBlank(geoTilknytning.getLandKode())) {
                return "LAND";
            } else if (isNotBlank(geoTilknytning.getBydel())) {
                return "BYDEL";
            }
        }
        return null;
    }

    private static String getGtVerdi(S610BrukerType.GeografiskTilknytning geoTilknytning) {

        if (nonNull(geoTilknytning)) {
            if (isNotBlank(geoTilknytning.getKommunenr())) {
                return geoTilknytning.getKommunenr();
            } else if (isNotBlank(geoTilknytning.getLandKode())) {
                return geoTilknytning.getLandKode();
            } else if (isNotBlank(geoTilknytning.getBydel())) {
                return geoTilknytning.getBydel();
            }
        }
        return null;
    }
}
