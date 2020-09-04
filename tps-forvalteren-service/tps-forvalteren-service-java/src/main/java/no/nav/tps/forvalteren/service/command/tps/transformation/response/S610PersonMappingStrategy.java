package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.PBOX;
import static no.nav.tps.forvalteren.domain.rs.MidlertidigAdressetype.STED;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.ctg.s610.domain.BankkontoNorgeType;
import no.nav.tps.ctg.s610.domain.BoAdresseType;
import no.nav.tps.ctg.s610.domain.NavTIADType;
import no.nav.tps.ctg.s610.domain.PostAdresseType;
import no.nav.tps.ctg.s610.domain.S610BrukerType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.ctg.s610.domain.TelefonType;
import no.nav.tps.ctg.s610.domain.UtlandsAdresseType;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigGateAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigPboxAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigStedAdresse;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse.MidlertidigUtadAdresse;
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
    private static final String TRUE = "J";
    private static final String TPS = "TPS";

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
                                .person(person)
                                .build());
                        person.setDoedsdato(isNotBlank(tpsPerson.getDatoDo()) ?
                                LocalDate.parse(tpsPerson.getDatoDo()).atStartOfDay() : null);
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
                        mapUtadAdresse(tpsPerson, person);
                        mapTiadAdresse(tpsPerson, person);
                        person.setSpesreg(tpsPerson.getBruker().getDiskresjonDetalj().getKodeDiskresjon());
                        person.setSpesregDato(getTimestamp(tpsPerson.getBruker().getDiskresjonDetalj().getDiskresjonTidspunkt()));
                        person.setEgenAnsattDatoFom(isNotBlank(tpsPerson.getBruker().getEgenAnsatt().getFom()) ?
                                LocalDate.parse(tpsPerson.getBruker().getEgenAnsatt().getFom()).atStartOfDay() : null);
                        person.setSivilstand(getSivilstand(tpsPerson));
                        person.setSivilstandRegdato(getTimestamp(tpsPerson.getSivilstandDetalj().getSivilstTidspunkt()));
                        person.setImportFra(TPS);
                    }

                })
                .exclude("statsborgerskap")
                .byDefault()
                .register();
    }

    public static LocalDateTime getTimestamp(String dato) {

        return isNotBlank(dato) ? LocalDate.parse(dato).atStartOfDay() : LocalDateTime.now();
    }

    public static String getSivilstand(S610PersonType tpsPerson) {

        return nonNull(tpsPerson.getSivilstandDetalj()) && nonNull(tpsPerson.getSivilstandDetalj().getKodeSivilstand()) ?
                tpsPerson.getSivilstandDetalj().getKodeSivilstand().name() : null;
    }

    private static void mapTiadAdresse(S610PersonType tpsPerson, Person person) {

        NavTIADType tiadAdresse = tpsPerson.getBruker().getPostadresseNorgeNAV();

        if (nonNull(tiadAdresse) && isNotBlank(tiadAdresse.getTypeAdresseNavNorge())) {
            MidlertidigAdresse midlertidigAdresse;

            if (STED.name().equals(tiadAdresse.getTypeAdresseNavNorge())) {
                midlertidigAdresse = MidlertidigStedAdresse.builder()
                        .eiendomsnavn(tiadAdresse.getEiendomsnavn())
                        .build();
            } else if (PBOX.name().equals(tiadAdresse.getTypeAdresseNavNorge())) {
                midlertidigAdresse = MidlertidigPboxAdresse.builder()
                        .postboksAnlegg(tiadAdresse.getPostboksAnlegg())
                        .postboksnr(tiadAdresse.getPostboksnr())
                        .build();
            } else {
                midlertidigAdresse = MidlertidigGateAdresse.builder()
                        .gatenavn(tiadAdresse.getGatenavn())
                        .gatekode(tiadAdresse.getGatekode())
                        .husnr(tiadAdresse.getHusnr())
                        .build();
            }
            midlertidigAdresse.setGyldigTom(getTimestamp(tiadAdresse.getDatoTom()));
            midlertidigAdresse.setPostnr(tiadAdresse.getPostnr());
            if (isNotBlank(tiadAdresse.getTilleggsLinje())) {
                midlertidigAdresse.setTilleggsadresse(tiadAdresse.getTilleggsLinje());
            } else if (isNotBlank(tiadAdresse.getBolignr())) {
                midlertidigAdresse.setTilleggsadresse(BOLIGNR + tiadAdresse.getBolignr());
            }
            midlertidigAdresse.setPerson(person);
            person.getMidlertidigAdresse().add(midlertidigAdresse);
        }
    }

    private static void mapUtadAdresse(S610PersonType tpsPerson, Person person) {

        UtlandsAdresseType utenlandsadresse = tpsPerson.getBruker().getPostadresseUtlandNAV();

        if (nonNull(utenlandsadresse) && isNotBlank(utenlandsadresse.getAdresseType())) {
            MidlertidigAdresse midlertidigAdresse = MidlertidigUtadAdresse.builder()
                    .postLinje1(utenlandsadresse.getAdresse1())
                    .postLinje2(utenlandsadresse.getAdresse2())
                    .postLinje3(utenlandsadresse.getAdresse3())
                    .postLand(utenlandsadresse.getLandKode())
                    .build();
            midlertidigAdresse.setPerson(person);
            person.getMidlertidigAdresse().add(midlertidigAdresse);
        }
    }

    private static void mapPostadresse(S610PersonType tpsPerson, Person person) {

        PostAdresseType tpsPostadresse = tpsPerson.getPostAdresse().getFullPostAdresse();

        if (isNotBlank(tpsPostadresse.getAdresseType())) {

            Postadresse postadresse = Postadresse.builder()
                    .postLinje1(tpsPostadresse.getAdresse1())
                    .postLinje2(tpsPostadresse.getAdresse2())
                    .postLinje3(tpsPostadresse.getAdresse3())
                    .person(person)
                    .build();

            if (POST_NORGE.equals(tpsPostadresse.getAdresseType())) {

                String poststed = format("%s %s", tpsPostadresse.getPostnr(), tpsPostadresse.getPoststed());
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
                    .husnummer(skipLeadZeros(tpsBoadresse.getOffAdresse().getHusnr()))
                    .gatekode(tpsBoadresse.getOffAdresse().getGatekode())
                    .build();

        } else if (MATR_ADRESSE.equals(tpsBoadresse.getAdresseType())) {

            adresse = Matrikkeladresse.builder()
                    .mellomnavn(tpsBoadresse.getMatrAdresse().getMellomAdresse())
                    .gardsnr(skipLeadZeros(tpsBoadresse.getMatrAdresse().getGardsnr()))
                    .bruksnr(skipLeadZeros(tpsBoadresse.getMatrAdresse().getBruksnr()))
                    .festenr(skipLeadZeros(tpsBoadresse.getMatrAdresse().getFestenr()))
                    .undernr(skipLeadZeros(tpsBoadresse.getMatrAdresse().getUndernr()))
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
            adresse.setPerson(person);
            person.getBoadresse().add(adresse);
        }
    }

    private static String skipLeadZeros(String number) {

        return StringUtils.isNumeric(number) ?
                Integer.valueOf(number).toString() :
                number;
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
                .map(telefon ->
                        isNotBlank(telefon.getTlfLandkode()) ?
                                telefon.getTlfLandkode() : "+47"
                )
                .findFirst().orElse(null) : null;
    }

    private String getTelefonnr(S610BrukerType.Telefoner telefoner, String telefontype) {

        return nonNull(telefoner) ? telefoner.getTelefon().stream()
                .filter(telefon -> telefontype.equals(telefon.getTlfType()))
                .map(TelefonType::getTlfNummer)
                .findFirst().orElse(null) : null;
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
