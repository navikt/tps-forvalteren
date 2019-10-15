package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Component
public class ValidateOpprettRequest {

    private static final String UGYLDIG_IDENTTYPE = "bestilling.input.validation.ugyldig.identtype";
    private static final LocalDateTime LOWER_BOUND = of(1900, 1, 1, 0, 0, 0);

    @Autowired
    private MessageProvider messageProvider;

    public void validate(RsPersonBestillingKriteriumRequest request) {

        validate(request.getAntall(), request.getOpprettFraIdenter());
        validateDatoFoedtEtter(request.getFoedtEtter());
        validateDatoFoedtFoer(request.getFoedtFoer());
        validateDatoIntervall(request.getFoedtEtter(), request.getFoedtFoer());
        validateKjoenn(request.getKjonn());
        validateUtvandret(request);

        if (!request.getRelasjoner().getPartner().isEmpty()) {
            request.getRelasjoner().getPartner().forEach(partner -> {
                validateDatoFoedtEtter(partner.getFoedtEtter());
                validateDatoFoedtFoer(partner.getFoedtFoer());
                validateDatoIntervall(partner.getFoedtEtter(), partner.getFoedtFoer());
                validateKjoenn(partner.getKjonn());
            });
        }

        if (!request.getRelasjoner().getBarn().isEmpty()) {
            request.getRelasjoner().getBarn().forEach(barn -> {
                validateDatoFoedtEtter(barn.getFoedtEtter());
                validateDatoFoedtFoer(barn.getFoedtFoer());
                validateDatoIntervall(barn.getFoedtEtter(), barn.getFoedtFoer());
                validateKjoenn(barn.getKjonn());
            });
        }
    }

    private void validateUtvandret(RsPersonBestillingKriteriumRequest request) {
        if (nonNull(request.getUtvandretTilLand()) && nonNull(request.getIdenttype()) && !FNR.name().equals(request.getIdenttype())) {
            throw new TpsfFunctionalException(messageProvider.get(UGYLDIG_IDENTTYPE));
        }

        request.getRelasjoner().getBarn().forEach(barn -> {
            if (nonNull(barn.getUtvandretTilLand()) && nonNull(barn.getIdenttype()) && !FNR.name().equals(barn.getIdenttype())) {
                throw new TpsfFunctionalException(messageProvider.get(UGYLDIG_IDENTTYPE));
            }
        });
        request.getRelasjoner().getPartner().forEach(partner -> {
            if (nonNull(partner.getUtvandretTilLand()) && nonNull(partner.getIdenttype()) &&
                    !FNR.name().equals(partner.getIdenttype())) {
                throw new TpsfFunctionalException(messageProvider.get(UGYLDIG_IDENTTYPE));
            }
        });
    }

    private void validate(Integer antall, List<String> eksisterendeIdenter) {
        if (isNull(antall) && eksisterendeIdenter.isEmpty()) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.ugyldig.antall"));
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
        if (isNotBlank(kjoenn) && !"M".equals(kjoenn) && !"K".equals(kjoenn) && !"U".equals(kjoenn)) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.ugyldig.kjoenn"));
        }
    }
}
