package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.ctg.s610.domain.BankkontoNorgeType;
import no.nav.tps.ctg.s610.domain.S610BrukerType;
import no.nav.tps.ctg.s610.domain.S610PersonType;
import no.nav.tps.ctg.s610.domain.TelefonType;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;

@Component
public class S610PersonMappingStrategy implements MappingStrategy {

    private static final String MOBIL = "MOBI";
    private static final String HJEM = "HJET";

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
                        person.setPersonStatus(tpsPerson.getPersonstatusDetalj().getKodePersonstatus().name());
                    }
                })
                .exclude("statsborgerskap")
                .byDefault()
                .register();
    }

    private static LocalDateTime getBankkontoRegdato(BankkontoNorgeType bankkontoNorge) {

        return nonNull(bankkontoNorge) && nonNull(bankkontoNorge.getRegTidspunkt()) ?
                LocalDate.parse(bankkontoNorge.getRegTidspunkt()).atStartOfDay() : null;
    }

    private static String getBankkontnr(BankkontoNorgeType bankkontoNorge) {

        return nonNull(bankkontoNorge) ? format("%s.%s.%s", bankkontoNorge.getKontoNummer().substring(0,4),
                bankkontoNorge.getKontoNummer().substring(4,6), bankkontoNorge.getKontoNummer().substring(6)) : null;
    }

    private static String getTlfnrLandskode(S610BrukerType.Telefoner telefoner, String telefontype) {

        return telefoner.getTelefon().stream()
                .filter(telefon -> telefontype.equals(telefon.getTlfType()))
                .map(TelefonType::getTlfLandkode)
                .findFirst().orElse(null);
    }

    private String getTelefonnr(S610BrukerType.Telefoner telefoner, String telefontype) {

        return telefoner.getTelefon().stream()
                .filter(telefon -> telefontype.equals(telefon.getTlfType()))
                .map(TelefonType::getTlfNummer)
                .findFirst().orElse(null);
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
