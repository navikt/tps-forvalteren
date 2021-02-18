package no.nav.tps.forvalteren.service.command.testdata.restreq;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Sivilstatus;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.ENKE_ELLER_ENKEMANN;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.GIFT;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.GJENLEVENDE_PARTNER;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.REGISTRERT_PARTNER;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SEPARERT;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SEPARERT_PARTNER;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SKILT;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SKILT_PARTNER;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.UGIFT;

@Service
@RequiredArgsConstructor
public class ValidateSivilstandService {

    private static final LocalDateTime START_OF_ERA = LocalDateTime.of(1900,1,1,0,0);

    private final MessageProvider messageProvider;

    public void validateStatus(RsPersonBestillingKriteriumRequest request) {

        if (nonNull(request.getRelasjoner().getPartner()) && !request.getRelasjoner().getPartnere().isEmpty()) {
            throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.partnere"));
        }

        if (!request.getRelasjoner().getPartnere().isEmpty()) {
            validateInnhold(request);
            validateDatoer(request.getRelasjoner().getPartnere());

            AtomicBoolean harVaertGift = new AtomicBoolean(false);
            request.getRelasjoner().getPartnere().forEach(partnerRequest ->
                    partnerRequest.getSivilstander().forEach(sivilstand -> {
                        if (isGift(sivilstand.getSivilstand())) {
                            harVaertGift.set(true);
                        }
                    }));

            validateHistoricIntegrity(request.getRelasjoner().getPartnere(), harVaertGift.get());
        }
    }

    private void validateInnhold(RsPersonBestillingKriteriumRequest request) {

        for (int i = 0; i < request.getRelasjoner().getPartnere().size(); i++) {
            for (int j = 0; j < request.getRelasjoner().getPartnere().get(i).getSivilstander().size(); j++) {
                if (!Sivilstatus.exists(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstand())) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.ugyldig-kode",
                            request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstand()));
                }
                if (i > 0 && isNull(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstandRegdato())) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.regdato.required.multiple"));
                }
                if (j > 0 && isNull(request.getRelasjoner().getPartnere().get(i).getSivilstander().get(j).getSivilstandRegdato())) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.regdato.multiple"));
                }
            }
        }
    }

    private void validateDatoer(List<RsPartnerRequest> partnereRequest) {

        LocalDateTime dateComperator = START_OF_ERA;

        for (RsPartnerRequest partner : partnereRequest) {
            for (RsSivilstandRequest sivilstandRequest : partner.getSivilstander()) {

                if (sivilstandRequest.getSivilstandRegdato().isBefore(dateComperator.plusDays(2))) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.datoer"));
                } else {
                    dateComperator = sivilstandRequest.getSivilstandRegdato();
                }
            }
        }
    }

    private void validateHistoricIntegrity(List<RsPartnerRequest> partnereRequest, boolean harVaertGift) {

        String currentSivilstand = UGIFT.getKodeverkskode();

        for (RsPartnerRequest partner : partnereRequest) {
            for (RsSivilstandRequest sivilstandRequest : partner.getSivilstander()) {

                if (isUgift(sivilstandRequest.getSivilstand()) && !isUgift(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.ugift"));
                }
                if (isSeparert(sivilstandRequest.getSivilstand()) && isSkilt(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.separert"));
                }
                if (sivilstandRequest.getSivilstand().equals(currentSivilstand)) {
                    throw new TpsfFunctionalException(messageProvider.get("bestilling.input.validation.sivilstand.samme"));
                }
                currentSivilstand = sivilstandRequest.getSivilstand();

                validateUgiftAndSivilstandHistorikk(sivilstandRequest, harVaertGift);
            }
        }
    }

    private void validateUgiftAndSivilstandHistorikk(RsSivilstandRequest sivilstand, boolean harVaertGift) {

        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, SEPARERT, SEPARERT_PARTNER, "bestilling.input.validation.sivilstand.separert.ugift");
        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, SKILT, SKILT_PARTNER, "bestilling.input.validation.sivilstand.skilt.ugift");
        validateUgiftAndSivilstandHistorikk(harVaertGift, sivilstand, ENKE_ELLER_ENKEMANN, GJENLEVENDE_PARTNER, "bestilling.input.validation.sivilstand.enke.ugift");
    }

    private void validateUgiftAndSivilstandHistorikk(boolean harVaertGift, RsSivilstandRequest sivilstand, Sivilstatus gift, Sivilstatus partner, String error) {
        if (!harVaertGift && gift.getKodeverkskode().equals(sivilstand.getSivilstand()) ||
                partner.getKodeverkskode().equals(sivilstand.getSivilstand())) {
            throw new TpsfFunctionalException(messageProvider.get(error));
        }
    }

    private static boolean isSeparert(String sivilstand) {
        return SEPARERT.getKodeverkskode().equals(sivilstand) ||
                SEPARERT_PARTNER.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isSkilt(String sivilstand) {
        return SKILT.getKodeverkskode().equals(sivilstand) ||
                SKILT_PARTNER.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isUgift(String sivilstand) {
        return UGIFT.getKodeverkskode().equals(sivilstand);
    }

    private static boolean isGift(String sivilstand) {
        return GIFT.getKodeverkskode().equals(sivilstand) ||
                REGISTRERT_PARTNER.getKodeverkskode().equals(sivilstand);
    }
}