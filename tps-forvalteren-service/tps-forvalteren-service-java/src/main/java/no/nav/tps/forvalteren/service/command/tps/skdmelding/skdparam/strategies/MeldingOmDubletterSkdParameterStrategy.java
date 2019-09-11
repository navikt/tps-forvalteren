package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.MeldingOmDubletterSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

/**
 * Service is used to merge two persons in TPS
 */
@Service
public class MeldingOmDubletterSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_DUBLETT_FNR = "58";
    private static final String AARSAKSKODE_FOR_DUBLETT_DNR = "94";
    private static final String TILDELINGSKODE_PA_FORSVUNNET_MELDING = "0";

    @Override
    public SkdMeldingTrans1 execute(Person duplicatePerson) {

        return SkdMeldingTrans1.builder()
                .aarsakskode(FNR.name().equals(duplicatePerson.getIdenttype()) ? AARSAKSKODE_FOR_DUBLETT_FNR : AARSAKSKODE_FOR_DUBLETT_DNR)
                .tildelingskode(hentTildelingskode())
                .fodselsdato(getDato(duplicatePerson.getIdentHistorikk().get(0).getPerson()))
                .personnummer(getPersonnr(duplicatePerson.getIdentHistorikk().get(0).getPerson()))
                .maskindato(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(duplicatePerson.getRegdato())))
                .maskintid(ConvertDateToString.hhMMss(duplicatePerson.getRegdato()))
                .tidligereFnrDnr(duplicatePerson.getIdent())
                .build();
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_PA_FORSVUNNET_MELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof MeldingOmDubletterSkdParametere;
    }

    private static String getPersonnr(Person person) {
        return person.getIdent().substring(6, 11);
    }

    private static String getDato(Person person) {
        return person.getIdent().substring(0, 6);
    }
}
