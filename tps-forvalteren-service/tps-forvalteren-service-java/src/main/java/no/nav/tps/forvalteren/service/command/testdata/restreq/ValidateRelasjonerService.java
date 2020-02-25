package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BorHos;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.domain.rs.skd.IdentType;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Service
@RequiredArgsConstructor
public class ValidateRelasjonerService {

    private final MessageProvider messageProvider;

    public void isGyldig(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        validateAtPersonerEksister(hovedperson, request, personer);
        validateEksisterendeIdent(hovedperson, request, personer);
        validateHarBarnEksisterendeForeldre(request, personer);
        validateHarFellesAdresseSpesreg(hovedperson, request, personer);
        validateHarFellesAdresseAnnet(hovedperson, request, personer);
        validateHarFellesAdresseBarnBorHosDeg(request);
    }

    private void validateHarFellesAdresseBarnBorHosDeg(RsPersonBestillingRelasjonRequest request) {

        request.getRelasjoner().getPartnere().forEach(partner ->

                request.getRelasjoner().getBarn().forEach(barn -> {

                    if ((partner.getIdent().equals(barn.getPartnerIdent()) || request.getRelasjoner().getPartnere().size() == 1) &&
                            isTrue(partner.getHarFellesAdresse()) &&
                            BorHos.DEG == barn.getBorHos()) {
                        throw new TpsfFunctionalException(
                                messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.barn.bor.hos.deg", barn.getIdent()));
                    }
                })
        );
    }

    private void validateAtPersonerEksister(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        if (!personer.containsKey(hovedperson)) {
            throw new TpsfFunctionalException(
                    messageProvider.get("bestilling.relasjon.input.validation.hovedperson.ekistere.ikke", hovedperson));
        }

        request.getRelasjoner().getPartnere().forEach(partner -> {
            if (!personer.containsKey(partner.getIdent())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.partner.ekistere.ikke", partner.getIdent()));
            }
        });

        request.getRelasjoner().getBarn().forEach(barn -> {
            if (!personer.containsKey(barn.getIdent())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.barn.ekistere.ikke", barn.getIdent()));
            }
        });
    }

    private void validateHarFellesAdresseSpesreg(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartnere().forEach(partner -> {

            if (isTrue(partner.getHarFellesAdresse()) && (personer.get(partner.getIdent()).isKode6() ||
                    personer.get(hovedperson).isKode6())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.kode6"));
            }

            if (isTrue(partner.getHarFellesAdresse()) && (personer.get(partner.getIdent()).isUtenFastBopel() ||
                    personer.get(hovedperson).isUtenFastBopel())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.ufb"));
            }
        });
    }

    private void validateHarFellesAdresseAnnet(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        request.getRelasjoner().getPartnere().forEach(partner -> {

            if (isTrue(partner.getHarFellesAdresse()) && !isIdentTypeFnr(personer.get(partner.getIdent()).getIdenttype()) ||
                    !isIdentTypeFnr(personer.get(hovedperson).getIdenttype())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.ikke.fnr"));
            }

            if (isTrue(partner.getHarFellesAdresse()) && (personer.get(partner.getIdent()).isForsvunnet() ||
                    personer.get(hovedperson).isForsvunnet())) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.forsvunnet"));
            }
        });
    }

    private void validateEksisterendeIdent(String hovedperson, RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {

        personer.get(hovedperson).getRelasjoner().forEach(relasjon -> {
            request.getRelasjoner().getPartnere().forEach(partner -> {
                if (relasjon.getPersonRelasjonMed().getIdent().equals(partner.getIdent())) {
                    throw new TpsfFunctionalException(
                            messageProvider.get("bestilling.relasjon.input.validation.duplikat.partner", partner.getIdent()));
                }
            });

            request.getRelasjoner().getBarn().forEach(barnet -> {
                if (relasjon.getPersonRelasjonMed().getIdent().equals(barnet.getIdent())) {
                    throw new TpsfFunctionalException(
                            messageProvider.get("bestilling.relasjon.input.validation.duplikat.barn", barnet.getIdent()));
                }
            });
        });
    }

    private void validateHarBarnEksisterendeForeldre(RsPersonBestillingRelasjonRequest request, Map<String, Person> personer) {
        request.getRelasjoner().getBarn().forEach(barnet -> {
            AtomicInteger counter = new AtomicInteger(0);
            personer.get(barnet.getIdent()).getRelasjoner().forEach(barnRelasjon -> {

                if ("FAR".equals(barnRelasjon.getRelasjonTypeNavn()) || "MOR".equals(barnRelasjon.getRelasjonTypeNavn())) {
                    barnRelasjon.getPersonRelasjonMed().getRelasjoner().forEach(foreldreRelasjon -> {
                        if ("FOEDSEL".equals(foreldreRelasjon.getRelasjonTypeNavn())) {
                            counter.incrementAndGet();
                        }
                    });
                }
            });
            if (counter.intValue() > 1) {
                throw new TpsfFunctionalException(
                        messageProvider.get("bestilling.relasjon.input.validation.barn.har.foreldre", barnet.getIdent()));
            }
        });
    }

    private boolean isIdentTypeFnr(String identType) {
        return !IdentType.BOST.name().equals(identType) && !IdentType.DNR.name().equals(identType);

    }
}
