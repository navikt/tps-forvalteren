package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Component
public class ValidateOpprettRequest {

    private static final LocalDateTime LOWER_BOUND = of(1900, 1, 1, 0, 0, 0);

    @Autowired
    private MessageProvider messageProvider;

    public void validate(RsPersonBestillingKriteriumRequest request) {

        validateDatoFoedtEtter(request.getFoedtEtter());
        validateDatoFoedtFoer(request.getFoedtFoer());
        validateDatoIntervall(request.getFoedtEtter(), request.getFoedtFoer());
        validateKjoenn(request.getKjonn());

        if (nonNull(request.getRelasjoner().getPartner())) {
            validateDatoFoedtEtter(request.getRelasjoner().getPartner().getFoedtEtter());
            validateDatoFoedtFoer(request.getRelasjoner().getPartner().getFoedtFoer());
            validateDatoIntervall(request.getRelasjoner().getPartner().getFoedtEtter(), request.getRelasjoner().getPartner().getFoedtFoer());
            validateKjoenn(request.getRelasjoner().getPartner().getKjonn());
        }

        if (!request.getRelasjoner().getBarn().isEmpty()) {
            request.getRelasjoner().getBarn().forEach(barn -> validateDatoFoedtEtter(barn.getFoedtEtter()));
            request.getRelasjoner().getBarn().forEach(barn -> validateDatoFoedtFoer(barn.getFoedtFoer()));
            request.getRelasjoner().getBarn().forEach(barn -> validateDatoIntervall(barn.getFoedtEtter(), barn.getFoedtFoer()));
            request.getRelasjoner().getBarn().forEach(barn -> validateKjoenn(barn.getKjonn()));
        }
    }

    private void validateDatoFoedtEtter(LocalDateTime datoFoedtEtter) {
        if (nonNull(datoFoedtEtter) && (datoFoedtEtter.isAfter(now()) || datoFoedtEtter.isBefore(LOWER_BOUND))) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.dato.foedt.etter"));
        }
    }

    private void validateDatoFoedtFoer(LocalDateTime datoFoedtFoer) {
        if (nonNull(datoFoedtFoer) && (datoFoedtFoer.isBefore(LOWER_BOUND) || datoFoedtFoer.isAfter(now()))) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.dato.foedt.foer"));
        }
    }

    private void validateDatoIntervall(LocalDateTime datoFoedtEtter, LocalDateTime datoFoedtFoer) {
        if (nonNull(datoFoedtEtter) && nonNull(datoFoedtFoer) && datoFoedtFoer.isBefore(datoFoedtEtter)) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.ugyldig.intervall"));
        }
    }

    private void validateKjoenn(String kjoenn) {
        if (nonNull(kjoenn) && !"M".equals(kjoenn) && !"K".equals(kjoenn) && !"U".equals(kjoenn)) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.ugyldig.kjoenn"));
        }
    }
}
