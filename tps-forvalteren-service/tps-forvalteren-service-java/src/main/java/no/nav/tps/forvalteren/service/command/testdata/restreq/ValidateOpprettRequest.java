package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_FOEDT_ETTER;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_FOEDT_FOER;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_UGYLDIG_INTERVALL;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_UGYLDIG_KJOENN;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Component
public class ValidateOpprettRequest {

    private static final LocalDateTime LOWER_BOUND = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    private static final LocalDateTime UPPER_BOUND = LocalDateTime.of(2039, 12, 31, 23, 59, 59);

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
        if (nonNull(datoFoedtEtter) && datoFoedtEtter.isAfter(UPPER_BOUND)) {
            throw new TpsfFunctionalException(messageProvider.get(BESTILLING_VALIDERING_FOEDT_ETTER));
        }
    }

    private void validateDatoFoedtFoer(LocalDateTime datoFoedtFoer) {
        if (nonNull(datoFoedtFoer) && datoFoedtFoer.isBefore(LOWER_BOUND)) {
            throw new TpsfFunctionalException(messageProvider.get(BESTILLING_VALIDERING_FOEDT_FOER));
        }
    }

    private void validateDatoIntervall(LocalDateTime datoFoedtEtter, LocalDateTime datoFoedtFoer) {
        if (nonNull(datoFoedtEtter) && nonNull(datoFoedtFoer) && datoFoedtFoer.isBefore(datoFoedtEtter)) {
            throw new TpsfFunctionalException(messageProvider.get(BESTILLING_VALIDERING_UGYLDIG_INTERVALL));
        }
    }

    private void validateKjoenn(String kjoenn) {
        if (nonNull(kjoenn) && !"M".equals(kjoenn) && !"K".equals(kjoenn) && !"U".equals(kjoenn)) {
            throw new TpsfFunctionalException(messageProvider.get(BESTILLING_VALIDERING_UGYLDIG_KJOENN));
        }
    }
}
